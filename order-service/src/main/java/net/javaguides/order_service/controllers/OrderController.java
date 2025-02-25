package net.javaguides.order_service.controllers;

import lombok.RequiredArgsConstructor;
import net.javaguides.order_service.services.IOrderService;
import net.javaguides.order_service.shemas.request.*;
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


    @GetMapping("/get-all-orders-by-user-id-client")
    @ApiMessage("Get all orders by user id client")
    public ResponseEntity<List<ResAllOrderByUserIdDto>> getAllOrdersByUserIdClient(@RequestParam String userId) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getAllOrdersByUserIdClient(userId));
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

    @GetMapping("/get-all-orders-waiting")
    @ApiMessage("Get all orders by user id")
    public ResponseEntity<ResResultPaginationDTO> getAllOrdersByUserIdWaiting(@RequestParam String userId,
                                                                              @RequestParam("current") String current,
                                                                              @RequestParam("pageSize") String pageSize,
                                                                              @RequestParam("sort") String sort) {
        int page = Integer.parseInt(current);
        int size = Integer.parseInt(pageSize);

        Pageable pageable = PageRequest.of(page - 1, size, sort.equals("asc") ? Sort.by("createdAt").ascending() : Sort.by("createdAt").descending());
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getAllOrdersByUserIdWating(userId, pageable));
    }

    @GetMapping("/get-all-orders-processing")
    @ApiMessage("Get all orders by user id")
    public ResponseEntity<ResResultPaginationDTO> getAllOrdersByUserIdProcessing(@RequestParam String userId,
                                                                                 @RequestParam("current") String current,
                                                                                 @RequestParam("pageSize") String pageSize,
                                                                                 @RequestParam("sort") String sort) {
        int page = Integer.parseInt(current);
        int size = Integer.parseInt(pageSize);

        Pageable pageable = PageRequest.of(page - 1, size, sort.equals("asc") ? Sort.by("createdAt").ascending() : Sort.by("createdAt").descending());
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getAllOrdersByUserIdProcessing(userId, pageable));
    }

    @GetMapping("/get-all-orders-shipping")
    @ApiMessage("Get all orders by user id")
    public ResponseEntity<ResResultPaginationDTO> getAllOrdersByUserIdShipping(@RequestParam String userId,
                                                                               @RequestParam("current") String current,
                                                                               @RequestParam("pageSize") String pageSize,
                                                                               @RequestParam("sort") String sort) {
        int page = Integer.parseInt(current);
        int size = Integer.parseInt(pageSize);

        Pageable pageable = PageRequest.of(page - 1, size, sort.equals("asc") ? Sort.by("createdAt").ascending() : Sort.by("createdAt").descending());
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getAllOrdersByUserIdShipping(userId, pageable));
    }

    @GetMapping("/get-all-orders-cancelled")
    @ApiMessage("Get all orders by user id")
    public ResponseEntity<ResResultPaginationDTO> getAllOrdersByUserIdCancelled(@RequestParam String userId,
                                                                                @RequestParam("current") String current,
                                                                                @RequestParam("pageSize") String pageSize,
                                                                                @RequestParam("sort") String sort) {
        int page = Integer.parseInt(current);
        int size = Integer.parseInt(pageSize);

        Pageable pageable = PageRequest.of(page - 1, size, sort.equals("asc") ? Sort.by("createdAt").ascending() : Sort.by("createdAt").descending());
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getAllOrdersByUserIdCancelled(userId, pageable));
    }

    @GetMapping("/get-all-orders-by-order-id/{id}")
    @ApiMessage("Get all orders by order id")
    public ResponseEntity<ResOrderByUserIdDto> getAllOrdersByOrderId(@PathVariable String id) {
        return ResponseEntity.ok(orderService.getOrdersByOrderId(id));
    }

    @GetMapping("/get-order-by-cart-id/{id}")
    @ApiMessage("Get order by cart id")
    public ResponseEntity<ResOrderByIdDto> getOrderByCartId(@PathVariable String id) throws Exception {
        return ResponseEntity.ok(orderService.getOrderByCartId(id));
    }

    @PutMapping("/update-order")
    @ApiMessage("Update order by id and total amount")
    public ResponseEntity<ResOrderByIdDto> updateOrder(@RequestBody ReqUpdateOrderDto reqUpdateOrderDto) throws Exception {
        return ResponseEntity.ok(orderService.updateOrder(reqUpdateOrderDto));
    }

    @PutMapping("/update-order-status/{id}")
    @ApiMessage("Update order status by id")
    public ResponseEntity<ResOrderByIdDto> updateOrderStatus(@PathVariable String id) throws Exception {
        return ResponseEntity.ok(orderService.updateOrderStatus(id));
    }

    @PutMapping("/cancel-order")
    @ApiMessage("Cancel order by id - status = pending")
    public ResponseEntity<ResOrderByIdDto> cancelOrder(@RequestBody ReqCancelOrderStatusPending reqCancelOrderStatusPending) throws Exception {
        return ResponseEntity.ok(orderService.cancelOrder(reqCancelOrderStatusPending));
    }

    @PutMapping("/update-order-status-cart-id")
    @ApiMessage("Update order status by cart id")
    public ResponseEntity<ResOrderByIdDto> updateOrderStatusByCartId(@RequestBody ReqUpdateStatusCartIdDto reqUpdateStatusCartIdDto) throws Exception {
        return ResponseEntity.ok(orderService.updateOrderStatusByCartId(reqUpdateStatusCartIdDto));
    }
}
