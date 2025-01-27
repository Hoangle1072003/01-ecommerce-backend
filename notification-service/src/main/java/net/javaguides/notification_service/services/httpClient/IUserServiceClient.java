package net.javaguides.notification_service.services.httpClient;

import net.javaguides.notification_service.config.FeignCustomConfig;
import net.javaguides.notification_service.dto.response.ResUserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

/**
 * File: IUserServiceClient.java
 * Author: Le Van Hoang
 * Date: 27/01/2025
 * Time: 21:30
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */
@FeignClient(name = "IDENTITY-SERVICE", configuration = FeignCustomConfig.class)
public interface IUserServiceClient {
    @GetMapping("/api/v1/user/{id}")
    ResUserDTO getUserById(@PathVariable("id") UUID id) throws Exception;
}
