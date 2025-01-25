package net.javaguides.order_service.shemas.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.javaguides.order_service.utils.constants.PaymentMethod;
import net.javaguides.order_service.utils.constants.PaymentStatus;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * File: ResCreateOrderDto.java
 * Author: Le Van Hoang
 * Date: 25/01/2025
 * Time: 10:18
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResCreateOrderDto {
    private String id;
    private String cartId;
    private String userId;
    private String shipping;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private double totalAmount;
}
