package net.javaguides.cart_service.schema.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * File: ResGetCartItemDto.java
 * Author: Le Van Hoang
 * Date: 26/01/2025
 * Time: 14:54
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResGetCartItemDto {
    private String id;
    private String cartId;
    private String productId;
    private String variantId;
    private int quantity;
    private Double price;
}
