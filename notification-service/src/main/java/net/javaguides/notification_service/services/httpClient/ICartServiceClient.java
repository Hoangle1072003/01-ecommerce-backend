package net.javaguides.notification_service.services.httpClient;

import net.javaguides.notification_service.config.FeignCustomConfig;
import net.javaguides.notification_service.dto.response.ResCartByIdDto;
import net.javaguides.notification_service.dto.response.ResCartItemByIdDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * File: ICartServiceClient.java
 * Author: Le Van Hoang
 * Date: 27/01/2025
 * Time: 21:59
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@FeignClient(name = "CART-SERVICE", configuration = FeignCustomConfig.class)
public interface ICartServiceClient {
    @GetMapping("/api/v1/cart/{id}")
    ResCartByIdDto getCartById(@PathVariable String id);

    @GetMapping("/api/v1/cart-item/get-cart-item-by-id-status/{id}")
    ResCartItemByIdDto getCartItemByIdAndStatus(@PathVariable("id") String id);
}
