package net.javaguides.product_service.service;

import net.javaguides.product_service.shema.Category;

import java.util.List;

/**
 * File: ICategoryService.java
 * Author: Le Van Hoang
 * Date: 1/11/2025 (11/01/2025)
 * Time: 12:29 AM
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */


public interface ICategoryService {
    Category save(Category category);

    Category findById(String id);

    List<Category> findAll();
}
