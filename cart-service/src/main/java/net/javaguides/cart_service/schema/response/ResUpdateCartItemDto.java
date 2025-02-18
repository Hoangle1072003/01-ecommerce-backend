package net.javaguides.cart_service.schema.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * File: ResUpdateCartItemDto.java
 * Author: Le Van Hoang
 * Date: 04/02/2025
 * Time: 20:24
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResUpdateCartItemDto {
    private String message;
    private String productId;
    private String variantId;
    private int newQuantity;
    private double cartTotal;
}
