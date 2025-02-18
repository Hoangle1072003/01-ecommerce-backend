package net.javaguides.payment_service.services.httpClient;

import net.javaguides.payment_service.schemas.response.ResUserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

/**
 * File: IIdentityServiceClient.java
 * Author: Le Van Hoang
 * Date: 31/01/2025
 * Time: 22:35
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@FeignClient(name = "IDENTITY-SERVICE")
public interface IIdentityServiceClient {
    @GetMapping(value = "/api/v1/user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResUserDTO getUserById(@PathVariable("id") UUID id);
}
