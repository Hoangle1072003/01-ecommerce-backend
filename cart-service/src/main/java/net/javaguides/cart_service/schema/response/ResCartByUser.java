package net.javaguides.cart_service.schema.response;

import lombok.Data;
import net.javaguides.cart_service.utils.constant.CartStatusEnum;

import java.util.UUID;

/**
 * File: ResCartByUser.java
 * Author: Le Van Hoang
 * Date: 31/01/2025
 * Time: 15:42
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */
@Data
public class ResCartByUser {
    private String id;
    private UUID userId;
    private CartStatusEnum status;
    private double total;
}
