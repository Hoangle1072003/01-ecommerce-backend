package net.javaguides.notification_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.javaguides.notification_service.dto.response.ResCartByIdDto;
import net.javaguides.notification_service.dto.response.ResCartItemByIdDto;
import net.javaguides.notification_service.dto.response.ResOrderByIdDto;
import net.javaguides.notification_service.dto.response.ResUserDTO;

/**
 * File: ReqEmailDto.java
 * Author: Le Van Hoang
 * Date: 27/01/2025
 * Time: 16:17
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqEmailSimpleDto {
    private String name;
    private String to;
    private ResUserDTO resUserDTO;
    private ResCartByIdDto resCartByIdDto;
    private ResOrderByIdDto resOrderByIdDto;
    private ResCartItemByIdDto resCartItemByIdDto;

}
