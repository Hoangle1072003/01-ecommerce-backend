package net.javaguides.employee_service.entity.request;

import lombok.Value;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link net.javaguides.employee_service.entity.Employee}
 */
@Value
public class ReqEmployeeDto implements Serializable {
    UUID id;
    String firstName;
    String lastName;
    String email;
    String departmentCode;
}