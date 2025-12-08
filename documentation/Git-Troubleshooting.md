# Git: Pourquoi je ne vois que README sur GitHub ?

Problème courant: Vous avez initialisé le dépôt, ajouté seulement `README.md`, et poussé la branche `main`. Résultat: GitHub n'affiche que `README.md` parce que les autres fichiers n'ont jamais été ajoutés/committés.

## Symptômes
- GitHub montre uniquement `README.md` alors que vous avez des fichiers en local.
- Commandes effectuées (extrait):
  - `git init`
  - `echo "# E-Commerce-Monolithique" >> README.md`
  - `git add README.md`
  - `git commit -m "first commit"`
  - `git branch -M main`
  - `git remote add origin ...`
  - `git push -u origin main`

Seul `README.md` a été ajouté et committé. Les autres fichiers n'ont jamais été suivis par Git.

## Solution rapide: Ajouter, committer et pousser TOUS les fichiers
1. Vérifier l'état:
   - `git status`
2. Ajouter les fichiers non suivis:
   - `git add .`
3. Créer un commit:
   - `git commit -m "Add project sources (Spring Boot app)"`
4. Pousser vers GitHub:
   - `git push`

Rafraîchissez la page GitHub: vous devriez voir tout le projet.

## Si GitHub refuse le push (cas de figure)
- « non-fast-forward »: vous avez des commits distants que vous n'avez pas en local.
  - `git pull --rebase origin main`
  - Résolvez d'éventuels conflits, puis:
  - `git push`

- Mauvais remote ou URL:
  - `git remote -v` (vérifiez qu'il pointe vers `https://github.com/fedi365/E-Commerce-Monolithique.git`)
  - Pour corriger: `git remote set-url origin https://github.com/fedi365/E-Commerce-Monolithique.git`

- Mauvaise branche locale:
  - `git branch` (assurez-vous d'être sur `main`)
  - Pour changer: `git checkout main` (ou créer/renommer si besoin)

## Avertissement CRLF/LF
Message vu: `warning: LF will be replaced by CRLF ...` (Windows). Ce n'est pas bloquant. Pour normaliser les fins de ligne et réduire les avertissements, vous pouvez utiliser `.gitattributes`:

```
* text=auto
*.java text eol=lf
*.md text eol=lf
*.properties text eol=lf
```

## Ignorer les fichiers build/IDE (recommandé)
Nous avons ajouté un `.gitignore` adapté Java/Maven/IntelliJ. Si des fichiers indésirables sont déjà suivis, supprimez-les de l'index:

```
# Exemple: arrêter de suivre target/ et .idea
git rm -r --cached target .idea *.iml
git commit -m "chore(git): clean tracked build/IDE artifacts"
```

## OneDrive et chemins avec espaces
- Évitez de déplacer manuellement le dossier `.git`.
- Si OneDrive verrouille des fichiers, fermez les applications qui les utilisent et réessayez.

## Check-list finale
- [ ] `git status` propre (rien à committer)
- [ ] Tous les fichiers visibles sur GitHub
- [ ] `.gitignore` présent à la racine
- [ ] Optionnel: `.gitattributes` pour normaliser les fins de ligne

## Commandes complètes (copier-coller)
```
git status
git add .
git commit -m "Add project sources (Spring Boot app)"
git push
```
