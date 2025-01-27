package net.javaguides.payment_service.controllers;

import net.javaguides.event.dto.PaymentEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * File: HomeController.java
 * Author: Le Van Hoang
 * Date: 25/01/2025
 * Time: 14:41
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@RestController
@RequestMapping("/api/v1")
public class HomeController {
    @RequestMapping("/home")
    public String home() {
        return "Welcome to payment service";
    }
}
