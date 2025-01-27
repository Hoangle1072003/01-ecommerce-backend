package net.javaguides.order_service.services.httpClient;

import net.javaguides.order_service.shemas.response.ResCartClientDto;
import net.javaguides.event.dto.CartItemClientEvent;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * File: ICartServiceClient.java
 * Author: Le Van Hoang
 * Date: 25/01/2025
 * Time: 16:55
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@FeignClient(name = "CART-SERVICE")
public interface ICartServiceClient {
    @GetMapping("/api/v1/cart/{id}")
    ResCartClientDto getCartById(@PathVariable String id);

    @GetMapping("/api/v1/cart-item/cart/{id}")
    List<CartItemClientEvent> getCartItemByCartId(@PathVariable("id") String id);
}
