//package net.javaguides.cart_service.config;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import net.javaguides.cart_service.schema.response.RestResponse;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
//import org.springframework.security.web.AuthenticationEntryPoint;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.util.Optional;
//
/// **
// * File: CustomAuthenticationEntryPoint.java
// * Author: Le Van Hoang
// * Date: 06/02/2025
// * Time: 10:47
// * Version: 1.0
// * <p>
// * Copyright © 2025 Le Van Hoang. All rights reserved.
// */
//
//@Component
//public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
//
//    private final AuthenticationEntryPoint delegate = new BearerTokenAuthenticationEntryPoint();
//
//    private final ObjectMapper mapper;
//
//    public CustomAuthenticationEntryPoint(ObjectMapper mapper) {
//        this.mapper = mapper;
//    }
//
//    @Override
//    public void commence(HttpServletRequest request, HttpServletResponse response, org.springframework.security.core.AuthenticationException authException) throws IOException, ServletException {
//        this.delegate.commence(request, response, authException);
//        response.setContentType("application/json;charset=UTF-8");
//
//        RestResponse<Object> res = new RestResponse<Object>();
//        res.setStatusCode(HttpStatus.UNAUTHORIZED.value());
//
//        String errorMessage = Optional.ofNullable(authException.getCause()) // NULL
//                .map(Throwable::getMessage)
//                .orElse(authException.getMessage());
//        res.setError(errorMessage);
//
//        res.setMessage("Token is invalid or expired or not present in the request header");
//
//        mapper.writeValue(response.getWriter(), res);
//    }
//}
//
