package net.javaguides.identity_service.service;

import net.javaguides.identity_service.domain.User;
import net.javaguides.identity_service.domain.request.ReqUserGoogleDto;
import net.javaguides.identity_service.domain.response.ResResultPaginationDTO;
import net.javaguides.identity_service.utils.constant.StatusEnum;
import org.springframework.data.domain.Pageable;

import java.util.List;
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
    User handleUser(User user);

    void deleteUser(UUID id);

    // con dung dc
    ResResultPaginationDTO getAllUsers(Pageable pageable);

    ResResultPaginationDTO getAllUsersIsNull(Pageable pageable);

    List<User> findByUserIsNullNameOrEmail(String keyword);

    User handleUpdateUser(User user);

    boolean isEmailExist(String email);

    User getUserById(UUID id);

    User handleGetUserByUserName(String userName);

    void updateUserToken(String email, String token);

    User getUserByRefreshToken(String token, String email);

    List<User> findByNameOrActive(String name, StatusEnum status, UUID roleId);

    boolean activateUserByEmail(String email);

    User saveUserByGoogle(ReqUserGoogleDto reqUserGoogleDto);
}
