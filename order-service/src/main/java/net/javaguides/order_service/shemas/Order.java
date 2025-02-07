package net.javaguides.order_service.shemas;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.javaguides.order_service.utils.constants.PaymentMethod;
import net.javaguides.order_service.utils.constants.PaymentStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * File: Order.java
 * Author: Le Van Hoang
 * Date: 24/01/2025
 * Time: 15:38
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "orders")
public class Order extends AbstractMappedEntity {
    @Id
    private String id;
    private String cartId;
    private String userId;
    private String shipping;
    @Field("payment_method")
    private PaymentMethod paymentMethod;
    @Field("payment_status")
    private PaymentStatus paymentStatus;
    private String paymentId;
    @Field("total_amount")
    private Double totalAmount;
    @Field("reason")
    private String reason;
}
