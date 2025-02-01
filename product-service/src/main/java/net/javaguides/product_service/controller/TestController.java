package net.javaguides.product_service.controller;

import net.javaguides.product_service.shema.response.RestResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * File: TestController.java
 * Author: Le Van Hoang
 * Date: 01/02/2025
 * Time: 02:33
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@RestController
@RequestMapping("/api")
public class TestController {

    @GetMapping("/test")
    public ResponseEntity<RestResponse<String>> testApi() {
        RestResponse<String> response = new RestResponse<>();
        response.setStatusCode(200);
        response.setMessage("Success");
        response.setData("Hello, World!");

        return ResponseEntity.ok(response);
    }
}