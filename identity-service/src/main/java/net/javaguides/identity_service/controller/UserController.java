package net.javaguides.identity_service.controller;

import lombok.RequiredArgsConstructor;
import net.javaguides.identity_service.entity.request.ReqUserDto;
import net.javaguides.identity_service.entity.response.ResUserDto;
import net.javaguides.identity_service.service.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * File: UserController.java
 * Author: Le Van Hoang
 * Date: 12/27/2024 (27/12/2024)
 * Time: 9:57 AM
 * Version: 1.0
 * <p>
 * Copyright © 2024 Le Van Hoang. All rights reserved.
 */

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final IUserService userService;

    @PostMapping()
    public ResponseEntity<ReqUserDto> createUser(@RequestBody ReqUserDto user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(user));
    }


    @GetMapping("/{id}")
    public ResponseEntity<ResUserDto> getUserById(@PathVariable UUID id) {
        ResUserDto user = userService.getUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }
}
