package net.javaguides.chat_service.services.httpClient;

import net.javaguides.chat_service.dtos.response.ResProductDetailsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * File: ProductServiceClient.java
 * Author: Le Van Hoang
 * Date: 08/02/2025
 * Time: 21:09
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@FeignClient(name = "PRODUCT-SERVICE")
public interface ProductServiceClient {

    @GetMapping("/api/v1/products/{id}")
    ResProductDetailsDto getProduct(@PathVariable String id);
}
