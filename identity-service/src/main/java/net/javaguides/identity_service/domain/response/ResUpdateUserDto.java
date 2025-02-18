package net.javaguides.identity_service.domain.response;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.javaguides.identity_service.utils.constant.GenderEnum;
import net.javaguides.identity_service.utils.constant.StatusEnum;

import java.time.Instant;
import java.util.UUID;

/**
 * File: ResUpdateUserDto.java
 * Author: Le Van Hoang
 * Date: 17/02/2025
 * Time: 15:50
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResUpdateUserDto {
    private UUID id;
    private String name;
    private String address;
    private GenderEnum gender;
    private StatusEnum status;
}
