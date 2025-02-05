package net.javaguides.order_service.shemas.response;

import lombok.Data;
import net.javaguides.event.dto.CartItemClientEvent;
import net.javaguides.order_service.utils.constants.PaymentMethod;
import net.javaguides.order_service.utils.constants.PaymentStatus;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

/**
 * File: ResOrderByUserId.java
 * Author: Le Van Hoang
 * Date: 05/02/2025
 * Time: 14:33
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Data
public class ResOrderByUserIdDto {
    private String id;
    private String cartId;
    private String userId;
    private String shipping;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private String paymentId;
    private Double totalAmount;
    private Cart cart;
    private List<CartItemClientEvent> cartItems;
}
