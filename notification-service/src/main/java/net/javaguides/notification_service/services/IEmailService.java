package net.javaguides.notification_service.services;

import net.javaguides.event.dto.UserActiveEvent;
import net.javaguides.notification_service.dto.response.ResCartByIdDto;
import net.javaguides.notification_service.dto.response.ResCartItemByIdDto;
import net.javaguides.notification_service.dto.response.ResOrderByIdDto;
import net.javaguides.notification_service.dto.response.ResUserDTO;

/**
 * File: IEmailService.java
 * Author: Le Van Hoang
 * Date: 27/01/2025
 * Time: 16:07
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

public interface IEmailService {
    void sendSimpleMailMessage(String name, String to);

    void sendThankYouEmail(String name, String to);

    void sendOrderConfirmationEmail(String name, String to, ResUserDTO resUserDTO, ResCartByIdDto resCartByIdDto, ResOrderByIdDto resOrderByIdDto, ResCartItemByIdDto resCartItemByIdDto);

    void sendAccountActivationEmail(UserActiveEvent userActiveEvent);

    void sendForgotPasswordEmail(String email, String token);
}
