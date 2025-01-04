package net.javaguides.identity_service.service;

import net.javaguides.identity_service.domain.Permission;
import net.javaguides.identity_service.domain.response.ResResultPaginationDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

/**
 * File: null.java
 * Author: Le Van Hoang
 * Date: 1/4/2025 (04/01/2025)
 * Time: 2:02 PM
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

public interface IPermissionService {
    boolean isPermissionExits(Permission permission);

    Permission createPermission(Permission permission);

    Permission fetchById(UUID id);

    Permission updatePermission(Permission permission);

    void deletePermission(UUID id);

    List<Permission> fetchAll();

    ResResultPaginationDTO fetchAlls(Pageable pageable);

    List<Permission> searchPermissionByModule(String module);

    List<String> getAllModules();
}
