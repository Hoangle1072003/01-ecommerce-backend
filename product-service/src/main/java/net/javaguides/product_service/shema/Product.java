package net.javaguides.product_service.shema;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.Date;
import java.util.List;

/**
 * File: Product.java
 * Author: Le Van Hoang
 * Date: 1/9/2025 (09/01/2025)
 * Time: 1:33 PM
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Document(collection = "products")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product extends AbstractMappedEntity {
    @Id
    private String id;


    private String code;

    private String name;

    private String brand;

    private String description;
    @Field("short_description")
    private String ShortDescription;

    @Field("release_date")
    private Instant releaseDate;


    @Field("weight_g")
    private double weightInGrams;


    @Field("category_id")
    private String categoryID;

    private List<Spec> specs;

    private List<Product_varient> varients;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Spec {
        private String k;
        private Object v;
        private String u;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Product_varient {
        @Id
        private String id;
        @Field("varient_name")
        private String name;
        private double price;
        private String image;
        @Field("is_available")
        private boolean isAvailable;
        @Field("in_stock")
        private Long stock;
    }
}
