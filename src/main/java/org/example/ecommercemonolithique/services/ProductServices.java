package org.example.ecommercemonolithique.services;

import org.example.ecommercemonolithique.model.Product;
import org.example.ecommercemonolithique.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProductServices {
    private final ProductRepository productRepository; // 'final' est une bonne pratique avec l'injection par
                                                       // constructeur

    public ProductServices(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Create
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    // Read all
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Read one
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product not found with id: " + id));
    }

    // Update
    public Product updateProduct(Long id, Product updated) {
        Product existing = getProductById(id);
        existing.setName(updated.getName());
        existing.setQuantity(updated.getQuantity());
        existing.setPrice(updated.getPrice());
        return productRepository.save(existing);
    }

    // Delete
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new NoSuchElementException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    // calcul Promo
    public double calculatePromo(Product product) {
        return product.getPrice() - (product.getPrice() * 40 / 100);
    }
}