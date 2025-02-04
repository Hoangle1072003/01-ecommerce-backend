package net.javaguides.cart_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * File: HomeController.java
 * Author: Le Van Hoang
 * Date: 03/02/2025
 * Time: 14:54
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@RestController
@RequestMapping("/api/home")
public class HomeController {

    @GetMapping("/test")
    public String home() {
        return "Welcome to Cart Service API     ";
    }
}
