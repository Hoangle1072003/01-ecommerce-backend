package net.javaguides.cart_service.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import feign.Response;
import feign.codec.DecodeException;
import feign.codec.Decoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * File: FeignCustomConfig.java
 * Author: Le Van Hoang
 * Date: 19/01/2025
 * Time: 22:13
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */
@Configuration
public class FeignCustomConfig {
    @Bean
    public Decoder feignDecoder(ObjectMapper objectMapper) {
        return new Decoder() {
            @Override
            public Object decode(Response response, Type type) throws IOException, DecodeException, FeignException {
                JsonNode root = objectMapper.readTree(response.body().asInputStream());
                JsonNode dataNode = root.get("data");

                if (dataNode == null) {
                    throw new RuntimeException("Response does not contain 'data' key");
                }
                return objectMapper.treeToValue(dataNode, objectMapper.constructType(type));
            }
        };
    }
}
