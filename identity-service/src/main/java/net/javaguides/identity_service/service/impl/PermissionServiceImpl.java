package net.javaguides.identity_service.service.impl;

import lombok.RequiredArgsConstructor;
import net.javaguides.identity_service.domain.Permission;
import net.javaguides.identity_service.domain.response.ResMeta;
import net.javaguides.identity_service.domain.response.ResResultPaginationDTO;
import net.javaguides.identity_service.repository.IPermissionRepository;
import net.javaguides.identity_service.service.IPermissionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * File: PermissionService.java
 * Author: Le Van Hoang
 * Date: 1/4/2025 (04/01/2025)
 * Time: 2:04 PM
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements IPermissionService {
    private final IPermissionRepository permissionRepository;

    @Override
    public boolean isPermissionExits(Permission permission) {
        return permissionRepository.existsByModuleAndApiPathAndMethod(
                permission.getModule(),
                permission.getApiPath(),
                permission.getMethod()
        );
    }

    @Override
    public Permission createPermission(Permission permission) {
        return permissionRepository.save(permission);
    }

    @Override
    public Permission fetchById(UUID id) {
        return permissionRepository.findById(id).orElse(null);
    }

    @Override
    public Permission updatePermission(Permission permission) {
        Permission oldPermission = fetchById(permission.getId());
        if (oldPermission != null) {
            oldPermission.setName(permission.getName());
            oldPermission.setApiPath(permission.getApiPath());
            oldPermission.setMethod(permission.getMethod());
            oldPermission.setModule(permission.getModule());
            return permissionRepository.save(oldPermission);
        }
        return null;
    }

    @Override
    public void deletePermission(UUID id) {
        Optional<Permission> permission = permissionRepository.findById(id);
        Permission currentPermission = permission.orElse(null);
        currentPermission.getRoles().forEach(role -> role.getPermissions().remove(currentPermission));
        permissionRepository.delete(currentPermission);
    }

    @Override
    public List<Permission> fetchAll() {
        return permissionRepository.findAll();
    }

    @Override
    public ResResultPaginationDTO fetchAlls(Pageable pageable) {
        Pageable sortedByCreatedAt = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("createdAt").descending());
        Page<Permission> pagePermissions = permissionRepository.findAll(sortedByCreatedAt);

        return new ResResultPaginationDTO(
                new ResMeta(
                        pagePermissions.getNumber() + 1,
                        pagePermissions.getSize(),
                        pagePermissions.getTotalPages(),
                        pagePermissions.getTotalElements()
                ),
                pagePermissions.getContent()
        );
    }

    @Override
    public List<Permission> searchPermissionByModule(String module) {
        List<Permission> permissions = permissionRepository.findByModule(module);

        return permissions;
    }

    @Override
    public List<String> getAllModules() {
        List<Permission> permissions = permissionRepository.findAll();
        if (permissions.isEmpty()) {
            return Collections.emptyList();
        }

        return permissions.stream()
                .map(Permission::getModule)
                .distinct()
                .collect(Collectors.toList());
    }
}
