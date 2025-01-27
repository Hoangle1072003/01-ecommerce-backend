package net.javaguides.order_service.services;

import net.javaguides.order_service.shemas.request.ReqCreateOrderDto;
import net.javaguides.order_service.shemas.response.ResCreateOrderDto;
import net.javaguides.order_service.shemas.response.ResOrderByIdDto;

/**
 * File: IOrderService.java
 * Author: Le Van Hoang
 * Date: 25/01/2025
 * Time: 10:15
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

public interface IOrderService {
    ResCreateOrderDto createOrder(ReqCreateOrderDto reqCreateOrderDto) throws Exception;

    ResOrderByIdDto getOrderById(String id);
}
