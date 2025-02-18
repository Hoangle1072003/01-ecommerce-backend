package net.javaguides.payment_service.services.httpClient;

import net.javaguides.payment_service.schemas.response.ResOrderByIdDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * File: IOrderServiceClient.java
 * Author: Le Van Hoang
 * Date: 01/02/2025
 * Time: 00:24
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */
@FeignClient(name = "ORDER-SERVICE")
public interface IOrderServiceClient {
    @GetMapping("/api/v1/orders/{id}")
    ResOrderByIdDto getOrder(@PathVariable String id);
}
