package net.javaguides.product_service.controller;

import lombok.RequiredArgsConstructor;
import net.javaguides.product_service.mapper.ICategoryMapper;
import net.javaguides.product_service.service.ICategoryService;
import net.javaguides.product_service.shema.Category;
import net.javaguides.product_service.shema.request.ReqCategoryDto;
import net.javaguides.product_service.shema.response.ResCategoryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * File: CategoryController.java
 * Author: Le Van Hoang
 * Date: 1/11/2025 (11/01/2025)
 * Time: 12:27 AM
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
public class CategoryController {
    private final ICategoryService categoryService;
    private final ICategoryMapper categoryMapper;

    @PostMapping()
    public ResponseEntity<ResCategoryDto> saveCategory(@RequestBody ReqCategoryDto categoryDto) {
        return ResponseEntity.ok(categoryMapper.toDto(categoryService.save(categoryMapper.toEntity(categoryDto))));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResCategoryDto> getCategoryById(@PathVariable String id) {
        return ResponseEntity.ok(categoryMapper.toDto(categoryService.findById(id)));
    }

    @GetMapping()
    public ResponseEntity<List<ResCategoryDto>> getCategories() {
        List<Category> categories = categoryService.findAll();
        return ResponseEntity.ok(categories.stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList()));
    }

}
