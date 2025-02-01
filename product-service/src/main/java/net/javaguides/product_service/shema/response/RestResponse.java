package net.javaguides.product_service.shema.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * File: RestResponse.java
 * Author: Le Van Hoang
 * Date: 1/9/2025 (09/01/2025)
 * Time: 10:10 PM
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

