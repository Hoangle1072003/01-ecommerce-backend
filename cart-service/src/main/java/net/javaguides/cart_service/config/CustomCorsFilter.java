//package net.javaguides.cart_service.config;
//
//import jakarta.servlet.*;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.stereotype.Component;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//import org.springframework.web.filter.CorsFilter;
//
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.Iterator;
//import java.util.Spliterators;
//import java.util.stream.Stream;
//import java.util.stream.StreamSupport;
//
//
/// **
// * File: CustomCorsFilter.java
// * Author: Le Van Hoang
// * Date: 06/02/2025
// * Time: 10:22
// * Version: 1.0
// * <p>
// * Copyright © 2025 Le Van Hoang. All rights reserved.
// */
//
//@Component
//public class CustomCorsFilter implements Filter {
//
//
//    @Override
//    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
////        HttpServletResponse response = (HttpServletResponse) res;
////        HttpServletRequest request = (HttpServletRequest) req;
////        if (iteratorToFiniteStream(request.getHeaderNames().asIterator()).count() < 100) {
////            iteratorToFiniteStream(request.getHeaderNames().asIterator())
////                    .filter(h -> h.equalsIgnoreCase("access-control-request-method"))
////                    .findFirst().ifPresent(h -> response.setHeader("Access-Control-Allow-Methods", request.getHeader(h)));
////        }
////        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
////        response.setHeader("Access-Control-Allow-Headers", "content-type");
////        response.setHeader("Access-Control-Max-Age", "3600");
////        response.setHeader("Allow", "GET, HEAD, POST, PUT, DELETE, OPTIONS, PATCH");
////        chain.doFilter(req, res);
//    }
////
////    static <T> Stream<T> iteratorToFiniteStream(final Iterator<T> iterator) {
////        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, 0), false);
////    }
////
////    @Override
////    public void init(FilterConfig filterConfig) {
////    }
////
////    @Override
////    public void destroy() {
////    }
//}
