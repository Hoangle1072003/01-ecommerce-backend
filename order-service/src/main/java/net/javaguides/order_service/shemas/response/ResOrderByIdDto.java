package net.javaguides.order_service.shemas.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.javaguides.order_service.utils.constants.PaymentMethod;
import net.javaguides.order_service.utils.constants.PaymentStatus;


/**
 * File: ResOrderByIdDto.java
 * Author: Le Van Hoang
 * Date: 27/01/2025
 * Time: 21:44
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
