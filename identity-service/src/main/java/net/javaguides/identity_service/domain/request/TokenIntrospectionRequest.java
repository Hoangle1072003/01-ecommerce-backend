package net.javaguides.identity_service.domain.request;

/**
 * File: TokenIntrospectionRequest.java
 * Author: Le Van Hoang
 * Date: 1/3/2025 (03/01/2025)
 * Time: 6:13 PM
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

public class TokenIntrospectionRequest {
    private String token;

    public TokenIntrospectionRequest(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
