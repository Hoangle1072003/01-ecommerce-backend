package net.javaguides.product_service.config;


import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import net.javaguides.product_service.repository.ICategoryRepository;
import net.javaguides.product_service.repository.IProductRepository;
import net.javaguides.product_service.shema.Category;
import net.javaguides.product_service.shema.Product;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * File: DatabaseInitializer.java
 * Author: Le Van Hoang
 * Date: 1/11/2025 (11/01/2025)
 * Time: 12:56 AM
 * Version: 1.1
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */
@Service
@RequiredArgsConstructor
public class DatabaseInitializer implements CommandLineRunner {
    private final ICategoryRepository categoryRepository;
    private final IProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();
        System.out.println("DatabaseInitializer.run");
        long countCategory = categoryRepository.count();

        if (countCategory == 0 && productRepository.count() == 0) {
            System.out.println("DatabaseInitializer.run: countCategory == 0");

            for (int i = 0; i < 20; i++) {
                // Create a new category
                Category category = new Category();
                String categoryName = faker.commerce().department();
                category.setName(categoryName);
                category.setDescription(faker.lorem().sentence());
                Category savedCategory = categoryRepository.save(category);

                // Create a product linked to the created category
                Product product = new Product();
                product.setCode(faker.commerce().promotionCode());
                product.setName(faker.commerce().productName());
                product.setBrand(faker.company().name());
                product.setDescription(faker.lorem().paragraph());
                product.setShortDescription(faker.lorem().sentence());
                product.setReleaseDate(Instant.now());
                product.setWeightInGrams(faker.number().randomDouble(2, 50, 2000));
                product.setCategoryID(savedCategory.getId()); // Link product to category

                // Create a list of specifications (specs)
                List<Product.Spec> specs = List.of(
                        new Product.Spec("Color", faker.color().name(), null),
                        new Product.Spec("Size", faker.number().numberBetween(1, 100) + " cm", "cm"),
                        new Product.Spec("Material", faker.commerce().material(), null),
                        new Product.Spec("Weight", faker.number().randomDouble(2, 1, 20) + " kg", "kg"),
                        new Product.Spec("Battery Life", faker.number().numberBetween(1, 24) + " hours", "hours"),
                        new Product.Spec("Resolution", faker.number().numberBetween(1, 100) + " px", "px"),
                        new Product.Spec("Memory", faker.number().numberBetween(1, 100) + " GB", "GB"),
                        new Product.Spec("Storage", faker.number().numberBetween(1, 100) + " GB", "GB"),
                        new Product.Spec("Processor", faker.lorem().word(), null),
                        new Product.Spec("OS", faker.lorem().word(), null),
                        new Product.Spec("Camera", faker.number().numberBetween(1, 100) + " MP", "MP")
                );

                product.setSpecs(specs);

                // Create product variants with random count (1 to 3)
                int variantCount = faker.number().numberBetween(1, 4);
                List<Product.Product_varient> variants = new ArrayList<>();
                for (int j = 0; j < variantCount; j++) {
                    Product.Product_varient variant = new Product.Product_varient(
                            faker.internet().uuid(),
                            faker.commerce().material(),
                            faker.number().randomDouble(2, 10, 500),
                            faker.internet().image(),
                            faker.bool().bool(),
                            faker.number().numberBetween(1L, 100L)
                    );
                    variants.add(variant);
                }
                product.setVarients(variants);

                productRepository.save(product);
            }
        }

        if (countCategory > 0 && productRepository.count() > 0) {
            System.out.println("DatabaseInitializer.run: countCategory > 0");
        } else {
            System.out.println("DatabaseInitializer.run: countCategory == 0");
        }
    }
}
