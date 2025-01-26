package net.javaguides.product_service.service.impl;

import lombok.RequiredArgsConstructor;
import net.javaguides.product_service.repository.IProductRepository;
import net.javaguides.product_service.service.IProductService;
import net.javaguides.product_service.shema.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * File: ProductServiceImpl.java
 * Author: Le Van Hoang
 * Date: 1/10/2025 (10/01/2025)
 * Time: 4:18 AM
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements IProductService {
    private final IProductRepository productRepository;

    @Override
    public Optional<Product> findById(String id) {
        return productRepository.findById(id);
    }

    @Override
    public Product findProductVarientById(String id) {
        return productRepository.findProductByVarientId(id);
    }

    @Override
    @Transactional
    public void updateProductStock(String productId, String variantId, int quantity) {
        Product product = productRepository.findById(productId).orElse(null);
        if (product != null) {
            Product.Product_varient variant = product.getVarients().stream()
                    .filter(v -> v.getId().equals(variantId))
                    .findFirst()
                    .orElse(null);

            if (variant != null) {
                long updatedStock = variant.getStock() - quantity;
                if (updatedStock >= 0) {
                    variant.setStock(updatedStock);
                    productRepository.save(product);
                    System.out.println("Updated stock for product: " + productId + " with variant: " + variantId);
                } else {
                    System.out.println("Not enough stock for product: " + productId + " with variant: " + variantId);
                }
            } else {
                System.out.println("Variant not found for product: " + productId + " with variantId: " + variantId);
            }
        } else {
            System.out.println("Product not found: " + productId);
        }
    }

}
