package net.javaguides.identity_service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javaguides.identity_service.domain.Permission;
import net.javaguides.identity_service.domain.response.PermissionsResponseDTO;
import net.javaguides.identity_service.domain.response.ResPermissionDTO;
import net.javaguides.identity_service.domain.response.ResResultPaginationDTO;
import net.javaguides.identity_service.mapper.IPermissionMapper;
import net.javaguides.identity_service.service.IPermissionService;
import net.javaguides.identity_service.utils.annotation.ApiMessage;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * File: PermissionController.java
 * Author: Le Van Hoang
 * Date: 1/4/2025 (04/01/2025)
 * Time: 2:00 PM
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Slf4j
@RequestMapping("/api/v1/permissions")
@RequiredArgsConstructor
@RestController()
public class PermissionController {
    private final IPermissionService permissionService;
    private final IPermissionMapper permissionMapper;

    @PostMapping()
    @ApiMessage("Create permission")
    public ResponseEntity<Permission> createPermission(
            @Valid @RequestBody Permission permission
    ) throws Exception {
        if (permissionService.isPermissionExits(permission)) {
            throw new Exception("Permission already exists");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(permissionService.createPermission(permission));
    }

    @PutMapping()
    @ApiMessage("Update permission")
    public ResponseEntity<Permission> updatePermission(
            @Valid @RequestBody Permission permission
    ) throws Exception {
        if (permissionService.fetchById(permission.getId()) == null) {
            throw new Exception("Permission not found");
        }
        if (permissionService.isPermissionExits(permission)) {
            throw new Exception("Permission already exists");
        }
        return ResponseEntity.status(HttpStatus.OK).body(permissionService.updatePermission(permission));
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Delete permission")
    public ResponseEntity<Void> deletePermission(
            @PathVariable("id") UUID id
    ) throws Exception {
        if (permissionService.fetchById(id) == null) {
            throw new Exception("Permission not found");
        }
        permissionService.deletePermission(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping()
    @ApiMessage("Get all permissions")
    public ResponseEntity<PermissionsResponseDTO> getAllPermissions(

    ) {

        List<Permission> permissions = permissionService.fetchAll();

        Map<String, Map<String, List<ResPermissionDTO>>> transformedPermissions = permissions.stream()
                .map(permissionMapper::toDto)
                .collect(Collectors.groupingBy(
                        resPermissionDto -> resPermissionDto.getModule(),
                        Collectors.groupingBy(
                                resPermissionDto -> resPermissionDto.getName().split(" ")[0]
                        )
                ));

        PermissionsResponseDTO responseDTO = new PermissionsResponseDTO(transformedPermissions);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @GetMapping("/list")
    @ApiMessage("Get all permissions")
    public ResponseEntity<ResResultPaginationDTO> getAll(
            @RequestParam("page") String current,
            @RequestParam("size") String pageSize
    ) {
        int page = Integer.parseInt(current);
        int size = Integer.parseInt(pageSize);
        Pageable pageable = PageRequest.of(page - 1, size);
        return ResponseEntity.status(HttpStatus.OK).body(permissionService.fetchAlls(pageable));
    }

    @GetMapping("/search")
    @ApiMessage("Search permission by module")
    public ResponseEntity<List<Permission>> searchPermissionByModule(
            @RequestParam("module") String module) {
        return ResponseEntity.status(HttpStatus.OK).body(permissionService.searchPermissionByModule(module));
    }

    // get all  module
    @GetMapping("/modules")
    @ApiMessage("Get all modules")
    public ResponseEntity<List<String>> getAllModules() {
        return ResponseEntity.status(HttpStatus.OK).body(permissionService.getAllModules());
    }
}
