package net.javaguides.order_service.controllers;

import lombok.RequiredArgsConstructor;
import net.javaguides.order_service.services.IOrderService;
import net.javaguides.order_service.shemas.request.ReqCreateOrderDto;
import net.javaguides.order_service.shemas.response.ResCreateOrderDto;
import net.javaguides.order_service.shemas.response.ResOrderByIdDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * File: OrderController.java
 * Author: Le Van Hoang
 * Date: 25/01/2025
 * Time: 10:14
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final IOrderService orderService;

    @PostMapping
    public ResponseEntity<ResCreateOrderDto> createOrder(@RequestBody ReqCreateOrderDto reqCreateOrderDto) throws Exception {
        return ResponseEntity.ok(orderService.createOrder(reqCreateOrderDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResOrderByIdDto> getOrder(@PathVariable String id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }
}
