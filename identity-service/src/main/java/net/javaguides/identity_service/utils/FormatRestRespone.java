package net.javaguides.identity_service.utils;

import jakarta.servlet.http.HttpServletResponse;
import net.javaguides.identity_service.entity.response.RestResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * File: FormatRestRespone.java
 * Author: Le Van Hoang
 * Date: 12/27/2024 (27/12/2024)
 * Time: 2:40 PM
 * Version: 1.0
 * <p>
 * Copyright © 2024 Le Van Hoang. All rights reserved.
 */
@ControllerAdvice
public class FormatRestRespone implements ResponseBodyAdvice {
    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        HttpServletResponse servletResponse = ((ServletServerHttpResponse) response).getServletResponse();
        int status = servletResponse.getStatus();
        RestResponse<Object> restResponse = new RestResponse<>();
        restResponse.setStatusCode(status);
        if (body instanceof String) {
            return body;
        }
        if (status >= 400) {
            return body;
        } else {
            restResponse.setData(body);
            restResponse.setMessage("CALL API SUCCESS");
        }
        return restResponse;
    }

}
