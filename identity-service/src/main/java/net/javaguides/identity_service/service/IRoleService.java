package net.javaguides.identity_service.service;

import net.javaguides.identity_service.domain.Role;
import net.javaguides.identity_service.domain.response.ResResultPaginationDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

/**
 * File: null.java
 * Author: Le Van Hoang
 * Date: 1/4/2025 (04/01/2025)
 * Time: 2:40 PM
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

public interface IRoleService {
    boolean existByName(String name);

    Role createRole(Role role);

    Role fetchById(UUID id);

    Role updateRole(Role role);

    void deleteRole(UUID id);

    ResResultPaginationDTO fetchAll(Pageable pageable);

    List<Role> getAllRoles();

    List<Role> findByName(String name);

    Role findByNameRole(String name);
}
