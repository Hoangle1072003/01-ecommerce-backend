package net.javaguides.department_service.service.impl;

import lombok.RequiredArgsConstructor;
import net.javaguides.department_service.entity.Department;
import net.javaguides.department_service.entity.request.ReqDepartmentDto;
import net.javaguides.department_service.mapper.IDepartmentMapper;
import net.javaguides.department_service.repository.IDepartmentRepository;
import net.javaguides.department_service.service.IDepartmentService;
import org.springframework.stereotype.Service;

/**
 * File: DepartmentService.java
 * Author: Le Van Hoang
 * Date: 12/26/2024 (26/12/2024)
 * Time: 7:12 AM
 * Version: 1.0
 * <p>
 * Copyright © 2024 Le Van Hoang. All rights reserved.
 */

@Service
@RequiredArgsConstructor
public class DepartmentService implements IDepartmentService {
    private final IDepartmentRepository departmentRepository;
    private final IDepartmentMapper departmentMapper;

    @Override
    public ReqDepartmentDto saveDepartment(ReqDepartmentDto reqDepartmentDto) {
        Department department = departmentMapper.toEntity(reqDepartmentDto);
        department = departmentRepository.save(department);
        return departmentMapper.toDto(department);
    }

    @Override
    public ReqDepartmentDto getDepartmentByCode(String code) {
        Department department = departmentRepository.findByDepartmentCode(code);
        return departmentMapper.toDto(department);
    }
}
