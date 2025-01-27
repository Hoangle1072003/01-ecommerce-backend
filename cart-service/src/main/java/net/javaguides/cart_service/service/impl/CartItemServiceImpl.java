package net.javaguides.cart_service.service.impl;

import lombok.RequiredArgsConstructor;
import net.javaguides.cart_service.mapper.IMapperCartItem;
import net.javaguides.cart_service.repository.ICartItemRepository;
import net.javaguides.cart_service.repository.ICartRepository;
import net.javaguides.cart_service.schema.Cart;
import net.javaguides.cart_service.schema.CartItem;
import net.javaguides.cart_service.schema.response.*;
import net.javaguides.cart_service.service.ICartItemService;
import net.javaguides.cart_service.service.httpClient.IIdentityServiceClient;
import net.javaguides.cart_service.service.httpClient.IProductServiceClient;
import net.javaguides.cart_service.utils.constant.CartStatusEnum;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * File: CartItemServiceImpl.java
 * Author: Le Van Hoang
 * Date: 21/01/2025
 * Time: 15:44
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements ICartItemService {
    private final ICartItemRepository cartItemRepository;
    private final ICartRepository cartRepository;
    private final IIdentityServiceClient identityServiceClient;
    private final IProductServiceClient productServiceClient;
    private final IMapperCartItem mapperCartItem;

    @Override
    public ResCartItemDto getCartItemByUserId(UUID id) throws Exception {
        ResUserDTO user = identityServiceClient.getUserById(id);
        if (user == null || user.getId() == null) {
            throw new Exception("User not found");
        }

        Cart cart = cartRepository.findCartByUserId(user.getId());
        if (cart == null) {
            throw new Exception("Cart not found");
        }

        if (cart.getStatus() == CartStatusEnum.ACTIVE) {
            List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());
            if (cartItems != null && !cartItems.isEmpty()) {
                List<ResCartItemDto.ProductDto> productDtos = new ArrayList<>();

                for (CartItem cartItem : cartItems) {
                    ResProductVarientDto productVarientDto = productServiceClient.getProductVarient(cartItem.getVariantId());

                    if (productVarientDto != null) {
                        ResCartItemDto.ProductDto productDto = new ResCartItemDto.ProductDto();
                        productDto.setProductId(cartItem.getProductId());
                        productDto.setProductName(productVarientDto.getName());
                        productDto.setBrandName(productVarientDto.getName());
                        List<ResCartItemDto.VarientDto> varientDtos = new ArrayList<>();
                        for (ResProductVarientDto.VarientDto varient : productVarientDto.getVarients()) {
                            ResCartItemDto.VarientDto varientDto = new ResCartItemDto.VarientDto();
                            varientDto.setVariantId(varient.getId());
                            varientDto.setVariantName(varient.getName());
                            varientDto.setVariant_img(varient.getImage());
                            varientDto.setVariantPrice(varient.getPrice());
                            varientDto.setQuantity(cartItem.getQuantity());
                            varientDto.setTotal(varient.getPrice() * cartItem.getQuantity());

                            varientDtos.add(varientDto);
                        }
                        productDto.setVarients(varientDtos);
                        productDtos.add(productDto);
                    }
                }

                ResCartItemDto resCartItemDto = new ResCartItemDto();
                resCartItemDto.setUserId(user.getId());
                resCartItemDto.setProducts(productDtos);
                return resCartItemDto;
            }
        }

        return new ResCartItemDto();
    }

    @Override
    public Void deleteCartItem(ResCartItemDeleteDto resCartItemDelete) throws Exception {
        Cart cart = cartRepository.findCartByUserId(resCartItemDelete.getUserId());
        if (cart == null) {
            throw new Exception("Cart not found");
        }
        if (cart.getStatus() == CartStatusEnum.ACTIVE) {
            List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());
            if (cartItems != null && !cartItems.isEmpty()) {
                List<CartItem> cartItemsDelete = cartItems.stream().filter(cartItem -> resCartItemDelete.getVariantId().contains(cartItem.getVariantId())).collect(Collectors.toList());
                cartItemRepository.deleteAll(cartItemsDelete);

                double newTotal = cartItems.stream()
                        .filter(cartItem -> !resCartItemDelete.getVariantId().contains(cartItem.getVariantId()))
                        .mapToDouble(cartItem -> cartItem.getPrice() * cartItem.getQuantity())
                        .sum();

                cart.setTotal(newTotal);
                cart.setModifiedOn(Instant.now());
                cartRepository.save(cart);
            }
        }
        return null;
    }

    @Override
    public List<ResGetCartItemDto> getCartItemByCartId(String id) throws Exception {
        List<CartItem> cartItems = cartItemRepository.findCartItemByCartId(id);
        if (cartItems == null || cartItems.isEmpty()) {
            throw new Exception("Cart item not found");
        }

        return cartItems.stream()
                .map(mapperCartItem::toResGetCartItemDto)
                .toList();
    }
}
