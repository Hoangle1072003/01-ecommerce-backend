package net.javaguides.payment_service.schemas.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.javaguides.payment_service.utils.constant.PaymentMethod;
import net.javaguides.payment_service.utils.constant.PaymentStatus;

/**
 * File: ResOrderByIdDto.java
 * Author: Le Van Hoang
 * Date: 01/02/2025
 * Time: 00:24
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResOrderByIdDto {
    private String id;
    private String cartId;
    private String userId;
    private String shipping;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private Double totalAmount;
}

