package net.javaguides.product_service.service.impl;

import lombok.RequiredArgsConstructor;
import net.javaguides.product_service.repository.IProductRepository;
import net.javaguides.product_service.service.IProductService;
import net.javaguides.product_service.shema.Product;
import net.javaguides.product_service.shema.response.ResProductDetailsDto;
import net.javaguides.product_service.shema.response.ResProductPage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements IProductService {
    private final IProductRepository productRepository;
    private final MongoTemplate mongoTemplate;

    @Override
    public Optional<Product> findById(String id) {
        return productRepository.findById(id);
    }

    @Override
    public Product findProductVarientById(String id) {
        return productRepository.findProductByVarientId(id);
    }

    @Override
    @Transactional
    public void updateProductStock(String productId, String variantId, int quantity) {
        Product product = productRepository.findById(productId).orElse(null);
        if (product != null) {
            Product.Product_varient variant = product.getVarients().stream()
                    .filter(v -> v.getId().equals(variantId))
                    .findFirst()
                    .orElse(null);

            if (variant != null) {
                long updatedStock = variant.getStock() - quantity;
                if (updatedStock >= 0) {
                    variant.setStock(updatedStock);
                    productRepository.save(product);
                    System.out.println("Updated stock for product: " + productId + " with variant: " + variantId);
                } else {
                    System.out.println("Not enough stock for product: " + productId + " with variant: " + variantId);
                }
            } else {
                System.out.println("Variant not found for product: " + productId + " with variantId: " + variantId);
            }
        } else {
            System.out.println("Product not found: " + productId);
        }
    }
    @Override
    public List<Product> getProductsByCategory(String categoryId) {
        return productRepository.findAllByCategoryID(categoryId);
    }

    @Override
    public ResProductPage getAllProductWithPageAndSorting(Integer pageNumber, Integer pageSize, String sortBy, String dir) {
        // Tạo phân trang và sắp xếp
        Sort sort = dir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        // Lấy dữ liệu phân trang
        Page<Product> productPage = productRepository.findAll(pageable);
        return new ResProductPage(
                new ArrayList<>(productPage.getContent()),
                pageNumber,
                pageSize,
                productPage.getTotalElements(),
                productPage.getTotalPages(),
                productPage.isLast()
        );
    }


    @Override
    public ResProductPage productByPrice(Integer pageNumber, Integer pageSize, String sortBy, String dir, Double minPrice, Double maxPrice) {
        // Tạo phân trang và sắp xếp
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(dir.equalsIgnoreCase("asc") ? Sort.Order.asc(sortBy) : Sort.Order.desc(sortBy)));

        // Tạo truy vấn lọc theo giá
        Query query = new Query(Criteria.where("varients.price").gte(minPrice).lte(maxPrice));
        query.with(pageable);
        List<Product> products = mongoTemplate.find(query, Product.class);
        long total = mongoTemplate.count(query, Product.class);
        List<Product> productList = new ArrayList<>(products);
        return new ResProductPage(productList, pageNumber, pageSize, total, (int) Math.ceil((double) total / pageSize), total <= pageNumber * pageSize);
    }

    @Override
    public Product create(ResProductDetailsDto dto) {
        if (productRepository.existsByCode(dto.getCode())) {
            throw new RuntimeException("Product with code " + dto.getCode() + " already exists.");
        }
        Product product = new Product();
        product.setId(dto.getId());
        product.setCode(dto.getCode());
        product.setName(dto.getName());
        product.setBrand(dto.getBrand());
        product.setDescription(dto.getDescription());
        product.setShortDescription(dto.getShortDescription());
        product.setReleaseDate(dto.getReleaseDate().toInstant());
        product.setWeightInGrams(dto.getWeightInGrams());
        product.setCategoryID(dto.getCategory());
        List<Product.Spec> specs = dto.getSpecs().stream()
                .map(specDto -> new Product.Spec(specDto.getK(), specDto.getV(), specDto.getU()))
                .collect(Collectors.toList());
        product.setSpecs(specs);
        List<Product.Product_varient> varients = dto.getVarients().stream()
                .map(varientDto -> new Product.Product_varient(
                        varientDto.getId(),
                        varientDto.getName(),
                        varientDto.getPrice(),
                        varientDto.getImage(),
                        varientDto.isAvailable(),
                        varientDto.getStock()
                ))
                .collect(Collectors.toList());
        product.setVarients(varients);

        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(String id) {
        productRepository.deleteById(id);
    }

}
