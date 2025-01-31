package net.javaguides.payment_service.mappers;

import net.javaguides.payment_service.schemas.Payment;
import net.javaguides.payment_service.schemas.response.ResPaymentDto;
import net.javaguides.payment_service.schemas.response.ResUserDTO;
import org.mapstruct.Mapper;

/**
 * File: IPaymentMapper.java
 * Author: Le Van Hoang
 * Date: 31/01/2025
 * Time: 22:28
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Mapper(componentModel = "spring")
public interface IPaymentMapper {
    ResPaymentDto toResPaymentDto(Payment payment);

}
