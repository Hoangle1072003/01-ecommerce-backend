package net.javaguides.product_service.shema.response;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link net.javaguides.product_service.shema.Category}
 */
@Value
public class ResCategoryDto implements Serializable {
    String id;
    String name;
}