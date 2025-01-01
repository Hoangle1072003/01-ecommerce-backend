package net.javaguides.identity_service.service.impl;

import lombok.RequiredArgsConstructor;
import net.javaguides.identity_service.entity.User;
import net.javaguides.identity_service.entity.request.ReqUserDto;
import net.javaguides.identity_service.entity.response.ResUserDto;
import net.javaguides.identity_service.mapper.IUserMapper;

import net.javaguides.identity_service.repository.IUserRepository;
import net.javaguides.identity_service.service.IUserService;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * File: UserServiceImpl.java
 * Author: Le Van Hoang
 * Date: 12/27/2024 (27/12/2024)
 * Time: 9:58 AM
 * Version: 1.0
 * <p>
 * Copyright © 2024 Le Van Hoang. All rights reserved.
 */

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {
    private final IUserRepository userRepository;
    private final IUserMapper userMapper;

    @Override
    public ReqUserDto createUser(ReqUserDto user) {
        User userEntity = userMapper.toUser(user);
        userRepository.save(userEntity);
        return user;
    }

    @Override
    public ResUserDto getUserById(UUID id) {
        User user = userRepository.findById(id).get();
        return userMapper.toResUserDto(user);
    }
}
