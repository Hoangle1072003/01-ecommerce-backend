package net.javaguides.department_service.controller;

import lombok.RequiredArgsConstructor;
import net.javaguides.department_service.entity.request.ReqDepartmentDto;
import net.javaguides.department_service.service.impl.DepartmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * File: DepartmentController.java
 * Author: Le Van Hoang
 * Date: 12/26/2024 (26/12/2024)
 * Time: 7:17 AM
 * Version: 1.0
 * <p>
 * Copyright © 2024 Le Van Hoang. All rights reserved.
 */

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class DepartmentController {
    private final DepartmentService departmentService;

    @PostMapping("/department-service")
    public ResponseEntity<ReqDepartmentDto> saveDepartment(@RequestBody ReqDepartmentDto reqDepartmentDto) {
        ReqDepartmentDto departmentDto = departmentService.saveDepartment(reqDepartmentDto);
        if (departmentDto != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(departmentDto);
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/department-service/{departmentCode}")
    public ResponseEntity<ReqDepartmentDto> getDepartmentByCode(@PathVariable String departmentCode) {
        ReqDepartmentDto departmentDto = departmentService.getDepartmentByCode(departmentCode);
        if (departmentDto != null) {
            return ResponseEntity.ok(departmentDto);
        }
        return ResponseEntity.notFound().build();
    }
}
