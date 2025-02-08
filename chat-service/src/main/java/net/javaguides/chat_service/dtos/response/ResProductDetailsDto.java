package net.javaguides.chat_service.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * File: ResProductDetailsDto.java
 * Author: Le Van Hoang
 * Date: 08/02/2025
 * Time: 21:11
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResProductDetailsDto {
    String id;
    String code;
    String name;
    String brand;
    String description;
    String ShortDescription;
    Date releaseDate;
    double weightInGrams;
    List<Product.Spec> specs;
    List<Product.Product_varient> varients;
}
