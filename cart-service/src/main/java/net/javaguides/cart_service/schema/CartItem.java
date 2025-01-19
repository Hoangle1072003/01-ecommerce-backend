package net.javaguides.cart_service.schema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serial;
import java.io.Serializable;

/**
 * File: CartItem.java
 * Author: Le Van Hoang
 * Date: 12/01/2025
 * Time: 20:49
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Document(collection = "cart_item")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItem extends AbstractMappedEntity implements Serializable {
    private static final long serialVersionUID = 760078460929410864L;
    @Id
    private String id;
    @Field("cart_id")
    private String cartId;
    @Field("product_id")
    private String productId;
    @Field("variant_id")
    private String variantId;
    private int quantity;
    private Double price;
}
