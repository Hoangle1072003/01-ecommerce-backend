package net.javaguides.product_service.mapper;

import net.javaguides.product_service.shema.Product;
import net.javaguides.product_service.shema.response.ResProductDetailsDto;
import net.javaguides.product_service.shema.response.ResProductDto;
import net.javaguides.product_service.shema.response.ResProductVarientDto;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface IProductMapper {
    Product toEntity(ResProductDto resProductDto);

    ResProductDto toDto(Product product);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Product partialUpdate(ResProductDto resProductDto, @MappingTarget Product product);

    Product toProductEntityFromDetails(ResProductDetailsDto resProductDetailsDto);

    ResProductDetailsDto toResProductDetailsDto(Product product);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Product updateProductFromResProductDetailsDto(ResProductDetailsDto resProductDetailsDto, @MappingTarget Product product);


    ResProductVarientDto toResProductVarientDto(Product productVarient);

}