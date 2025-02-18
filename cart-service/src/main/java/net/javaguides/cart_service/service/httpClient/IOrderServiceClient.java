package net.javaguides.cart_service.service.httpClient;

import net.javaguides.cart_service.schema.request.ReqUpdateOrderDto;
import net.javaguides.cart_service.schema.response.ResOrderByIdDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * File: OrderServiceClient.java
 * Author: Le Van Hoang
 * Date: 06/02/2025
 * Time: 20:20
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */
@FeignClient(name = "ORDER-SERVICE")
public interface IOrderServiceClient {
    @GetMapping("/api/v1/orders/get-order-by-cart-id/{id}")
    ResOrderByIdDto getOrderByCartId(@PathVariable String id) throws Exception;

    @PutMapping("/api/v1/orders/update-order")
    ResOrderByIdDto updateOrder(@RequestBody ReqUpdateOrderDto reqUpdateOrderDto) throws Exception;

    @PutMapping("/api/v1/orders/update-order-status/{id}")
    ResOrderByIdDto updateOrderStatus(@PathVariable String id) throws Exception;
}
