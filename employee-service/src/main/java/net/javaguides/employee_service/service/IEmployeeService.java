package net.javaguides.employee_service.service;

import net.javaguides.employee_service.entity.request.ReqEmployeeDto;
import net.javaguides.employee_service.entity.response.ResAPIDto;

import java.util.UUID;

/**
 * File: null.java
 * Author: Le Van Hoang
 * Date: 12/26/2024 (26/12/2024)
 * Time: 7:55 AM
 * Version: 1.0
 * <p>
 * Copyright © 2024 Le Van Hoang. All rights reserved.
 */

public interface IEmployeeService {
    ReqEmployeeDto saveEmployee(ReqEmployeeDto reqEmployeeDto);

    ResAPIDto findById(UUID id);
}
