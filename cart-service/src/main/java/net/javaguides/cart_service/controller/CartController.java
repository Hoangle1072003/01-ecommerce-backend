package net.javaguides.cart_service.controller;

import lombok.RequiredArgsConstructor;
import net.javaguides.cart_service.service.ICartService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
