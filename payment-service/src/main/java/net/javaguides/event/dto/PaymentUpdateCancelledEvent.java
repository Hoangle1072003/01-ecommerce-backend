package net.javaguides.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * File: PaymentUpdateCancelledEvent.java
 * Author: Le Van Hoang
 * Date: 08/02/2025
 * Time: 00:06
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentUpdateCancelledEvent {
    private String orderId;
}
