package net.javaguides.product_service.shema.response;

import lombok.Value;
import net.javaguides.product_service.shema.Product;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * DTO for {@link net.javaguides.product_service.shema.Product}
 */
@Value
public class ResProductDetailsDto implements Serializable {
    String id;
    String code;
    String name;
    String brand;
    String description;
    String ShortDescription;
    String category;
    Date releaseDate;
    double weightInGrams;
    List<Product.Spec> specs;
    List<Product.Product_varient> varients;
}