package net.javaguides.order_service.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * File: HomeController.java
 * Author: Le Van Hoang
 * Date: 24/01/2025
 * Time: 15:30
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@RestController
@RequestMapping("/api/v1/home")
public class HomeController {
    @GetMapping
    public String home() {
        return "Welcome to Order Service";
    }
}
