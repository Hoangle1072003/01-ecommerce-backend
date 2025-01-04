package net.javaguides.identity_service.mapper;

import net.javaguides.identity_service.domain.User;
import net.javaguides.identity_service.domain.request.ReqUserCreateDto;
import org.mapstruct.*;

/**
 * File: null.java
 * Author: Le Van Hoang
 * Date: 1/1/2025 (01/01/2025)
 * Time: 11:29 PM
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface IUserCreateMapper {
    User toEntity(ReqUserCreateDto reqUserCreateDto);

    ReqUserCreateDto toDto(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdate(ReqUserCreateDto reqUserCreateDto, @MappingTarget User user);
}
