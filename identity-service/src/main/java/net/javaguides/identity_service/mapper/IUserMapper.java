package net.javaguides.identity_service.mapper;

import net.javaguides.identity_service.domain.User;
import net.javaguides.identity_service.domain.request.ReqUpdateUserDto;
import net.javaguides.identity_service.domain.response.ResCreateUserDTO;
import net.javaguides.identity_service.domain.response.ResUpdateUserDto;
import net.javaguides.identity_service.domain.response.ResUpdateUserPhoneDto;
import net.javaguides.identity_service.domain.response.ResUserDTO;
import org.mapstruct.*;

/**
 * File: null.java
 * Author: Le Van Hoang
 * Date: 1/1/2025 (01/01/2025)
 * Time: 11:25 PM
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface IUserMapper {
    User toEntity(ResCreateUserDTO resCreateUserDTO);

    ResCreateUserDTO toDto(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdate(ResCreateUserDTO resCreateUserDTO, @MappingTarget User user);

    User toEntity(ResUserDTO resUserDto);

    ResUserDTO toDto1(User user);

    ResUpdateUserDto convertToResUpdateUserDto(User user);

    ResUpdateUserPhoneDto convertToResUpdateUserPhoneDto(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdate(ResUserDTO resUserDto, @MappingTarget User user);
}
