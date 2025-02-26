package net.javaguides.identity_service.service.httpClient;

import net.javaguides.identity_service.domain.response.ResAllOrderByUserIdDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author Le Van Hoang
 * @version 1.0
 * @since 2025-02-25
 * Time: 00:41
 */
@FeignClient(name = "ORDER-SERVICE")
public interface IOrderServiceClient {
    @GetMapping("/api/v1/orders/get-all-orders-by-user-id-client")
    List<ResAllOrderByUserIdDto> getAllOrdersByUserIdClient(@RequestParam String userId);

}
