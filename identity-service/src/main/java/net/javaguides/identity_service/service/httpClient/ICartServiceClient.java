package net.javaguides.identity_service.service.httpClient;

import net.javaguides.identity_service.domain.response.ResCartByUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

/**
 * @author Le Van Hoang
 * @version 1.0
 * @since 2025-02-24
 * Time: 23:45
 */
@FeignClient(name = "CART-SERVICE")
public interface ICartServiceClient {
    @GetMapping("/api/v1/cart/user/{id}")
    ResCartByUser getCartByUserId(@PathVariable UUID id);
}
