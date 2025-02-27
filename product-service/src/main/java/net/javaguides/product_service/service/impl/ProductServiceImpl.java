package net.javaguides.product_service.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import net.javaguides.product_service.mapper.IProductMapper;
import net.javaguides.product_service.repository.IProductRepository;
import net.javaguides.product_service.service.IProductService;
import net.javaguides.product_service.shema.Product;
import net.javaguides.product_service.shema.response.ResProductDto;
import net.javaguides.product_service.shema.response.ResProductRecentlyDto;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private final StringRedisTemplate redisTemplate;
    private static final int MAX_RECENT_PRODUCTS = 10;
    private static final long TTL_IN_SECONDS = 7 * 24 * 60 * 60;
    private final IProductMapper productMapper;
    private final ObjectMapper objectMapper;

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

    public void saveRecentlyViewedProduct(String userId, Optional<Product> product) {
        String key = "recently_viewed:" + userId;
        ListOperations<String, String> listOps = redisTemplate.opsForList();

        ResProductDto productDto = productMapper.toDto(product.get());

        try {
            String productJson = new ObjectMapper().writeValueAsString(productDto);

            redisTemplate.opsForList().remove(key, 1, productJson);

            listOps.leftPush(key, productJson);

            redisTemplate.opsForList().trim(key, 0, MAX_RECENT_PRODUCTS - 1);

            redisTemplate.expire(key, Duration.ofSeconds(TTL_IN_SECONDS));

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<ResProductRecentlyDto> getRecentlyViewedProducts(String userId) {
        String key = "recently_viewed:" + userId;
        List<String> productJsons = redisTemplate.opsForList().range(key, 0, MAX_RECENT_PRODUCTS - 1);

        if (productJsons == null || productJsons.isEmpty()) {
            return List.of();
        }

        return productJsons.stream()
                .map(json -> {
                    try {
                        Product product = objectMapper.readValue(json, Product.class);
                        return productMapper.toResProductRecentlyDto(product);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .filter(product -> product != null)
                .collect(Collectors.toList());
    }
}
