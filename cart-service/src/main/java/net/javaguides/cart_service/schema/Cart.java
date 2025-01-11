package net.javaguides.cart_service.schema;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serial;
import java.io.Serializable;

/**
 * File: Cart.java
 * Author: Le Van Hoang
 * Date: 12/01/2025
 * Time: 00:39
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Document(collection = "cart")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart extends AbstractMappedEntity implements Serializable {
    private static final long serialVersionUID = -1486934856014993488L;
    @Id
    private String id;
    @Field("user_id")
    private String userId;
    @Field("product_id")
    private String productId;
    private int quantity;
    private double price;
    private double total;
}
