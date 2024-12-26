package net.javaguides.employee_service.entity.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * File: ResDepartmentDto.java
 * Author: Le Van Hoang
 * Date: 12/26/2024 (26/12/2024)
 * Time: 8:59 AM
 * Version: 1.0
 * <p>
 * Copyright © 2024 Le Van Hoang. All rights reserved.
 */

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResDepartmentDto {
    private UUID id;
    private String departmentName;
    private String departmentDescription;
    private String departmentCode;
}

