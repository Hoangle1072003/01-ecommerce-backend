package net.javaguides.notification_service.services.httpClient;

import net.javaguides.notification_service.config.FeignCustomConfig;
import net.javaguides.notification_service.dto.response.ResOrderByIdDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * File: IOrderServiceClient.java
 * Author: Le Van Hoang
 * Date: 27/01/2025
 * Time: 21:53
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@FeignClient(name = "ORDER-SERVICE", configuration = FeignCustomConfig.class)
public interface IOrderServiceClient {
    @GetMapping("/api/v1/orders/{id}")
    ResOrderByIdDto getOrder(@PathVariable String id);
}
