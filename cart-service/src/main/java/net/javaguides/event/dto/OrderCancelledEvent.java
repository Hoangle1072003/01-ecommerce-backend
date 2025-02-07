package net.javaguides.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.javaguides.cart_service.utils.constant.PaymentStatus;

/**
 * File: OrderCancelledEvent.java
 * Author: Le Van Hoang
 * Date: 07/02/2025
 * Time: 15:59
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderCancelledEvent {
    private String cartId;
}