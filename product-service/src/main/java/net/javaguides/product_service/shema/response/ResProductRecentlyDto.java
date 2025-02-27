package net.javaguides.product_service.shema.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResProductRecentlyDto implements Serializable {
    private String id;
    private String name;
    private List<Product_varientDto> varients;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Product_varientDto implements Serializable {
        private String id;
        private String name;
        private double price;
        private String image;
    }
}
