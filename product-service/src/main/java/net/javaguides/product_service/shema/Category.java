package net.javaguides.product_service.shema;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * File: Category.java
 * Author: Le Van Hoang
 * Date: 1/11/2025 (11/01/2025)
 * Time: 12:08 AM
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */
@Document(collection = "categories")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category extends AbstractMappedEntity {
    @Id
    private String id;
    private String name;
    private String description;
    
}
