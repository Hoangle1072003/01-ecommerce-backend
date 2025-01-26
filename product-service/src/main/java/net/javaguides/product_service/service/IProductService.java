package net.javaguides.product_service.service;

import net.javaguides.product_service.shema.Product;

import java.util.Optional;

/**
 * File: null.java
 * Author: Le Van Hoang
 * Date: 1/10/2025 (10/01/2025)
 * Time: 4:17 AM
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

public interface IProductService {
    Optional<Product> findById(String id);

    Product findProductVarientById(String id);

    void updateProductStock(String productId, String variantId, int quantity);
}
