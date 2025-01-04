package net.javaguides.identity_service.mapper;

import net.javaguides.identity_service.entity.Permission;
import net.javaguides.identity_service.entity.response.ResPermissionDTO;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface IPermissionMapper {
    Permission toEntity(ResPermissionDTO resPermissionDTO);

    ResPermissionDTO toDto(Permission permission);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Permission partialUpdate(ResPermissionDTO resPermissionDTO, @MappingTarget Permission permission);
}