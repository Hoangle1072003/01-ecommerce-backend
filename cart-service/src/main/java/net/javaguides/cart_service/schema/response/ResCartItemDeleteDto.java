package net.javaguides.cart_service.schema.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * File: ResCartItemDelete.java
 * Author: Le Van Hoang
 * Date: 24/01/2025
 * Time: 13:50
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResCartItemDeleteDto {
    private UUID userId;
    private String variantId;
}
