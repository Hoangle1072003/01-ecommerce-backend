package net.javaguides.api_gateway.config;

/**
 * File: TokenIntrospectionResponse.java
 * Author: Le Van Hoang
 * Date: 1/3/2025 (03/01/2025)
 * Time: 5:54 PM
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

public class TokenIntrospectionResponse {
    private int statusCode;
    private String error;
    private String message;
    private Boolean data;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getData() {
        return data;
    }

    public void setData(Boolean data) {
        this.data = data;
    }
}

