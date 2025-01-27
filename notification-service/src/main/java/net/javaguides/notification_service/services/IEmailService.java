package net.javaguides.notification_service.services;

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
}
