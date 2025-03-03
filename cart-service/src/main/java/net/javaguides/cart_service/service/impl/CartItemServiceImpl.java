package net.javaguides.cart_service.service.impl;

import lombok.RequiredArgsConstructor;
import net.javaguides.cart_service.mapper.IMapperCartItem;
import net.javaguides.cart_service.repository.ICartItemRepository;
import net.javaguides.cart_service.repository.ICartRepository;
import net.javaguides.cart_service.schema.Cart;
import net.javaguides.cart_service.schema.CartItem;
import net.javaguides.cart_service.schema.request.ReqUpdateOrderDto;
import net.javaguides.cart_service.schema.response.*;
import net.javaguides.cart_service.service.ICartItemService;
import net.javaguides.cart_service.service.httpClient.IIdentityServiceClient;
import net.javaguides.cart_service.service.httpClient.IOrderServiceClient;
import net.javaguides.cart_service.service.httpClient.IProductServiceClient;
import net.javaguides.cart_service.utils.constant.CartStatusEnum;
import net.javaguides.cart_service.utils.constant.PaymentStatus;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    private final IOrderServiceClient orderServiceClient;
    private final IMapperCartItem mapperCartItem;

    @Override
    public ResCartItemDto getCartItemByUserId(UUID id) throws Exception {
        ResUserDTO user = identityServiceClient.getUserById(id);
        if (user == null || user.getId() == null) {
            throw new Exception("User not found");
        }

        List<Cart> carts = cartRepository.findCartByUserId(user.getId());
        if (carts == null || carts.isEmpty()) {
            throw new Exception("Cart not found");
        }

        Cart activeCart = carts.stream()
                .filter(cart -> cart.getStatus() == CartStatusEnum.ACTIVE)
                .findFirst()
                .orElseThrow(() -> new Exception("No active cart found"));

        List<CartItem> cartItems = cartItemRepository.findByCartId(activeCart.getId());
        System.out.println("cartItems: " + cartItems);

        if (cartItems == null || cartItems.isEmpty()) {
            return new ResCartItemDto(user.getId(), new ArrayList<>());
        }

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
                    varientDto.setDeletedAt(cartItem.getDeletedAt());
                    varientDtos.add(varientDto);
                }
                productDto.setVarients(varientDtos);
                productDtos.add(productDto);
            }
        }
        return new ResCartItemDto(user.getId(), productDtos);
    }

    @Override
    public ResCartItemDto getCartItemByIdAndStatus(UUID id) throws Exception {
        ResUserDTO user = identityServiceClient.getUserById(id);
        if (user == null || user.getId() == null) {
            throw new Exception("User not found");
        }

        List<Cart> carts = cartRepository.findCartByUserId(user.getId());
        if (carts == null || carts.isEmpty()) {
            throw new Exception("Cart not found");
        }

        Cart activeCart = carts.stream()
//                .filter(cart -> cart.getStatus() == CartStatusEnum.COMPLETED)
                .findFirst()
                .orElseThrow(() -> new Exception("No active cart found"));

        List<CartItem> cartItems = cartItemRepository.findByCartId(activeCart.getId());
        System.out.println("cartItems: " + cartItems);

        if (cartItems == null || cartItems.isEmpty()) {
            return new ResCartItemDto(user.getId(), new ArrayList<>());
        }

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
        return new ResCartItemDto(user.getId(), productDtos);
    }


    @Override
    public Void deleteCartItem(ResCartItemDeleteDto resCartItemDelete) throws Exception {
        List<Cart> cartList = cartRepository.findCartByUserId(resCartItemDelete.getUserId());

        if (cartList == null || cartList.isEmpty()) {
            throw new Exception("Cart not found");
        }

        Cart cart = cartList.stream()
                .filter(c -> c.getStatus() == CartStatusEnum.ACTIVE)
                .findFirst()
                .orElseThrow(() -> new Exception("No active cart found"));

        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());
        if (cartItems == null || cartItems.isEmpty()) {
            throw new Exception("Cart is empty");
        }

        List<CartItem> itemsToDelete = cartItems.stream()
                .filter(item -> resCartItemDelete.getVariantId().contains(item.getVariantId()))
                .collect(Collectors.toList());

        if (itemsToDelete.isEmpty()) {
            throw new Exception("No matching cart items found to delete");
        }

        for (CartItem item : itemsToDelete) {
            item.setDeletedAt(Instant.now());
        }
        cartItemRepository.saveAll(itemsToDelete);

        double newTotal = cartItems.stream()
                .filter(item -> item.getDeletedAt() == null)
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();

        cart.setTotal(newTotal);
        cart.setModifiedOn(Instant.now());

        if (newTotal == 0) {
            System.out.println("Hủy giỏ hàng");
            cart.setStatus(CartStatusEnum.CANCELLED);
        }

        cartRepository.save(cart);

        try {
            ResOrderByIdDto order = orderServiceClient.getOrderByCartId(cart.getId());
            if (order != null && order.getPaymentStatus().equals(PaymentStatus.PENDING)) {
                System.out.println("newTotal: " + newTotal);
                System.out.println("order.getTotalAmount(): " + order.getTotalAmount());
                if (newTotal == 0) {
                    System.out.println("Hủy đơn hàng");
                    orderServiceClient.updateOrderStatus(order.getId());
                } else {
                    ReqUpdateOrderDto reqUpdateOrderDto = new ReqUpdateOrderDto();
                    reqUpdateOrderDto.setId(order.getId());
                    reqUpdateOrderDto.setTotal_amount(String.valueOf(newTotal));
                    orderServiceClient.updateOrder(reqUpdateOrderDto);
                }
            }
        } catch (Exception e) {
            System.out.println("Không tìm thấy đơn hàng liên quan, bỏ qua cập nhật đơn hàng.");
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

    @Override
    public ResUpdateCartItemDto updateCartItemQuantity(UUID userId, String productId, String variantId, int quantity) throws Exception {
        ResUserDTO user = Optional.ofNullable(identityServiceClient.getUserById(userId))
                .orElseThrow(() -> new Exception("Người dùng không tồn tại"));

        Cart activeCart = cartRepository.findCartByUserId(user.getId()).stream()
                .filter(cart -> cart.getStatus() == CartStatusEnum.ACTIVE)
                .findFirst()
                .orElseThrow(() -> new Exception("Không có giỏ hàng đang hoạt động"));

        CartItem cartItem = cartItemRepository.findByCartId(activeCart.getId()).stream()
                .filter(item -> item.getProductId().equals(productId) &&
                        item.getVariantId().equals(variantId) &&
                        item.getDeletedAt() == null)
                .findFirst()
                .orElseThrow(() -> new Exception("Không tìm thấy mặt hàng trong giỏ hoặc đã bị xóa"));

        System.out.println("cartItem update: " + cartItem);

        if (quantity <= 0) throw new Exception("Số lượng phải lớn hơn 0");

        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);

        double total = cartItemRepository.findByCartId(activeCart.getId()).stream()
                .filter(item -> item.getDeletedAt() == null)
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();

        activeCart.setTotal(total);
        activeCart.setModifiedOn(Instant.now());
        cartRepository.save(activeCart);

        try {
            ResOrderByIdDto order = orderServiceClient.getOrderByCartId(cartItem.getCartId());
            if (order != null) {
                ReqUpdateOrderDto reqUpdateOrderDto = new ReqUpdateOrderDto();
                reqUpdateOrderDto.setId(order.getId());
                reqUpdateOrderDto.setTotal_amount(String.valueOf(total));
                orderServiceClient.updateOrder(reqUpdateOrderDto);
                System.out.println("Tồn tại đơn hàng liên quan, cập nhật lại.");
            } else {
                System.out.println("Không tồn tại đơn hàng liên quan.");
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi cập nhật đơn hàng: " + e.getMessage());
        }

        return new ResUpdateCartItemDto(
                "Cập nhật số lượng thành công",
                productId,
                variantId,
                quantity,
                total
        );
    }


    @Override
    public List<ResGetCartItemDto> getCartItemByCartIdAndDeletedAtIsNull(String id) throws Exception {
        List<CartItem> cartItems = cartItemRepository.findCartItemByCartIdAndDeletedAtIsNull(id);
        if (cartItems == null || cartItems.isEmpty()) {
            throw new Exception("Cart item not found");
        }
        return cartItems.stream()
                .map(mapperCartItem::toResGetCartItemDto)
                .toList();
    }
}
