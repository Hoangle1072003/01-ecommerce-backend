package net.javaguides.department_service.service;

import net.javaguides.department_service.entity.request.ReqDepartmentDto;

/**
 * File: null.java
 * Author: Le Van Hoang
 * Date: 12/26/2024 (26/12/2024)
 * Time: 7:12 AM
 * Version: 1.0
 * <p>
 * Copyright © 2024 Le Van Hoang. All rights reserved.
 */

public interface IDepartmentService {
    ReqDepartmentDto saveDepartment(ReqDepartmentDto reqDepartmentDto);

    ReqDepartmentDto getDepartmentByCode(String code);
}
