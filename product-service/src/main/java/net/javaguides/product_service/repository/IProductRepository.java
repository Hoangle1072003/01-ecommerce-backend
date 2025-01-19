package net.javaguides.product_service.repository;

import net.javaguides.product_service.shema.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * File: null.java
 * Author: Le Van Hoang
 * Date: 1/9/2025 (09/01/2025)
 * Time: 2:05 PM
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */


@Repository
public interface IProductRepository extends MongoRepository<Product, String> {
    @Query("{ 'varients.id' : ?0 }")
    Product findProductByVarientId(String varientId);
}
