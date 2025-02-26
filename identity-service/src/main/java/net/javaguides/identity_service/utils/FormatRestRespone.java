package net.javaguides.identity_service.utils;

import jakarta.servlet.http.HttpServletResponse;
import net.javaguides.identity_service.domain.response.RestResponse;
import net.javaguides.identity_service.utils.annotation.ApiMessage;
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
        response.getHeaders().add("X-Author", "Industrial University of Ho Chi Minh City - Developed by Le Van Hoang & Vo Cong Tuan Anh");
        if (body instanceof org.springframework.hateoas.RepresentationModel) {
            return body;
        }
        int status = servletResponse.getStatus();

        RestResponse<Object> restResponse = new RestResponse<>();
        restResponse.setStatusCode(status);
        if (body instanceof String) {
            return body;
        }
        String path = request.getURI().getPath();
        if (path.startsWith("/v3/api-docs") || path.startsWith("/swagger-ui")) {
            return body;
        }
        if (status >= 400) {
            return body;
        } else {
            restResponse.setData(body);
            restResponse.setAuthor("Industrial University of Ho Chi Minh City - Developed by Le Van Hoang & Vo Cong Tuan Anh");
            ApiMessage apiMessage = returnType.getMethodAnnotation(ApiMessage.class);
            restResponse.setMessage(apiMessage != null ? apiMessage.value() : "CALL API SUCCESS");
        }
        return restResponse;
    }

}
