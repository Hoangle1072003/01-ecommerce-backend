package net.javaguides.employee_service.entity.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.javaguides.employee_service.entity.request.ReqEmployeeDto;

/**
 * File: ResAPIDto.java
 * Author: Le Van Hoang
 * Date: 12/26/2024 (26/12/2024)
 * Time: 9:15 AM
 * Version: 1.0
 * <p>
 * Copyright © 2024 Le Van Hoang. All rights reserved.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResAPIDto {
    private ReqEmployeeDto employee;
    private ResDepartmentDto department;

}
