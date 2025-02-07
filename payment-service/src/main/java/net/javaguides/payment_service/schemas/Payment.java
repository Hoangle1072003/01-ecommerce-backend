package net.javaguides.payment_service.schemas;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.javaguides.payment_service.utils.constant.PaymentMethod;
import net.javaguides.payment_service.utils.constant.PaymentStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * File: Payment.java
 * Author: Le Van Hoang
 * Date: 25/01/2025
 * Time: 14:44
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Document(collection = "payments")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Payment extends AbstractMappedEntity {
    @Id
    private String id;
    @Field("order_id")
    private String orderId;
    @Field("user_id")
    private String userId;
    @Field("payment_method")
    private PaymentMethod paymentMethod;
    @Field("payment_status")
    private PaymentStatus paymentStatus;
    private String paymentUrl;
    @Field("vnp_TxnRef")
    private String vnpTxnRef;
    private String transactionNo;
    @Field("total_amount")
    private Double totalAmount;
}
