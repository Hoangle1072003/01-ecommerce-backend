package net.javaguides.chat_service.dtos.response;

import lombok.Data;

/**
 * File: RestResponse.java
 * Author: Le Van Hoang
 * Date: 08/02/2025
 * Time: 19:48
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Data
public class RestResponse<T> {
    private int statusCode;
    private String error;
    private Object message;
    private T data;

}