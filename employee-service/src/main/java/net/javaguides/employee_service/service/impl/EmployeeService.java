package net.javaguides.employee_service.service.impl;

import lombok.RequiredArgsConstructor;
import net.javaguides.employee_service.entity.Employee;
import net.javaguides.employee_service.entity.request.ReqEmployeeDto;
import net.javaguides.employee_service.entity.response.ResAPIDto;
import net.javaguides.employee_service.entity.response.ResDepartmentDto;
import net.javaguides.employee_service.mapper.IEmployeeMapper;
import net.javaguides.employee_service.repository.IEmployeeRepository;
import net.javaguides.employee_service.service.APIClient;
import net.javaguides.employee_service.service.IEmployeeService;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * File: EmployeeService.java
 * Author: Le Van Hoang
 * Date: 12/26/2024 (26/12/2024)
 * Time: 7:55 AM
 * Version: 1.0
 * <p>
 * Copyright © 2024 Le Van Hoang. All rights reserved.
 */

@RequiredArgsConstructor
@Service
public class EmployeeService implements IEmployeeService {
    private final IEmployeeRepository employeeRepository;
    private final IEmployeeMapper employeeMapper;

    private final APIClient apiClient;

    @Override
    public ReqEmployeeDto saveEmployee(ReqEmployeeDto reqEmployeeDto) {
        return employeeMapper.toDto(employeeRepository.save(employeeMapper.toEntity(reqEmployeeDto)));
    }

    @Override
    public ResAPIDto findById(UUID id) {
        Employee employee = employeeRepository.findById(id).orElse(null);
        if (employee == null) {
            return null;
        }
        
        ResDepartmentDto department = apiClient.getDepartmentByCode(employee.getDepartmentCode());
        if (department == null) {
            return null;
        }
        ResAPIDto resAPIDto = new ResAPIDto();
        resAPIDto.setEmployee(employeeMapper.toDto(employee));
        resAPIDto.setDepartment(department);
        return resAPIDto;
    }
}
