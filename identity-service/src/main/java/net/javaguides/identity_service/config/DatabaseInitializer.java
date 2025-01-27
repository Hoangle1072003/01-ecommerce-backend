package net.javaguides.identity_service.config;

import lombok.RequiredArgsConstructor;
import net.javaguides.identity_service.domain.Role;
import net.javaguides.identity_service.domain.User;
import net.javaguides.identity_service.repository.IRoleRepository;
import net.javaguides.identity_service.repository.IUserRepository;
import net.javaguides.identity_service.utils.constant.GenderEnum;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    private static final String ADMIN_ROLE = "ADMIN";

    private static final String ADMIN_TEST_EMAIL = "clientassist.office@gmail.com";
    private static final String ADMIN_TEST_PASSWORD = "123456";
    private static final String ADMIN_TEST_NAME = "I'm admin test";

    private static final String USER_NAME = "I'm user";
    private static final String USER_EMAIL = "user@gmail.com";
    private static final String USER_PASSWORD = "123456";
    private static final Integer USER_AGE = 20;
    private static final String USER_PHONE = "0123456789";
    private static final String USER_ADDRESS = "HCM";
    private static final GenderEnum USER_GENDER = GenderEnum.MALE;
    private static final String USER_ROLE = "USER";


    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public void run(String... args) throws Exception {
        System.out.println("DatabaseInitializer.run");
        long userCount = userRepository.count();
        long roleCount = roleRepository.count();


        if (roleCount == 0) {
            System.out.println("=====>DatabaseInitializer.run: Insert role");
            Role adminRole = new Role();
            adminRole.setName(ADMIN_ROLE);
            adminRole.setDescription("Super Admin Role with all permissions");
            adminRole.setActive(true);
            roleRepository.save(adminRole);


            Role userRole = new Role();
            userRole.setName(USER_ROLE);
            userRole.setDescription("User Role not have all permissions admin");
            userRole.setActive(true);
            roleRepository.save(userRole);

        }

        User user = new User();
        User adminUser = new User();
        User adminTest = new User();
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
            user.setRole(roleRepository.findByName(USER_ROLE));

            adminTest.setName(ADMIN_TEST_NAME);
            adminTest.setEmail(ADMIN_TEST_EMAIL);
            adminTest.setPassword(passwordEncoder.encode(ADMIN_TEST_PASSWORD));
            adminTest.setAge(20);
            adminUser.setGender(GenderEnum.MALE);
            adminUser.setAddress("HCM");


            Role adminRole = roleRepository.findByName(ADMIN_ROLE);
            if (adminRole != null) {
                adminUser.setRole(adminRole);
                adminTest.setRole(adminRole);
            }
            userRepository.save(adminUser);
            userRepository.save(adminTest);
            userRepository.save(user);
        }
        if (roleCount > 0 || userCount > 0) {
            System.out.println("=====>DatabaseInitializer.run: Database is already initialized");
        } else {
            System.out.println("=====>DatabaseInitializer.run: Database is initialized");
        }

    }
}
