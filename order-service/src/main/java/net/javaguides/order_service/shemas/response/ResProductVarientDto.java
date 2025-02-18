package net.javaguides.order_service.shemas.response;

import lombok.Data;

import java.util.List;

/**
 * File: ResProductVarientDto.java
 * Author: Le Van Hoang
 * Date: 05/02/2025
 * Time: 15:39
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Data
public class ResProductVarientDto {
    String id;
    String name;
    List<VarientDto> varients;

    @Data
    public static class VarientDto {
        private String id;
        private String name;
        private double price;
        private String image;
        private Long stock;
        private boolean available;
    }
}

