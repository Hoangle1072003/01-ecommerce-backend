package net.javaguides.identity_service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.javaguides.identity_service.domain.Role;
import net.javaguides.identity_service.domain.response.ResResultPaginationDTO;
import net.javaguides.identity_service.service.IRoleService;
import net.javaguides.identity_service.utils.annotation.ApiMessage;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * File: RoleController.java
 * Author: Le Van Hoang
 * Date: 1/4/2025 (04/01/2025)
 * Time: 2:45 PM
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/roles")
public class RoleController {
    private final IRoleService roleService;

    @PostMapping()
    @ApiMessage("Create a new role")
    public ResponseEntity<Role> createRole(@Valid @RequestBody Role role) throws Exception {
        if (roleService.existByName(role.getName())) {
            throw new Exception("Role already exists");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(roleService.createRole(role));
    }

    @PutMapping()
    @ApiMessage("Update a role")
    public ResponseEntity<Role> updateRole(@Valid @RequestBody Role role) throws Exception {
        if (roleService.fetchById(role.getId()) == null) {
            throw new Exception("Role not found");
        }
//        if (roleService.existByName(role.getName())) {
//            throw new Exception("Role already exists");
//        }
        return ResponseEntity.status(HttpStatus.OK).body(roleService.updateRole(role));
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Delete a role")
    public ResponseEntity<Void> deleteRole(@PathVariable("id") UUID id) throws Exception {
        if (roleService.fetchById(id) == null) {
            throw new Exception("Role not found");
        }
        roleService.deleteRole(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{id}")
    @ApiMessage("Get a role")
    public ResponseEntity<Role> getRole(@PathVariable("id") UUID id) throws Exception {
        Role role = roleService.fetchById(id);
        if (role == null) {
            throw new Exception("Role not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(role);
    }

    // select all roles
    @GetMapping()
    @ApiMessage("Get all roles")
    public ResponseEntity<ResResultPaginationDTO> getRoles(
            @RequestParam("current") String current,
            @RequestParam("pageSize") String pageSize
    ) {
        int currentPage = Integer.parseInt(current);
        int size = Integer.parseInt(pageSize);
        Pageable pageable = PageRequest.of(currentPage - 1, size);
        return ResponseEntity.status(HttpStatus.OK).body(roleService.fetchAll(pageable));
    }

    @GetMapping("/all")
    @ApiMessage("Get all roles")
    public ResponseEntity<List<Role>> getAllRoles() {
        return ResponseEntity.status(HttpStatus.OK).body(roleService.getAllRoles());
    }

    @GetMapping("/search")
    @ApiMessage("Search role by name")
    public ResponseEntity<List<Role>> searchRoleByName(
            @RequestParam(value = "keyword", required = false) String keyword
    ) {
        List<Role> roles = roleService.findByName(keyword);
        return ResponseEntity.status(HttpStatus.OK).body(roles);
    }
}
