package net.javaguides.product_service.kafka;

import lombok.RequiredArgsConstructor;
import net.javaguides.product_service.service.IProductService;
import net.javaguides.product_service.shema.Product;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * File: ProductConsumerEvent.java
 * Author: Le Van Hoang
 * Date: 22/01/2025
 * Time: 16:43
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Component
@RequiredArgsConstructor
public class ProductConsumerEvent {
        private final IProductService productService;
    @KafkaListener(topics = "product-service")
    public void listenProductVariantStock(String productVariantId) {
        Product product = productService.findProductVarientById(productVariantId);

        if (product == null || product.getVarients().isEmpty()) {
            System.out.println("Sản phẩm không tồn tại hoặc không có biến thể.");
            return;
        }

        Product.Product_varient selectedVariant = product.getVarients().get(0);

        if (selectedVariant.getStock() <= 0) {
            System.out.println("Sản phẩm này không còn trong kho.");
        } else {
            System.out.println("Sản phẩm: " + product.getName() + " với biến thể: " + selectedVariant.getName() + " còn lại " + selectedVariant.getStock() + " trong kho.");
        }
    }
}
