package net.javaguides.identity_service.service;

import net.javaguides.identity_service.entity.request.ReqUserDto;
import net.javaguides.identity_service.entity.response.ResUserDto;

import java.util.UUID;

/**
 * File: null.java
 * Author: Le Van Hoang
 * Date: 12/27/2024 (27/12/2024)
 * Time: 9:57 AM
 * Version: 1.0
 * <p>
 * Copyright © 2024 Le Van Hoang. All rights reserved.
 */

public interface IUserService {
    ReqUserDto createUser(ReqUserDto reqUserDto);

    ResUserDto getUserById(UUID id);
}
