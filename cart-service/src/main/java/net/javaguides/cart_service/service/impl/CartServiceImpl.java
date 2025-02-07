package net.javaguides.cart_service.service.impl;

import lombok.RequiredArgsConstructor;
import net.javaguides.cart_service.mapper.ICartMapper;
import net.javaguides.cart_service.repository.ICartItemRepository;
import net.javaguides.cart_service.repository.ICartRepository;
import net.javaguides.cart_service.schema.Cart;
import net.javaguides.cart_service.schema.CartItem;
import net.javaguides.cart_service.schema.request.ReqCartDto;
import net.javaguides.cart_service.schema.request.ReqUpdateCart;
import net.javaguides.cart_service.schema.request.ReqUpdateOrderDto;
import net.javaguides.cart_service.schema.response.*;
import net.javaguides.cart_service.service.ICartService;
import net.javaguides.cart_service.service.httpClient.IIdentityServiceClient;
import net.javaguides.cart_service.service.httpClient.IOrderServiceClient;
import net.javaguides.cart_service.service.httpClient.IProductServiceClient;
import net.javaguides.cart_service.utils.constant.CartStatusEnum;
import net.javaguides.cart_service.utils.constant.PaymentStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * File: CartServiceImpl.java
 * Author: Le Van Hoang
 * Date: 12/01/2025
 * Time: 00:48
 * Version: 1.1
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements ICartService {
    private final ICartRepository cartRepository;
    private final IIdentityServiceClient identityServiceClient;
    private final IProductServiceClient productServiceClient;
    private final ICartItemRepository cartItemRepository;
    private final IOrderServiceClient orderServiceClient;
    private final ICartMapper cartMapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public Cart save(ReqCartDto reqCartDto) throws Exception {
        ResUserDTO user = identityServiceClient.getUserById(reqCartDto.getUserId());
        if (user == null) {
            throw new IllegalArgumentException("Người dùng không tồn tại.");
        }

        Cart cart = cartRepository.findByUserIdAndStatus(reqCartDto.getUserId(), CartStatusEnum.ACTIVE);
        if (cart == null) {
            cart = createNewCart(reqCartDto.getUserId());
        }

        ResProductVarientDto productVarient = productServiceClient.getProductVarient(reqCartDto.getProductVariantId());
        if (productVarient == null || productVarient.getVarients().isEmpty()) {
            throw new IllegalArgumentException("Product variant không tồn tại hoặc không có biến thể.");
        }

        ResProductVarientDto.VarientDto selectedVarient = productVarient.getVarients().get(0);

        int totalQuantityInCart = cartItemRepository.findByCartIdAndVariantId(cart.getId(), selectedVarient.getId())
                .stream()
                .mapToInt(CartItem::getQuantity)
                .sum();

        if (totalQuantityInCart + reqCartDto.getQuantity() > selectedVarient.getStock()) {
            throw new IllegalArgumentException("Số lượng sản phẩm không đủ. Đã có "
                    + totalQuantityInCart + " trong giỏ hàng. Chỉ còn lại "
                    + (selectedVarient.getStock() - totalQuantityInCart) + " sản phẩm.");
        }

        processCartItem(cart, reqCartDto, productVarient.getId(), selectedVarient);
        double additionalAmount = selectedVarient.getPrice() * reqCartDto.getQuantity();
        cart.setTotal(cart.getTotal() + additionalAmount);
        cartRepository.save(cart);
        ResOrderByIdDto order = orderServiceClient.getOrderByCartId(cart.getId());
        if (order != null && order.getPaymentStatus().equals(PaymentStatus.PENDING)) {
            ReqUpdateOrderDto reqUpdateOrderDto = new ReqUpdateOrderDto();
            reqUpdateOrderDto.setId(order.getId());
            reqUpdateOrderDto.setTotal_amount(String.valueOf(order.getTotalAmount() + additionalAmount));
            orderServiceClient.updateOrder(reqUpdateOrderDto);
            return cart;
        }
        return cart;
    }

    @Override
    public Cart createNewCart(UUID userId) {
        Cart newCart = new Cart();
        newCart.setUserId(userId);
        newCart.setStatus(CartStatusEnum.ACTIVE);
        newCart.setTotal(0.0);
        return cartRepository.save(newCart);
    }


    @Override
    public void processCartItem(Cart cart, ReqCartDto reqCartDto, String productId, ResProductVarientDto.VarientDto varient) {
        List<CartItem> existingCartItems = cartItemRepository.findByCartIdAndVariantId(cart.getId(), varient.getId());

        boolean hasDeletedItem = existingCartItems.stream().anyMatch(item -> item.getDeletedAt() != null);

        if (!existingCartItems.isEmpty() && !hasDeletedItem) {
            CartItem existingCartItem = existingCartItems.get(0);
            existingCartItem.setQuantity(existingCartItem.getQuantity() + reqCartDto.getQuantity());
            existingCartItem.setPrice(varient.getPrice());
            cartItemRepository.save(existingCartItem);
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setCartId(cart.getId());
            cartItem.setProductId(productId);
            cartItem.setVariantId(varient.getId());
            cartItem.setPrice(varient.getPrice());
            cartItem.setQuantity(reqCartDto.getQuantity());
            cartItem.setDeletedAt(null);
            cartItemRepository.save(cartItem);
        }
    }


    @Override
    public ResCartByUser findByUserId(UUID userId) {
        Cart cart = cartRepository.findByUserIdAndStatus(userId, CartStatusEnum.ACTIVE);
        if (cart == null) {
            return null;
        }
        return cartMapper.toResCartByUser(cart);
    }

    @Override
    public ResCartUpdateDto updateCartStatus(String cartId) {
        System.out.println("Cart id: " + cartId);
        Cart cart = cartRepository.findById(cartId).orElse(null);
        System.out.println("Cart: " + cart);
        if (cart == null) {
            throw new IllegalArgumentException("Giỏ hàng không tồn tại.");
        }
        if (cart.getStatus().equals(CartStatusEnum.ACTIVE)) {
            cart.setStatus(CartStatusEnum.PENDING);
            cartRepository.save(cart);
            return cartMapper.toResCartUpdateDto(cart);
        }
        return null;
    }

    @Override
    public ResCartUpdateDto updateCartStatusCancelled(String cartId) {
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) {
            throw new IllegalArgumentException("Giỏ hàng không tồn tại.");
        }
        if (cart.getStatus().equals(CartStatusEnum.ACTIVE)) {
            cart.setStatus(CartStatusEnum.CANCELLED);
            cartRepository.save(cart);
            return cartMapper.toResCartUpdateDto(cart);
        }
        return null;
    }

    @Override
    public ResCartUpdateDto updateStatusCartCompleted(String cartId) {
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) {
            throw new IllegalArgumentException("Giỏ hàng không tồn tại.");
        }
        if (cart.getStatus().equals(CartStatusEnum.PENDING)) {
            cart.setStatus(CartStatusEnum.SHIPPING);
            cartRepository.save(cart);
            return cartMapper.toResCartUpdateDto(cart);
        }
        return null;
    }

    @Override
    public Cart findById(String id) {
        return cartRepository.findById(id).orElse(null);
    }
}
