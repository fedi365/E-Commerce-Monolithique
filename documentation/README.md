# Documentation: Problèmes de configuration BD et leur résolution

Date: 2025-12-08

## Contexte
Application Spring Boot (Monolith) avec JPA/Hibernate, base de données PostgreSQL (Neon). Un `CommandLineRunner` insère des produits au démarrage et une API REST expose des endpoints CRUD `/products`.

## Problème 1: URL PostgreSQL invalide (psql vs JDBC)
- Symptôme: Erreur de connexion ou URL non reconnue côté Spring Boot/JDBC.
- Cause: L’URL fournie provenait de psql/libpq et contenait l’option `channel_binding=require`, qui n’est PAS supportée par le driver JDBC PostgreSQL.

### Avant (incorrect)
```
postgresql://neondb_owner:***@.../neondb?sslmode=require&channel_binding=require
```

### Après (correct – format JDBC)
Dans `src/main/resources/application.properties`:
```
spring.datasource.url=jdbc:postgresql://<host>/<database>?sslmode=require
spring.datasource.username=<user>
spring.datasource.password=<password>
```
Pour notre cas:
```
spring.datasource.url=jdbc:postgresql://ep-rough-unit-adyq3rq2-pooler.c-2.us-east-1.aws.neon.tech/neondb?sslmode=require
spring.datasource.username=neondb_owner
spring.datasource.password=********
```

Pourquoi? Les URLs JDBC doivent commencer par `jdbc:postgresql://` et n’acceptent pas les options spécifiques à psql comme `channel_binding`.

## Problème 2: « relation \"product\" does not exist » au démarrage
- Symptôme (logs):
```
org.postgresql.util.PSQLException: ERROR: relation "product" does not exist
... SQL [insert into product (name,price,quantity) values (?,?,?)]
```
- Cause: Hibernate tente d’insérer des données via le `CommandLineRunner` avant que la table ne soit créée dans PostgreSQL.

### Solution appliquée
Activer la gestion du schéma par Hibernate en environnement de dev/Neon:
```
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```
Ainsi, au démarrage, Hibernate crée/actualise la table `product` avant l’insertion des données initiales.

## Configuration finale (extrait)
`src/main/resources/application.properties`
```
spring.application.name=E-Commerce-Monolithique
server.port=8888
spring.datasource.url=jdbc:postgresql://ep-rough-unit-adyq3rq2-pooler.c-2.us-east-1.aws.neon.tech/neondb?sslmode=require
spring.datasource.username=neondb_owner
spring.datasource.password=********
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

## Stratégie de tests (isoler de la BD externe)
Pour éviter que les tests dépendent de Neon, utilisation d’une BD en mémoire H2 en mode compatibilité PostgreSQL.

`src/test/resources/application.properties`
```
spring.datasource.url=jdbc:h2:mem:testdb;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=false
spring.sql.init.mode=never
```

## Comment reproduire et vérifier
1. Démarrer l’application: `mvn spring-boot:run` ou exécuter la classe `ECommerceMonolithiqueApplication`.
2. Vérifier la création de la table et l’insertion initiale (logs SQL si `show-sql=true`).
3. Tester l’API CRUD:
   - POST http://localhost:8888/products
   - GET http://localhost:8888/products
   - GET http://localhost:8888/products/{id}
   - PUT http://localhost:8888/products/{id}
   - DELETE http://localhost:8888/products/{id}
4. Lancer les tests: `mvn test` (doit passer sans dépendre de Neon).

## Notes et bonnes pratiques
- Ne mélangez pas les paramètres spécifiques à psql dans une URL JDBC.
- Gardez `ddl-auto=update` seulement pour le développement; préférez les migrations (Flyway/Liquibase) en production.
- Conservez les identifiants BD hors du code source (variables d’environnement, Vault, etc.).
