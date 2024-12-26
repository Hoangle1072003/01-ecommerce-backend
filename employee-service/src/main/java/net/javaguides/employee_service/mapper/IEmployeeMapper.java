package net.javaguides.employee_service.mapper;

import net.javaguides.employee_service.entity.Employee;
import net.javaguides.employee_service.entity.request.ReqEmployeeDto;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface IEmployeeMapper {
    Employee toEntity(ReqEmployeeDto reqEmployeeDto);

    ReqEmployeeDto toDto(Employee employee);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Employee partialUpdate(ReqEmployeeDto reqEmployeeDto, @MappingTarget Employee employee);
}