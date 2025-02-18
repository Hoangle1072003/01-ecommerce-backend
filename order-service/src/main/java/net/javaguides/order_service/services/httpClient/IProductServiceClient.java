package net.javaguides.order_service.services.httpClient;

import net.javaguides.order_service.shemas.response.ResProductVarientDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * File: IProductClient.java
 * Author: Le Van Hoang
 * Date: 05/02/2025
 * Time: 15:40
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@FeignClient(name = "PRODUCT-SERVICE")
public interface IProductServiceClient {
    @GetMapping("/api/v1/products/varient/{id}")
    ResProductVarientDto getProductVarient(@PathVariable String id);
}
