package org.example.ecommercemonolithique;

import org.example.ecommercemonolithique.model.Product;
import org.example.ecommercemonolithique.repositories.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ECommerceMonolithiqueApplication {

    public static void main(String[] args) {
        SpringApplication.run(ECommerceMonolithiqueApplication.class, args);
    }
    @Bean
    CommandLineRunner Start(ProductRepository productRepository){
        return args ->{
            productRepository.save
                    (Product.builder().
                            name("Product 1").quantity(10).price(100.0)
                            .build());
            productRepository.save(Product.builder().name("Product 2").quantity(30).price(300.0).build());
        };
    }

}
