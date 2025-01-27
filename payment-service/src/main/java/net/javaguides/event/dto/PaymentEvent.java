package net.javaguides.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.javaguides.payment_service.utils.constant.PaymentMethod;
import net.javaguides.payment_service.utils.constant.PaymentStatus;

/**
 * File: PaymentEvent.java
 * Author: Le Van Hoang
 * Date: 25/01/2025
 * Time: 17:48
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentEvent {
    private String orderId;
    private String userId;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private Double totalAmount;
}