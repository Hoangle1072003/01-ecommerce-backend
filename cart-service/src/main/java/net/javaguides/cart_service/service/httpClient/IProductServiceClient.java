package net.javaguides.cart_service.service.httpClient;

import net.javaguides.cart_service.schema.response.ResProductVarientDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * File: IProductServiceClient.java
 * Author: Le Van Hoang
 * Date: 18/01/2025
 * Time: 22:57
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@FeignClient(name = "PRODUCT-SERVICE")
public interface IProductServiceClient {
    @GetMapping(value="/api/v1/products/varient/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResProductVarientDto getProductVarient(@PathVariable String id) ;
}
