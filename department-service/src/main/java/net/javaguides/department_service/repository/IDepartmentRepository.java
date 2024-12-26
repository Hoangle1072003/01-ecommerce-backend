package net.javaguides.department_service.repository;

import net.javaguides.department_service.entity.Department;
import net.javaguides.department_service.entity.request.ReqDepartmentDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * File: null.java
 * Author: Le Van Hoang
 * Date: 12/26/2024 (26/12/2024)
 * Time: 6:57 AM
 * Version: 1.0
 * <p>
 * Copyright © 2024 Le Van Hoang. All rights reserved.
 */

@Repository
public interface IDepartmentRepository extends JpaRepository<Department, UUID> {
    Department findByDepartmentCode(String departmentCode);
}
