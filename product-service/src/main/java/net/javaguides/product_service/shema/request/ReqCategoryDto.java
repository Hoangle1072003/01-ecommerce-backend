package net.javaguides.product_service.shema.request;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link net.javaguides.product_service.shema.Category}
 */
@Value
public class ReqCategoryDto implements Serializable {
    String name;
    String description;
}