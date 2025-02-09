package net.javaguides.identity_service.controller;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * File: AuthGoogleController.java
 * Author: Le Van Hoang
 * Date: 10/02/2025
 * Time: 01:13
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@RestController()
@RequestMapping("/auth/google")
public class AuthGoogleController {

    @GetMapping("/signin")
    public Map<String, Object> currentUser(OAuth2AuthenticationToken authentication) {
        return authentication.getPrincipal().getAttributes();
    }
}
