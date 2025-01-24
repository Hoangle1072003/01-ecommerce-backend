package net.javaguides.cart_service.schema.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

/**
 * File: ReqCartItemDto.java
 * Author: Le Van Hoang
 * Date: 21/01/2025
 * Time: 15:51
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResCartItemDto {
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
