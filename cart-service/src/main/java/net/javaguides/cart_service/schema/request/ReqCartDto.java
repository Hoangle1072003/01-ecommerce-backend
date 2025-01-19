package net.javaguides.cart_service.schema.request;


import lombok.Data;

import java.util.UUID;

@Data
public class ReqCartDto  {
    UUID userId;
    String productId;
    String productVariantId;
    int quantity;
}