package net.javaguides.notification_service.services.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import net.javaguides.notification_service.services.IEmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

import org.thymeleaf.context.Context;

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
    private final TemplateEngine templateEngine;
    public static final String UTF_8_ENCODING = "UTF-8";
    public static final String EMAIL_TEMPLATE = "email-thanks";
    public static final String TEXT_HTML_ENCONDING = "text/html";

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

    @Override
    public void sendThankYouEmail(String name, String to) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();

            var messageHelper = new MimeMessageHelper(mimeMessage, true, UTF_8_ENCODING);

            messageHelper.setFrom(fromEmail);
            messageHelper.setTo(to);
            messageHelper.setSubject("Cảm Ơn Bạn Đã Chọn Chúng Tôi");

            Context context = new Context();
            context.setVariable("name", name);
            String content = templateEngine.process(EMAIL_TEMPLATE, context);

            messageHelper.setText(content, true);

            javaMailSender.send(mimeMessage);
        } catch (MessagingException | MailException e) {
            e.printStackTrace();
        }
    }
}
