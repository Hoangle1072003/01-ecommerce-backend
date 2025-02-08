package net.javaguides.order_service.services;

import net.javaguides.order_service.shemas.request.ReqCancelOrderStatusPending;
import net.javaguides.order_service.shemas.request.ReqCreateOrderDto;
import net.javaguides.order_service.shemas.request.ReqUpdateOrderDto;
import net.javaguides.order_service.shemas.request.ReqUpdateStatusCartIdDto;
import net.javaguides.order_service.shemas.response.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

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

    List<ResPaymentMethod> getAllPaymentMethod();

    ResResultPaginationDTO getAllOrdersByUserId(String userId, Pageable pageable);

    ResResultPaginationDTO getAllOrdersByUserIdWating(String userId, Pageable pageable);

    ResResultPaginationDTO getAllOrdersByUserIdProcessing(String userId, Pageable pageable);

    ResResultPaginationDTO getAllOrdersByUserIdShipping(String userId, Pageable pageable);

    ResResultPaginationDTO getAllOrdersByUserIdCancelled(String userId, Pageable pageable);

    ResOrderByUserIdDto getOrdersByOrderId(String id);

    ResOrderByIdDto getOrderByCartId(String id) throws Exception;

    ResOrderByIdDto updateOrder(ReqUpdateOrderDto reqUpdateOrderDto) throws Exception;

    ResOrderByIdDto updateOrderStatus(String id) throws Exception;

    ResOrderByIdDto cancelOrder(ReqCancelOrderStatusPending reqCancelOrderStatusPending) throws Exception;

    ResOrderByIdDto updateOrderStatusByCartId(ReqUpdateStatusCartIdDto reqUpdateStatusCartIdDto) throws Exception;

    ResOrderByIdDto updateOrderStatusRefund(String id) throws Exception;
}
