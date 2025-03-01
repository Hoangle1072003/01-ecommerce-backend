package net.javaguides.product_service.repository;

import net.javaguides.product_service.shema.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;




@Repository
public interface IProductRepository extends MongoRepository<Product, String> {
    @Query("{ 'varients.id' : ?0 }")
    Product findProductByVarientId(String varientId);
    List<Product> findAllByCategoryID(String categoryID);
    @Query("{ 'varients.price' : { $gte: ?0, $lte: ?1 } }")
    Page<Product> findByPriceProductBetween(Double minPrice, Double maxPrice, Pageable pageable);
    boolean existsByCode(String code);

    @Query("{ $or: [ "
            + "{ 'name' : { $regex: ?0, $options: 'i' } }, "
            + "{ 'brand' : { $regex: ?0, $options: 'i' } }, "
            + "{ 'varients.price' : { $eq: ?1 } } "
            + "] }")
    Page<Product> searchProductsByKeywordWithPagination(String keyword, Double price, Pageable pageable);
}
