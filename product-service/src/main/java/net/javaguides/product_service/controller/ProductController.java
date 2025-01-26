package net.javaguides.product_service.controller;

import lombok.RequiredArgsConstructor;
import net.javaguides.event.dto.CartItemClientEvent;
import net.javaguides.product_service.mapper.IProductMapper;
import net.javaguides.product_service.repository.IProductRepository;
import net.javaguides.product_service.service.IProductService;
import net.javaguides.product_service.shema.Product;
import net.javaguides.product_service.shema.response.ResProductDetailsDto;
import net.javaguides.product_service.shema.response.ResProductDto;
import net.javaguides.product_service.shema.response.ResProductVarientDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * File: ProductController.java
 * Author: Le Van Hoang
 * Date: 1/9/2025 (09/01/2025)
 * Time: 1:33 PM
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final IProductRepository productRepository;
    private final IProductMapper productMapper;
    private final IProductService productService;

    @GetMapping()
    public ResponseEntity<List<ResProductDto>> getProducts() {
        List<Product> products = productRepository.findAll();
        List<ResProductDto> resProductDtos = products.stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resProductDtos);
    }

    @PostMapping()
    public ResponseEntity<String> saveProduct() {
        Product product1 = new Product();
        product1.setCode("P001");
        product1.setName("Men's T-Shirt");
        product1.setBrand("Brand A");
        product1.setDescription("A comfortable cotton T-shirt for men.");
        product1.setShortDescription("Comfortable cotton material, ideal for daily wear.");
        product1.setReleaseDate(new Date().toInstant());
        product1.setWeightInGrams(200.0);
        product1.setSpecs(Arrays.asList(
                new Product.Spec("Size", "M", "cm"),
                new Product.Spec("Color", "Red", "cm")
        ));
        product1.setVarients(Arrays.asList(
                new Product.Product_varient("V001", "M Red", 19.99, "https://salt.tikicdn.com/cache/750x750/ts/product/22/06/1c/aebf0f634e74c05b2d8282b2a3f2b1d5.png.webp", true, 100L),
                new Product.Product_varient("V002", "L Red", 22.99, "https://salt.tikicdn.com/cache/750x750/ts/product/8d/16/3e/9242556bf1de5fcdf30aec762ca9f51b.jpg.webp", true, 50L)
        ));

        Product product2 = new Product();
        product2.setCode("P002");
        product2.setName("Java Programming Book");
        product2.setBrand("Tech Books");
        product2.setDescription("A comprehensive guide to Java programming.");
        product2.setShortDescription("A complete guide for mastering Java programming language.");
        product2.setReleaseDate(new Date().toInstant());
        product2.setWeightInGrams(500.0);
        product2.setSpecs(Arrays.asList(
                new Product.Spec("Author", "John Doe", "String"),
                new Product.Spec("Pages", "350", "pages")
        ));
        product2.setVarients(Arrays.asList(
                new Product.Product_varient("V001", "Paperback", 39.99, "https://salt.tikicdn.com/cache/750x750/ts/product/b8/41/3c/30ea9e85a0944d7d548b330420333506.jpg.webp", true, 200L),
                new Product.Product_varient("V002", "Hardcover", 49.99, "https://salt.tikicdn.com/cache/750x750/ts/product/d4/30/1f/b7c3b7a858813afc6cb9b2bc4536ae42.jpg.webp", true, 30L),
                new Product.Product_varient("V003", "Book1", 50, "https://salt.tikicdn.com/cache/750x750/ts/product/be/b3/b6/75d2e20bc71ae1e8eb0922fd266dbbf8.jpg.webp", true, 30L)
        ));


        productRepository.save(product1);
        productRepository.save(product2);

        return ResponseEntity.ok("Products saved successfully");
    }


    @GetMapping("/{id}")
    public ResponseEntity<ResProductDetailsDto> getProduct(@PathVariable String id) {
        Optional<Product> productOptional = productService.findById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            ResProductDetailsDto resProductDetailsDto = productMapper.toResProductDetailsDto(product);
            return ResponseEntity.ok(resProductDetailsDto);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/varient/{id}")
    public ResponseEntity<ResProductVarientDto> getProductVarient(@PathVariable String id) {
        Product product_varient = productService.findProductVarientById(id);
        if (product_varient != null) {
            ResProductVarientDto resProductVarientDto = new ResProductVarientDto();
            resProductVarientDto.setId(product_varient.getId());
            resProductVarientDto.setName(product_varient.getName());
            System.out.println("Product Varients: " + product_varient.getVarients());
            List<ResProductVarientDto.VarientDto> varientDtos = product_varient.getVarients().stream()
                    .filter(varient -> varient.getId().equals(id))
                    .map(varient -> {
                        ResProductVarientDto.VarientDto varientDto = new ResProductVarientDto.VarientDto();
                        varientDto.setId(varient.getId());
                        varientDto.setName(varient.getName());
                        varientDto.setPrice(varient.getPrice());
                        varientDto.setImage(varient.getImage());
                        varientDto.setStock(varient.getStock());
                        varientDto.setAvailable(varient.isAvailable());
                        return varientDto;
                    })
                    .collect(Collectors.toList());
            resProductVarientDto.setVarients(varientDtos);
            System.out.println("Varient: " + resProductVarientDto);
            return ResponseEntity.ok(resProductVarientDto);
        }
        return ResponseEntity.notFound().build();
    }


}
