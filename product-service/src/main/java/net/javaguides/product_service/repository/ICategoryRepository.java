package net.javaguides.product_service.repository;

import net.javaguides.product_service.shema.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * File: null.java
 * Author: Le Van Hoang
 * Date: 1/11/2025 (11/01/2025)
 * Time: 12:28 AM
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Repository
public interface ICategoryRepository extends MongoRepository<Category, String> {
}
