package net.javaguides.employee_service.controller;

import lombok.RequiredArgsConstructor;
import net.javaguides.employee_service.entity.request.ReqEmployeeDto;
import net.javaguides.employee_service.entity.response.ResAPIDto;
import net.javaguides.employee_service.service.IEmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * File: EmployeeController.java
 * Author: Le Van Hoang
 * Date: 12/26/2024 (26/12/2024)
 * Time: 7:56 AM
 * Version: 1.0
 * <p>
 * Copyright © 2024 Le Van Hoang. All rights reserved.
 */

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class EmployeeController {
    private final IEmployeeService employeeService;

    @GetMapping("/employee-service/{id}")
    public ResponseEntity<ResAPIDto> getEmployeeServiceByID(@PathVariable UUID id) {
        return ResponseEntity.ok(employeeService.findById(id));
    }

    @PostMapping("/employee-service")
    public ResponseEntity<ReqEmployeeDto> saveEmployee(@RequestBody ReqEmployeeDto reqEmployeeDto) {
        return ResponseEntity.ok(employeeService.saveEmployee(reqEmployeeDto));
    }
}
