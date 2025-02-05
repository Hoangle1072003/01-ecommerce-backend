package net.javaguides.order_service.controllers;

import lombok.RequiredArgsConstructor;
import net.javaguides.order_service.services.IOrderService;
import net.javaguides.order_service.shemas.request.ReqCreateOrderDto;
import net.javaguides.order_service.shemas.response.*;
import net.javaguides.order_service.utils.annotation.ApiMessage;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/methods")
    @ApiMessage("Get all payment method")
    public ResponseEntity<List<ResPaymentMethod>> getPaymentMethod() {
        return ResponseEntity.ok(orderService.getAllPaymentMethod());
    }

    @GetMapping("/get-all-orders")
    @ApiMessage("Get all orders by user id")
    public ResponseEntity<ResResultPaginationDTO> getAllOrdersByUserId(@RequestParam String userId,
                                                                       @RequestParam("current") String current,
                                                                       @RequestParam("pageSize") String pageSize,
                                                                       @RequestParam("sort") String sort) {
        int page = Integer.parseInt(current);
        int size = Integer.parseInt(pageSize);

        Pageable pageable = PageRequest.of(page - 1, size, sort.equals("asc") ? Sort.by("createdAt").ascending() : Sort.by("createdAt").descending());
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getAllOrdersByUserId(userId, pageable));
    }

    @GetMapping("/get-all-orders-by-order-id/{id}")
    @ApiMessage("Get all orders by order id")
    public ResponseEntity<ResOrderByUserIdDto> getAllOrdersByOrderId(@PathVariable String id) {
        return ResponseEntity.ok(orderService.getOrdersByOrderId(id));
    }


}
