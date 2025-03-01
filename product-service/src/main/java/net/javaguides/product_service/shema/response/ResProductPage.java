package net.javaguides.product_service.shema.response;

import net.javaguides.product_service.shema.Product;

import java.util.List;

public record ResProductPage (List<Product> products, Integer pageNumber
		, Integer pageSize , long totalElements, int totalPages, boolean islast) {

}
