package net.javaguides.identity_service.domain.response;

import lombok.Data;

/**
 * File: RestResponse.java
 * Author: Le Van Hoang
 * Date: 12/27/2024 (27/12/2024)
 * Time: 2:34 PM
 * Version: 1.0
 * <p>
 * Copyright © 2024 Le Van Hoang. All rights reserved.
 */
@Data
public class RestResponse<T> {
    private int statusCode;
    private String error;
    private Object message;
    private T data;

}
