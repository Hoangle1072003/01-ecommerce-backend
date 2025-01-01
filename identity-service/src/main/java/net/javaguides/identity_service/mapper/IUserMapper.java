package net.javaguides.identity_service.mapper;

import net.javaguides.identity_service.entity.User;
import net.javaguides.identity_service.entity.request.ReqUserDto;
import net.javaguides.identity_service.entity.response.ResUserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * File: null.java
 * Author: Le Van Hoang
 * Date: 12/27/2024 (27/12/2024)
 * Time: 10:47 AM
 * Version: 1.0
 * <p>
 * Copyright © 2024 Le Van Hoang. All rights reserved.
 */

@Mapper(componentModel = "spring")
public interface IUserMapper {
    User toUser(ReqUserDto reqUserDto);

    ResUserDto toResUserDto(User user);
}
