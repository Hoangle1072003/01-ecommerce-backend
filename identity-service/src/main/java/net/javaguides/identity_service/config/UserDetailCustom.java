package net.javaguides.identity_service.config;

import lombok.RequiredArgsConstructor;
import net.javaguides.identity_service.domain.User;
import net.javaguides.identity_service.service.IUserService;
import net.javaguides.identity_service.utils.constant.AuthProvider;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * File: UserDetailCustom.java
 * Author: Le Van Hoang
 * Date: 1/1/2025 (01/01/2025)
 * Time: 11:55 PM
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Component("userDetailsService")
@RequiredArgsConstructor
public class UserDetailCustom implements UserDetailsService {
    private final IUserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.handleGetUserByUserName(username);
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), Collections.singletonList(new SimpleGrantedAuthority(user.getRole().getName())));

    }
}

