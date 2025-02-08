package net.javaguides.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * File: PaymentUpdateStatusRefundEvent.java
 * Author: Le Van Hoang
 * Date: 08/02/2025
 * Time: 15:14
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentUpdateStatusRefundEvent {
    private String orderId;
}

