package net.javaguides.product_service.service;

import net.javaguides.product_service.shema.Product;
import net.javaguides.product_service.shema.response.ResProductDetailsDto;
import net.javaguides.product_service.shema.response.ResProductPage;

import java.util.List;
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
    List<Product> getProductsByCategory(String categoryID);
    ResProductPage getAllProductWithPageAndSorting(Integer pageNumber, Integer pageSize,
                                                   String sortBy, String dir);
    ResProductPage productByPrice(Integer pageNumber, Integer pageSize, String sortBy, String dir, Double minPrice, Double maxPrice);
    Product create(ResProductDetailsDto request);
    void deleteProduct(String id);
    ResProductPage searchProducts(String keyword, Double price, Integer pageNumber, Integer pageSize);
}
