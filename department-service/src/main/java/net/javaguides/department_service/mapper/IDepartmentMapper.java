package net.javaguides.department_service.mapper;

import net.javaguides.department_service.entity.Department;
import net.javaguides.department_service.entity.request.ReqDepartmentDto;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface IDepartmentMapper {
    Department toEntity(ReqDepartmentDto reqDepartmentDto);

    ReqDepartmentDto toDto(Department department);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Department partialUpdate(ReqDepartmentDto reqDepartmentDto, @MappingTarget Department department);
}