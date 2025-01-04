package net.javaguides.identity_service.service.impl;

import lombok.RequiredArgsConstructor;
import net.javaguides.identity_service.domain.User;

import net.javaguides.identity_service.domain.response.ResResultPaginationDTO;
import net.javaguides.identity_service.repository.IUserRepository;
import net.javaguides.identity_service.service.IUserService;
import net.javaguides.identity_service.utils.constant.StatusEnum;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.List;
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

    @Override
    public User handleUser(User user) {
        return null;
    }

    @Override
    public void deleteUser(UUID id) {

    }

    @Override
    public ResResultPaginationDTO getAllUsers(Pageable pageable) {
        return null;
    }

    @Override
    public ResResultPaginationDTO getAllUsersIsNull(Pageable pageable) {
        return null;
    }

    @Override
    public List<User> findByUserIsNullNameOrEmail(String keyword) {
        return List.of();
    }

    @Override
    public User handleUpdateUser(User user) {
        return null;
    }

    @Override
    public boolean isEmailExist(String email) {
        return false;
    }

    @Override
    public User getUserById(UUID id) {
        return null;
    }

    @Override
    public User handleGetUserByUserName(String userName) {
        return userRepository.findByEmail(userName);
    }

    @Override
    public void updateUserToken(String email, String token) {

    }

    @Override
    public User getUserByRefreshToken(String token, String email) {
        return null;
    }

    @Override
    public List<User> findByNameOrActive(String name, StatusEnum status, UUID roleId) {
        return List.of();
    }
}
