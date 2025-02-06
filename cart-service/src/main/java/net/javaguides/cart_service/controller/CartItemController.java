package net.javaguides.cart_service.controller;

import lombok.RequiredArgsConstructor;
import net.javaguides.cart_service.schema.request.ReqCartItemUpdateDto;
import net.javaguides.cart_service.schema.response.*;
import net.javaguides.cart_service.service.ICartItemService;
import net.javaguides.cart_service.utils.annotation.ApiMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * File: CartItem.java
 * Author: Le Van Hoang
 * Date: 21/01/2025
 * Time: 15:43
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@RestController
@RequestMapping("/api/v1/cart-item")
@RequiredArgsConstructor
public class CartItemController {
    private final ICartItemService cartItemService;

    @GetMapping("{id}")
    @ApiMessage("Get cart item by user id")
    public ResponseEntity<ResCartItemDto> getCartItemByUserId(@PathVariable("id") UUID id) throws Exception {
        return ResponseEntity.ok(cartItemService.getCartItemByUserId(id));
    }

    @GetMapping("/get-cart-item-by-id-status/{id}")
    @ApiMessage("Get cart item by id and status")
    public ResponseEntity<ResCartItemDto> getCartItemByIdAndStatus(@PathVariable("id") UUID id) throws Exception {
        return ResponseEntity.ok(cartItemService.getCartItemByIdAndStatus(id));
    }

    @DeleteMapping
    @ApiMessage("Delete cart item")
    public ResponseEntity<Void> deleteCartItem(@RequestBody ResCartItemDeleteDto resCartItemDelete) throws Exception {
        cartItemService.deleteCartItem(resCartItemDelete);
        return ResponseEntity.ok().build();
    }

    // get cart item by cart id
    @GetMapping("/cart/{id}")
    @ApiMessage("Get cart item by cart id")
    public ResponseEntity<List<ResGetCartItemDto>> getCartItemByCartId(@PathVariable("id") String id) throws Exception {
        return ResponseEntity.ok(cartItemService.getCartItemByCartId(id));
    }

    @PutMapping("/update-quantity")
    @ApiMessage("Cập nhật số lượng mặt hàng trong giỏ hàng")
    public ResponseEntity<ResUpdateCartItemDto> updateCartItemQuantity(@RequestBody ReqCartItemUpdateDto cartItemUpdateRequest) throws Exception {
        ResUpdateCartItemDto updatedCartItem = cartItemService.updateCartItemQuantity(
                cartItemUpdateRequest.getUserId(),
                cartItemUpdateRequest.getProductId(),
                cartItemUpdateRequest.getVariantId(),
                cartItemUpdateRequest.getQuantity());
        return ResponseEntity.ok(updatedCartItem);
    }

    @GetMapping("/cart/deleted-at-is-null/{id}")
    @ApiMessage("Get cart item by cart id and deleted at is null")
    public ResponseEntity<List<ResGetCartItemDto>> getCartItemByCartIdAndDeletedAtIsNull(@PathVariable("id") String id) throws Exception {
        return ResponseEntity.ok(cartItemService.getCartItemByCartIdAndDeletedAtIsNull(id));
    }
}
