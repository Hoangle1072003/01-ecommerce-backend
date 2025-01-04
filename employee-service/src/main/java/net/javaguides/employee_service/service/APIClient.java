package net.javaguides.employee_service.service;

import net.javaguides.employee_service.entity.response.ResDepartmentDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


/**
 * File: null.java
 * Author: Le Van Hoang
 * Date: 12/26/2024 (26/12/2024)
 * Time: 8:52 AM
 * Version: 1.0
 * <p>
 * Copyright © 2024 Le Van Hoang. All rights reserved.
 */
@FeignClient(name = "DEPARTMENT-SERVICE")
public interface APIClient {
    @GetMapping("api/department-service/{departmentCode}")
    ResDepartmentDto getDepartmentByCode(@PathVariable String departmentCode);

}
