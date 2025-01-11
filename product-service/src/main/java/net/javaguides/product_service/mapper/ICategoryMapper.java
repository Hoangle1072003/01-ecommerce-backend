package net.javaguides.product_service.mapper;

import net.javaguides.product_service.shema.Category;
import net.javaguides.product_service.shema.request.ReqCategoryDto;
import net.javaguides.product_service.shema.response.ResCategoryDto;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ICategoryMapper {
    Category toEntity(ResCategoryDto resCategoryDto);

    ResCategoryDto toDto(Category category);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Category partialUpdate(ResCategoryDto resCategoryDto, @MappingTarget Category category);

    Category toEntity(ReqCategoryDto reqCategoryDto);

    ReqCategoryDto toDto1(Category category);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Category partialUpdate(ReqCategoryDto reqCategoryDto, @MappingTarget Category category);
}