package net.javaguides.product_service.service.impl;

import lombok.RequiredArgsConstructor;
import net.javaguides.product_service.repository.IProductRepository;
import net.javaguides.product_service.service.IProductService;
import net.javaguides.product_service.shema.Product;
import org.springframework.stereotype.Service;

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
}
