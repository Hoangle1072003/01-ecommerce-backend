package net.javaguides.notification_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

/**
 * File: ResCartItemByIdDto.java
 * Author: Le Van Hoang
 * Date: 27/01/2025
 * Time: 22:17
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResCartItemByIdDto {
    private UUID userId;
    private List<ProductDto> products;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProductDto {
        private String productId;
        private String productName;
        private String brandName;
        private List<VarientDto> varients;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class VarientDto {
        private String variantId;
        private String variantName;
        private String variant_img;
        private double variantPrice;
        private int quantity;
        private double total;
    }
}
