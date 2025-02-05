package net.javaguides.cart_service.controller;

import lombok.RequiredArgsConstructor;
import net.javaguides.cart_service.schema.Cart;
import net.javaguides.cart_service.schema.request.ReqCartDto;
import net.javaguides.cart_service.schema.request.ReqUpdateCart;
import net.javaguides.cart_service.schema.response.ResCartByUser;
import net.javaguides.cart_service.schema.response.ResCartUpdateDto;
import net.javaguides.cart_service.service.ICartService;
import net.javaguides.cart_service.utils.annotation.ApiMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * File: CartController.java
 * Author: Le Van Hoang
 * Date: 12/01/2025
 * Time: 00:46
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {
    private final ICartService cartService;

    @PostMapping()
    @ApiMessage("Create new cart")
    public ResponseEntity<Cart> createCart(@RequestBody ReqCartDto reqCartDto) {
        return ResponseEntity.ok(cartService.save(reqCartDto));
    }

    @GetMapping("/{id}")
    @ApiMessage("Get cart by id")
    public ResponseEntity<Cart> getCartById(@PathVariable String id) {
        return ResponseEntity.ok(cartService.findById(id));
    }

    @GetMapping("/user/{id}")
    @ApiMessage("Get cart by user id")
    public ResponseEntity<ResCartByUser> getCartByUserId(@PathVariable UUID id) {
        return ResponseEntity.ok(cartService.findByUserId(id));
    }

    @PutMapping()
    @ApiMessage("Update cart status")
    public ResponseEntity<ResCartUpdateDto> updateCartStatus(@RequestBody ReqUpdateCart reqCartUpdateDto) {
        return ResponseEntity.ok(cartService.updateCartStatus(reqCartUpdateDto.getId()));
    }


}
