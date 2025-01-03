package net.javaguides.api_gateway.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


import java.util.Arrays;
import java.util.List;

/**
 * File: AuthenticationFilter.java
 * Author: Le Van Hoang
 * Date: 1/2/2025 (02/01/2025)
 * Time: 5:33 PM
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class CustomGlobalFilter implements GlobalFilter, Ordered {

    private final WebClient.Builder webClientBuilder;
    private final String[] publicEndpoints = {
            "/identity-service/.*"
    };


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        log.info("Request path: {}", path);

        if (isPublicEndpoint(exchange.getRequest())) {
            return chain.filter(exchange);
        }

        List<String> authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);
        if (CollectionUtils.isEmpty(authHeader)) {
            return unauthorized(exchange.getResponse());
        }

        String token = authHeader.get(0);
        log.info("Token: {}", token);
        return checkToken(token)
                .flatMap(isValid -> {
                    if (!isValid) {
                        return unauthorized(exchange.getResponse());
                    }
                    return chain.filter(exchange);
                });
    }

    private Mono<Boolean> checkToken(String token) {
        log.info("Sending token to introspect: {}", token);
        return webClientBuilder.build()
                .post()
                .uri("http://localhost:8082/api/v1/auth/introspect")
                .header(HttpHeaders.AUTHORIZATION, token)
                .retrieve()
                .bodyToMono(TokenIntrospectionResponse.class)
                .map(response -> response.getData())
                .onErrorResume(e -> {
                    log.error("Error during token validation: {}", e.getMessage());
                    return Mono.just(false);
                })
                .doOnNext(isValid -> log.info("Token is valid: {}", isValid))
                .onErrorReturn(false);
    }


    private boolean isPublicEndpoint(ServerHttpRequest request) {
        return Arrays.stream(publicEndpoints)
                .anyMatch(s -> request.getURI().getPath().matches(s));
    }

    /**
     * Get the order value of this object.
     * <p>Higher values are interpreted as lower priority. As a consequence,
     * the object with the lowest value has the highest priority (somewhat
     * analogous to Servlet {@code load-on-startup} values).
     * <p>Same order values will result in arbitrary sort positions for the
     * affected objects.
     *
     * @return the order value
     * @see #HIGHEST_PRECEDENCE
     * @see #LOWEST_PRECEDENCE
     */
    @Override
    public int getOrder() {
        return -1;
    }


    Mono<Void> unauthorized(ServerHttpResponse response) {
        String message = "Unauthorized";
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.writeWith(Mono.just(response.bufferFactory().wrap(message.getBytes())));
    }
}
