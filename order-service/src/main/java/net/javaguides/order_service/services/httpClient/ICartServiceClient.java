package net.javaguides.order_service.services.httpClient;


import net.javaguides.order_service.shemas.response.Cart;
import net.javaguides.event.dto.CartItemClientEvent;
import net.javaguides.order_service.shemas.response.ResCartUpdateDto;
import net.javaguides.order_service.shemas.response.ResGetCartItemDto;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

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
    Cart getCartById(@PathVariable String id);

    @GetMapping("/api/v1/cart-item/cart/{id}")
    List<CartItemClientEvent> getCartItemByCartId(@PathVariable("id") String id);

    @GetMapping("/api/v1/cart-item/cart/deleted-at-is-null/{id}")
    List<ResGetCartItemDto> getCartItemByCartIdAndDeletedAtIsNull(@PathVariable("id") String id) throws Exception;

    @PutMapping("/api/v1/cart/update-status-cart-completed/{id}")
    ResCartUpdateDto updateStatusCartCompleted(@PathVariable String id);
}
