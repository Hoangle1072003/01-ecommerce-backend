package net.javaguides.product_service.controller;

import lombok.RequiredArgsConstructor;
import net.javaguides.event.dto.CartItemClientEvent;
import net.javaguides.product_service.mapper.IProductMapper;
import net.javaguides.product_service.repository.IProductRepository;
import net.javaguides.product_service.service.IExcelService;
import net.javaguides.product_service.service.IProductService;
import net.javaguides.product_service.shema.Product;
import net.javaguides.product_service.shema.response.ResProductDetailsDto;
import net.javaguides.product_service.shema.response.ResProductDto;
<<<<<<< HEAD
import net.javaguides.product_service.shema.response.ResProductPage;
import net.javaguides.product_service.shema.response.ResProductVarientDto;
import net.javaguides.product_service.utils.constant.AppConstants;
=======
import net.javaguides.product_service.shema.response.ResProductRecentlyDto;
import net.javaguides.product_service.shema.response.ResProductVarientDto;
import net.javaguides.product_service.utils.annotation.ApiMessage;
>>>>>>> 06360374641f4396b6829b3a5d11830cf1587668
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
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
    private final IExcelService excelService;


    @GetMapping()
    public ResponseEntity<List<ResProductDto>> getProducts() {
        List<Product> products = productRepository.findAll();
        List<ResProductDto> resProductDtos = products.stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resProductDtos);
    }

//    @PostMapping()
//    public ResponseEntity<String> saveProduct() {
//        Product product1 = new Product();
//        product1.setCode("P001");
//        product1.setName("Men's T-Shirt");
//        product1.setBrand("Brand A");
//        product1.setDescription("A comfortable cotton T-shirt for men.");
//        product1.setShortDescription("Comfortable cotton material, ideal for daily wear.");
//        product1.setReleaseDate(new Date().toInstant());
//        product1.setWeightInGrams(200.0);
//        product1.setSpecs(Arrays.asList(
//                new Product.Spec("Size", "M", "cm"),
//                new Product.Spec("Color", "Red", "cm")
//        ));
//        product1.setVarients(Arrays.asList(
//                new Product.Product_varient("V001", "M Red", 19.99, "https://salt.tikicdn.com/cache/750x750/ts/product/22/06/1c/aebf0f634e74c05b2d8282b2a3f2b1d5.png.webp", true, 100L),
//                new Product.Product_varient("V002", "L Red", 22.99, "https://salt.tikicdn.com/cache/750x750/ts/product/8d/16/3e/9242556bf1de5fcdf30aec762ca9f51b.jpg.webp", true, 50L)
//        ));
//
//        Product product2 = new Product();
//        product2.setCode("P002");
//        product2.setName("Java Programming Book");
//        product2.setBrand("Tech Books");
//        product2.setDescription("A comprehensive guide to Java programming.");
//        product2.setShortDescription("A complete guide for mastering Java programming language.");
//        product2.setReleaseDate(new Date().toInstant());
//        product2.setWeightInGrams(500.0);
//        product2.setSpecs(Arrays.asList(
//                new Product.Spec("Author", "John Doe", "String"),
//                new Product.Spec("Pages", "350", "pages")
//        ));
//        product2.setVarients(Arrays.asList(
//                new Product.Product_varient("V001", "Paperback", 39.99, "https://salt.tikicdn.com/cache/750x750/ts/product/b8/41/3c/30ea9e85a0944d7d548b330420333506.jpg.webp", true, 200L),
//                new Product.Product_varient("V002", "Hardcover", 49.99, "https://salt.tikicdn.com/cache/750x750/ts/product/d4/30/1f/b7c3b7a858813afc6cb9b2bc4536ae42.jpg.webp", true, 30L),
//                new Product.Product_varient("V003", "Book1", 50, "https://salt.tikicdn.com/cache/750x750/ts/product/be/b3/b6/75d2e20bc71ae1e8eb0922fd266dbbf8.jpg.webp", true, 30L)
//        ));
//
//
//        productRepository.save(product1);
//        productRepository.save(product2);
//
//        return ResponseEntity.ok("Products saved successfully");
//    }


    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            if (!file.getOriginalFilename().endsWith(".xlsx") && !file.getOriginalFilename().endsWith(".xls")) {
                return ResponseEntity.badRequest().body("Tệp không phải là tệp Excel");
            }
            excelService.processFile(file);
            return ResponseEntity.ok("Tệp đã được tải lên thành công");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Không thể tải lên tệp: " + file.getOriginalFilename() + "!");
        }
    }
<<<<<<< HEAD
    @GetMapping("/{id}")
    public ResponseEntity<ResProductDetailsDto> getProduct(@PathVariable String id) {
=======


    @GetMapping("/{userId}/{id}")
    public ResponseEntity<ResProductDetailsDto> getProduct(
            @PathVariable(required = false) String userId,
            @PathVariable String id) {

        if (userId != null && !userId.equals("null")) {
            System.out.println("UserID: [" + userId + "]");
        } else {
            System.out.println("UserID is missing!");
            userId = null;
        }

>>>>>>> 06360374641f4396b6829b3a5d11830cf1587668
        Optional<Product> productOptional = productService.findById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            ResProductDetailsDto resProductDetailsDto = productMapper.toResProductDetailsDto(product);

            if (userId != null) {
                productService.saveRecentlyViewedProduct(userId, productOptional);
            }

            return ResponseEntity.ok(resProductDetailsDto);
        }

        return ResponseEntity.notFound().build();
    }

<<<<<<< HEAD
    @GetMapping("/category")
    public ResponseEntity<List<Product>> getProductsByCategory(@RequestParam String categoryID) {
        {
            List<Product> products = productService.getProductsByCategory(categoryID);
            return ResponseEntity.ok(products);
        }
    }
    @GetMapping("/allProductPage")
    public ResponseEntity<ResProductPage> getProducts(
            @RequestParam(defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
            @RequestParam(defaultValue = AppConstants.SORT_DIR, required = false) String dir,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice
    ) {
        ResProductPage result;

        if (minPrice != null && maxPrice != null) {
            result = productService.productByPrice(pageNumber, pageSize, sortBy, dir, minPrice, maxPrice);
        } else {
            result = productService.getAllProductWithPageAndSorting(pageNumber, pageSize, sortBy, dir);
        }

        return ResponseEntity.ok(result);
    }
    @GetMapping("/search")
    public ResponseEntity<ResProductPage> searchProducts(
            @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(value = "price", required = false) Double price,
            @RequestParam(defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize) {

        ResProductPage result = productService.searchProducts(keyword, price, pageNumber, pageSize);
        return ResponseEntity.ok(result);
    }
=======
>>>>>>> 06360374641f4396b6829b3a5d11830cf1587668

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

    @GetMapping("/recently-viewed/{userId}")
    @ApiMessage("Get recently viewed products")
    public ResponseEntity<List<ResProductRecentlyDto>> getRecentlyViewedProducts(@PathVariable String userId) {
        List<ResProductRecentlyDto> products = productService.getRecentlyViewedProducts(userId);
        return ResponseEntity.ok(products);
    }


}
