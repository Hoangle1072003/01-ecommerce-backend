package net.javaguides.product_service.shema.response;

import lombok.Data;
import lombok.Value;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

/**
 * File: ResProductVarientDto.java
 * Author: Le Van Hoang
 * Date: 18/01/2025
 * Time: 21:35
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
