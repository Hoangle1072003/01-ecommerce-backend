package net.javaguides.chat_service.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.javaguides.chat_service.dtos.response.RestResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.AccessDeniedException;

/**
 * File: CustomAccessDeniedHandler.java
 * Author: Le Van Hoang
 * Date: 08/02/2025
 * Time: 19:49
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {


    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, org.springframework.security.access.AccessDeniedException accessDeniedException) throws IOException, ServletException {
        RestResponse<String> restResponse = new RestResponse<>();
        restResponse.setStatusCode(403);
        restResponse.setError("Forbidden");
        restResponse.setMessage("You do not have permission to access this resource.");

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json");

        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(restResponse));
    }
}