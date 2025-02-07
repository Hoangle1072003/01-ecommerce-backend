package net.javaguides.order_service.shemas.request;

import lombok.Data;

import java.util.UUID;

/**
 * File: ReqCancelOrderStatusPending.java
 * Author: Le Van Hoang
 * Date: 07/02/2025
 * Time: 15:17
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Data
public class ReqCancelOrderStatusPending {
    private String orderId;
    private UUID userId;
    private String reason;
}
