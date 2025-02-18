//package net.javaguides.notification_service.config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//import org.springframework.web.filter.CorsFilter;
//
//import java.util.List;
//
/// **
// * File: CustomCorsFilter.java
// * Author: Le Van Hoang
// * Date: 06/02/2025
// * Time: 15:25
// * Version: 1.0
// * <p>
// * Copyright © 2025 Le Van Hoang. All rights reserved.
// */
//
//@Configuration
//public class CustomCorsFilter {
//
//    @Value(("${cors-gateway.host}"))
//    private String host;
//
//    @Bean
//    public CorsFilter corsFilter() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//
////        CorsConfiguration config = new CorsConfiguration();
////        config.setAllowCredentials(true);
////        config.setAllowedOrigins(java.util.Collections.singletonList(host));
////        config.setAllowedHeaders(java.util.Collections.singletonList("*"));
////        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
////        config.setAllowedHeaders(List.of("Content-Type", "Authorization"));
////        config.setAllowCredentials(true);
////        config.setMaxAge(3600L);
////        source.registerCorsConfiguration("/**", config);
//
//        return new CorsFilter(source);
//    }
//}
