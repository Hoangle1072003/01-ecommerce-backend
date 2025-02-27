package net.javaguides.product_service.shema.response;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link net.javaguides.product_service.shema.Product}
 */
@Value
public class ResProductDto implements Serializable {
    String id;
    String name;
    List<Product_varientDto> varients;

    /**
     * DTO for {@link net.javaguides.product_service.shema.Product.Product_varient}
     */
    @Value
    public static class Product_varientDto implements Serializable {
        String id;
        String name;
        double price;
        String image;
    }
}