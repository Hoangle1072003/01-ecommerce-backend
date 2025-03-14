package net.javaguides.identity_service.service;

import net.javaguides.identity_service.domain.User;
import net.javaguides.identity_service.domain.request.*;
import net.javaguides.identity_service.domain.response.ResResultPaginationDTO;
import net.javaguides.identity_service.domain.response.ResUpdateUserDto;
import net.javaguides.identity_service.domain.response.ResUpdateUserPhoneDto;
import net.javaguides.identity_service.utils.constant.AuthProvider;
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

    User handleGetUserByUserNameByLocal(String userName, AuthProvider authProvider);

    void updateUserToken(String email, String token);

    User getUserByRefreshToken(String token, String email);

    List<User> findByNameOrActive(String name, StatusEnum status, UUID roleId);

    boolean activateUserByEmail(String email);

    User saveUserByGoogle(ReqUserGoogleDto reqUserGoogleDto);

    User saveUserByGithub(ReqUserGoogleDto reqUserGoogleDto);

    String resetPassword(User user);

    Void resetPasswordConfirm(ReqResetPasswordDto reqResetPasswordDto);

    ResUpdateUserDto updateUserClient(ReqUpdateUserDto reqUpdateUserDto);

    ResUpdateUserPhoneDto updateUserPhone(ReqUpdateUserPhoneDto reqUpdateUserPhoneDto);

    Void cancelAccount(ReqCancelAccountDto reqCancelAccountDto);

    Void cancelAccountOTP(ReqCancelDto reqCancelDto);

    Void suspendAccount(ReqSuspendAccountDto reqSuspendAccountDto);

    Void activeAccountSuspend(ReqActiveAccountSuspendDto reqSuspendDto);

    Void activeAccountSuspendOTP(ReqActiveAccountSuspendOTPDto reqActiveAccountSuspendOTPDto);

    Object checkAccountSuspend(ReqCheckAccountSuspendDto reqCheckAccountSuspendDto);
}
