package net.javaguides.order_service.shemas.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.javaguides.order_service.utils.constants.PaymentMethod;

/**
 * File: ReqCreateOrderDto.java
 * Author: Le Van Hoang
 * Date: 25/01/2025
 * Time: 10:03
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReqCreateOrderDto {
    private String cartId;
    private String userId;
    private String shipping;
    private PaymentMethod paymentMethod;
    private String paymentId;
    private double totalAmount;
}
