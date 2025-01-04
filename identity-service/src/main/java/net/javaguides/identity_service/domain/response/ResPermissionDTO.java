package net.javaguides.identity_service.domain.response;

import jakarta.validation.constraints.NotBlank;
import lombok.Value;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link net.javaguides.identity_service.domain.Permission}
 */
@Value
public class ResPermissionDTO implements Serializable {
    UUID id;
    @NotBlank(message = "Name is required")
    String name;
    @NotBlank(message = "API Path is required")
    String apiPath;
    @NotBlank(message = "Method is required")
    String method;
    @NotBlank(message = "Module is required")
    String module;
}