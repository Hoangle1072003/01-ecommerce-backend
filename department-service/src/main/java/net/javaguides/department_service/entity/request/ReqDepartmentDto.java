package net.javaguides.department_service.entity.request;

import lombok.Value;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link net.javaguides.department_service.entity.Department}
 */
@Value
public class ReqDepartmentDto implements Serializable {
    String departmentName;
    String departmentDescription;
    String departmentCode;
    UUID id;
}