package net.javaguides.order_service.shemas.request;

import lombok.Data;

/**
 * File: ReqUpdateOrderDto.java
 * Author: Le Van Hoang
 * Date: 06/02/2025
 * Time: 20:52
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Data
public class ReqUpdateOrderDto {
    private String id;
    private String total_amount;
}
