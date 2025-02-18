package net.javaguides.cart_service.schema.request;

import lombok.Data;

import java.util.UUID;

/**
 * File: ReqCartItemUpdateDto.java
 * Author: Le Van Hoang
 * Date: 04/02/2025
 * Time: 19:57
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Data
public class ReqCartItemUpdateDto {
    private UUID userId;
    private String productId;
    private String variantId;
    private int quantity;
}