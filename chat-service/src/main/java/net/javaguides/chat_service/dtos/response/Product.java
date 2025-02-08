package net.javaguides.chat_service.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

/**
 * File: Product.java
 * Author: Le Van Hoang
 * Date: 08/02/2025
 * Time: 21:12
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private String id;
    private String code;
    private String name;
    private String brand;
    private String description;
    private String ShortDescription;
    private Instant releaseDate;
    private double weightInGrams;
    private String categoryID;
    private List<Spec> specs;
    private List<Product_varient> varients;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Spec {
        private String k;
        private Object v;
        private String u;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Product_varient {
        private String id;
        private String name;
        private double price;
        private String image;
        private boolean isAvailable;
        private Long stock;
    }
}
