package net.javaguides.chat_service.dtos.request;

import lombok.Data;
import net.javaguides.chat_service.utils.constant.TypeChatEnum;

/**
 * File: ReqAiDto.java
 * Author: Le Van Hoang
 * Date: 08/02/2025
 * Time: 21:06
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Data
public class ReqAiDto {
    private TypeChatEnum type;
    private String question;
    private String productId;
}
