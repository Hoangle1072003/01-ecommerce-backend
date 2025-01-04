package net.javaguides.identity_service.config;

import lombok.RequiredArgsConstructor;
import net.javaguides.identity_service.domain.Permission;
import net.javaguides.identity_service.domain.Role;
import net.javaguides.identity_service.domain.User;
import net.javaguides.identity_service.repository.IPermissionRepository;
import net.javaguides.identity_service.repository.IRoleRepository;
import net.javaguides.identity_service.repository.IUserRepository;
import net.javaguides.identity_service.utils.constant.GenderEnum;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * File: DatabaseInitializer.java
 * Author: Le Van Hoang
 * Date: 1/1/2025 (01/01/2025)
 * Time: 11:16 PM
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */
@Service
@RequiredArgsConstructor
public class DatabaseInitializer implements CommandLineRunner {
    private static final String ADMIN_EMAIL = "admin@gmail.com";
    private static final String ADMIN_PASSWORD = "123456";
    private static final String ADMIN_NAME = "I'm super admin";
    private static final String ADMIN_ROLE = "SUPER_ADMIN";

    private static final String USER_NAME = "I'm user";
    private static final String USER_EMAIL = "user@gmail.com";
    private static final String USER_PASSWORD = "123456";
    private static final Integer USER_AGE = 20;
    private static final String USER_PHONE = "0123456789";
    private static final String USER_ADDRESS = "HCM";
    private static final GenderEnum USER_GENDER = GenderEnum.MALE;

    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final IPermissionRepository permissionRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public void run(String... args) throws Exception {
        System.out.println("DatabaseInitializer.run");
        long userCount = userRepository.count();
        long roleCount = roleRepository.count();
        long permissionCount = permissionRepository.count();

        if (permissionCount == 0) {
            System.out.println("=====>DatabaseInitializer.run: Insert permission");
            ArrayList<Permission> arrPermissions = new ArrayList<>();
            arrPermissions.add(new Permission("Create a permission", "/api/v1/permissions", "POST", "PERMISSIONS"));
            arrPermissions.add(new Permission("Update a permission", "/api/v1/permissions", "PUT", "PERMISSIONS"));
            arrPermissions.add(new Permission("Delete a permission", "/api/v1/permissions/{id}", "DELETE", "PERMISSIONS"));
            arrPermissions.add(new Permission("Get all permissions", "/api/v1/permissions", "GET", "PERMISSIONS"));
            arrPermissions.add(new Permission("Get all list permission", "/api/v1/permissions/list", "GET", "PERMISSIONS"));
            arrPermissions.add(new Permission("Search permission by module", "/api/v1/permissions/search", "GET", "PERMISSIONS"));
            arrPermissions.add(new Permission("Get all module", "/api/v1/permissions/modules", "GET", "PERMISSIONS"));


            arrPermissions.add(new Permission("Create a role", "/api/v1/roles", "POST", "ROLES"));
            arrPermissions.add(new Permission("Update a role", "/api/v1/roles", "PUT", "ROLES"));
            arrPermissions.add(new Permission("Delete a role", "/api/v1/roles/{id}", "DELETE", "ROLES"));
            arrPermissions.add(new Permission("Get a role", "/api/v1/roles/{id}", "GET", "ROLES"));
            arrPermissions.add(new Permission("Get all roles", "/api/v1/roles", "GET", "ROLES"));
            arrPermissions.add(new Permission("Get all roles combobox", "/api/v1/roles/all", "GET", "ROLES"));
            arrPermissions.add(new Permission("Search role by name", "/api/v1/roles/search", "GET", "ROLES"));

            arrPermissions.add(new Permission("Create a user", "/api/v1/user/create", "POST", "USERS"));
            arrPermissions.add(new Permission("Update a user", "/api/v1/user", "PUT", "USERS"));
            arrPermissions.add(new Permission("Delete a user", "/api/v1/user/delete/{id}", "DELETE", "USERS"));
            arrPermissions.add(new Permission("Get a user", "/api/v1/user/{id}", "GET", "USERS"));
            arrPermissions.add(new Permission("Get all users", "/api/v1/user", "GET", "USERS"));
            arrPermissions.add(new Permission("Search user by name", "/api/v1/user/search", "GET", "USERS"));
            permissionRepository.saveAll(arrPermissions);
        }
        if (roleCount == 0) {
            System.out.println("=====>DatabaseInitializer.run: Insert role");
            List<Permission> permissions = permissionRepository.findAll();
            Role adminRole = new Role();
            adminRole.setName(ADMIN_ROLE);
            adminRole.setDescription("Super Admin Role with all permissions");
            adminRole.setActive(true);
            adminRole.setPermissions(permissions);
            roleRepository.save(adminRole);


            Role userRole = new Role();
            userRole.setName("USER");
            userRole.setDescription("User Role not have all permissions admin");
            userRole.setActive(true);
            roleRepository.save(userRole);

        }

        User user = new User();
        User adminUser = new User();
        if (userCount == 0) {

            adminUser.setName(ADMIN_NAME);
            adminUser.setEmail(ADMIN_EMAIL);
            adminUser.setPassword(passwordEncoder.encode(ADMIN_PASSWORD));
            adminUser.setAge(20);
            adminUser.setGender(GenderEnum.MALE);
            adminUser.setAddress("HCM");


            user.setName(USER_NAME);
            user.setEmail(USER_EMAIL);
            user.setPassword(passwordEncoder.encode(USER_PASSWORD));
            user.setAge(USER_AGE);
            user.setPhone(USER_PHONE);
            user.setAddress(USER_ADDRESS);
            user.setGender(USER_GENDER);
            user.setRole(roleRepository.findByName("USER"));


            Role adminRole = roleRepository.findByName(ADMIN_ROLE);
            if (adminRole != null) {
                adminUser.setRole(adminRole);
            }
            userRepository.save(adminUser);
            userRepository.save(user);
        }
        if (permissionCount > 0 || roleCount > 0 || userCount > 0) {
            System.out.println("=====>DatabaseInitializer.run: Database is already initialized");
        } else {
            System.out.println("=====>DatabaseInitializer.run: Database is initialized");
        }

    }
}
