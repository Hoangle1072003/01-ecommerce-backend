package net.javaguides.notification_service.services.impl;

import lombok.RequiredArgsConstructor;
import net.javaguides.notification_service.services.IEmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import static net.javaguides.notification_service.utils.EmailUtil.getEmailMessage;

/**
 * File: EmailServiceImpl.java
 * Author: Le Van Hoang
 * Date: 27/01/2025
 * Time: 16:08
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements IEmailService {
    @Value("${spring.mail.username}")
    private String fromEmail;
    private final JavaMailSender javaMailSender;

    @Override
    public void sendSimpleMailMessage(String name, String to) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject("Welcome to JavaGuides");
            message.setText(getEmailMessage(name));
            javaMailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
