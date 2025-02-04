package net.javaguides.cart_service.service.httpClient;

import net.javaguides.cart_service.schema.response.ResUserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.print.attribute.standard.Media;
import java.util.UUID;

/**
 * File: UserServiceClient.java
 * Author: Le Van Hoang
 * Date: 18/01/2025
 * Time: 16:52
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@FeignClient(name = "IDENTITY-SERVICE")
public interface IIdentityServiceClient {
    @GetMapping(value = "/api/v1/user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResUserDTO getUserById(@PathVariable("id") UUID id);
}
