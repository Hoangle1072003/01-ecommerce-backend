package net.javaguides.identity_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;
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

    @RequestMapping("/user")
    public Principal user(Principal user) {
        return user;
    }

    @PostMapping("verify")
    public ResponseEntity<Boolean> verify(@RequestParam String token) {
        String verifyTokenUrl = "https://www.googleapis.com/oauth2/v3/tokeninfo?access_token=" + token;

        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(verifyTokenUrl, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.ok(true);
            } else {
                return ResponseEntity.ok(false);
            }
        } catch (HttpClientErrorException e) {
            System.out.println("HTTP Error: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(false);
    }

}
