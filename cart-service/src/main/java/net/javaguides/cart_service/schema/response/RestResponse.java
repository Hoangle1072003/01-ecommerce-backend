package net.javaguides.cart_service.schema.response;

import lombok.Data;

/**
 * File: RestResponse.java
 * Author: Le Van Hoang
 * Date: 12/01/2025
 * Time: 00:44
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
