package net.javaguides.identity_service.domain.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.javaguides.identity_service.utils.constant.GenderEnum;

import java.util.UUID;

/**
 * File: ReqUpdateUserDto.java
 * Author: Le Van Hoang
 * Date: 17/02/2025
 * Time: 15:48
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqUpdateUserDto {
    private UUID id;
    private String name;
    private GenderEnum gender;
    private String address;
}
