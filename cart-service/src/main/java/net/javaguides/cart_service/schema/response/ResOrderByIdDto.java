package net.javaguides.cart_service.schema.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.javaguides.cart_service.utils.constant.PaymentMethod;
import net.javaguides.cart_service.utils.constant.PaymentStatus;

/**
 * File: ResOrderByIdDto.java
 * Author: Le Van Hoang
 * Date: 06/02/2025
 * Time: 20:20
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
