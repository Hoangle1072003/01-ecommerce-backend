package net.javaguides.identity_service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javaguides.identity_service.domain.User;
import net.javaguides.identity_service.domain.request.ReqUpdateUserDto;
import net.javaguides.identity_service.domain.request.ReqUserGoogleDto;
import net.javaguides.identity_service.domain.response.ResCreateUserDTO;
import net.javaguides.identity_service.domain.response.ResResultPaginationDTO;
import net.javaguides.identity_service.domain.response.ResUpdateUserDto;
import net.javaguides.identity_service.domain.response.ResUserDTO;
import net.javaguides.identity_service.mapper.IUserMapper;
import net.javaguides.identity_service.service.IExcelService;
import net.javaguides.identity_service.service.IUserService;
import net.javaguides.identity_service.utils.annotation.ApiMessage;
import net.javaguides.identity_service.utils.constant.StatusEnum;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

/**
 * File: UserController.java
 * Author: Le Van Hoang
 * Date: 1/4/2025 (04/01/2025)
 * Time: 3:13 PM
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;
    private final PasswordEncoder passwordEncoder;
    private final IUserMapper userMapper;
    private final IExcelService excelService;


    @PostMapping("/create")
    @ApiMessage("Create new user")
    public ResponseEntity<ResCreateUserDTO> createNewUser(@Valid @RequestBody User user) throws Exception {

        boolean isEmailExist = userService.isEmailExist(user.getEmail());
        if (isEmailExist) {
            log.error("Email is already exist");
            throw new Exception("Email is already exist");
        }


        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User newUser = userService.handleUser(user);
        ResCreateUserDTO resCreateUserDTO = userMapper.toDto(newUser);
        log.info("User with id {} has been created", resCreateUserDTO.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(resCreateUserDTO);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") UUID id) {
        if (id == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id is invalid");
        }

        try {
            userService.deleteUser(id);
            return ResponseEntity.status(HttpStatus.OK).body("User with id " + id + " has been deleted");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }


    @GetMapping
    @ApiMessage("Get all users")
    public ResponseEntity<ResResultPaginationDTO> getAllUsers(
            @RequestParam("current") String current,
            @RequestParam("pageSize") String pageSize
    ) {
        int page = Integer.parseInt(current);
        int size = Integer.parseInt(pageSize);
        Pageable pageable = PageRequest.of(page - 1, size);
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUsers(pageable));
    }

    @GetMapping("/user-is-null")
    @ApiMessage("Get all users is By Role USER")
    public ResponseEntity<ResResultPaginationDTO> getAllUsersIsNull(
            @RequestParam("current") String current,
            @RequestParam("pageSize") String pageSize
    ) {
        int page = Integer.parseInt(current);
        int size = Integer.parseInt(pageSize);
        Pageable pageable = PageRequest.of(page - 1, size);
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUsersIsNull(pageable));
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.handleUpdateUser(user));
    }

    @GetMapping("/{id}")
    @ApiMessage("Get user by id")
    public ResponseEntity<ResUserDTO> getUserById(@PathVariable("id") UUID id) throws Exception {
        User user = userService.getUserById(id);
        if (id == null) {
            log.error("Id is invalid");
            throw new Exception("Id is invalid");
        } else if (!user.getId().equals(id) || user == null) {
            log.error("User with id {} not found", id);
            throw new Exception("User with id " + id + " not found");
        } else {
            log.info("User with id {} has been found", id);
            ResUserDTO resUserDto = userMapper.toDto1(user);
            System.out.println(resUserDto);
            return ResponseEntity.status(HttpStatus.OK).body(resUserDto);
        }
    }

    @GetMapping("/search")
    @ApiMessage("Search user by name or status (active)")
    public ResponseEntity<List<User>> searchUserByName(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "status", required = false) StatusEnum status,
            @RequestParam(value = "roleId", required = false) UUID roleId
    ) {
        List<User> users = userService.findByNameOrActive(keyword, status, roleId);
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @GetMapping("/search-role-is-null")
    @ApiMessage("Search user role is null  by name or email")
    public ResponseEntity<List<User>> searchUserByNameOrEmail(
            @RequestParam(value = "keyword", required = false) String keyword
    ) {
        List<User> users = userService.findByUserIsNullNameOrEmail(keyword);
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @PostMapping("/upload")
    @ApiMessage("Upload user")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            if (!file.getOriginalFilename().endsWith(".xlsx") && !file.getOriginalFilename().endsWith(".xls")) {
                return ResponseEntity.badRequest().body("File is not excel file");
            }
            excelService.processFile(file);
            return ResponseEntity.ok("File has been uploaded successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not upload the file: " + file.getOriginalFilename() + "!");
        }
    }

    @PutMapping("/update-user-client")
    @ApiMessage("Update user")
    public ResponseEntity<ResUpdateUserDto> updateUserClient(@RequestBody ReqUpdateUserDto reqUpdateUserDto) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUserClient(reqUpdateUserDto));
    }
}
