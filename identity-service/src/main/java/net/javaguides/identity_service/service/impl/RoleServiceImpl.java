package net.javaguides.identity_service.service.impl;

import lombok.RequiredArgsConstructor;
import net.javaguides.identity_service.domain.Permission;
import net.javaguides.identity_service.domain.Role;
import net.javaguides.identity_service.domain.response.ResMeta;
import net.javaguides.identity_service.domain.response.ResResultPaginationDTO;
import net.javaguides.identity_service.repository.IPermissionRepository;
import net.javaguides.identity_service.repository.IRoleRepository;
import net.javaguides.identity_service.service.IRoleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * File: IRoleServiceImpl.java
 * Author: Le Van Hoang
 * Date: 1/4/2025 (04/01/2025)
 * Time: 2:42 PM
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements IRoleService {
    private final IRoleRepository roleRepository;
    private final IPermissionRepository permissionRepository;


    @Override
    public boolean existByName(String name) {
        return roleRepository.existsByName(name);
    }

    @Override
    public Role createRole(Role role) {
        if (role.getPermissions() != null) {
            List<UUID> requiredPermissionIds = role.getPermissions()
                    .stream().map(x -> x.getId())
                    .collect(Collectors.toList());
            List<Permission> permissions = permissionRepository.findByIdIn(requiredPermissionIds);
            role.setPermissions(permissions);
        }
        return roleRepository.save(role);
    }

    @Override
    public Role fetchById(UUID id) {
        return roleRepository.findById(id).orElse(null);
    }

    @Override
    public Role updateRole(Role role) {
        Role oldRole = fetchById(role.getId());
        if (role.getPermissions() != null) {
            List<UUID> requiredPermissionIds = role.getPermissions()
                    .stream().map(x -> x.getId())
                    .collect(Collectors.toList());
            List<Permission> permissions = permissionRepository.findByIdIn(requiredPermissionIds);
            oldRole.setPermissions(permissions);
        }
        if (oldRole != null) {
            oldRole.setName(role.getName());
            oldRole.setDescription(role.getDescription());
            oldRole.setActive(role.getActive());
            oldRole.setPermissions(role.getPermissions());
            return roleRepository.save(oldRole);
        }
        return null;
    }

    @Override
    public void deleteRole(UUID id) {
        roleRepository.deleteById(id);
    }

    @Override
    public ResResultPaginationDTO fetchAll(Pageable pageable) {
        Page<Role> pageRoles = roleRepository.findAll(pageable);
        return new ResResultPaginationDTO(
                new ResMeta(
                        pageRoles.getNumber() + 1,
                        pageRoles.getSize(),
                        pageRoles.getTotalPages(),
                        pageRoles.getTotalElements()
                )
                , pageRoles.getContent()
        )
                ;
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public List<Role> findByName(String name) {
        return roleRepository.findAllByNameContaining(name);
    }

    @Override
    public Role findByNameRole(String name) {
        return roleRepository.findByName(name);
    }
}
