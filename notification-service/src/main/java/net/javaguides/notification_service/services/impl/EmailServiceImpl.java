package net.javaguides.notification_service.services.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import net.javaguides.event.dto.UserActiveEvent;
import net.javaguides.notification_service.dto.response.ResCartByIdDto;
import net.javaguides.notification_service.dto.response.ResCartItemByIdDto;
import net.javaguides.notification_service.dto.response.ResOrderByIdDto;
import net.javaguides.notification_service.dto.response.ResUserDTO;
import net.javaguides.notification_service.services.IEmailService;
import net.javaguides.notification_service.services.httpClient.IUserServiceClient;
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
    private static final String EMAIL_ORDER_CONFIRMATION_TEMPLATE = "email-bill";
    public static final String EMAIL_ACCOUNT_ACTIVATION_TEMPLATE = "email-account-activation";

    @Value("${activation.link}")
    private String linkActivation;

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

    @Override
    public void sendOrderConfirmationEmail(String name, String to, ResUserDTO resUserDTO, ResCartByIdDto resCartByIdDto, ResOrderByIdDto resOrderByIdDto, ResCartItemByIdDto resCartItemByIdDto) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();

            var messageHelper = new MimeMessageHelper(mimeMessage, true, UTF_8_ENCODING);

            messageHelper.setFrom(fromEmail);
            messageHelper.setTo(to);
            messageHelper.setSubject("Đơn Hàng Của Bạn Đã Được Xác Nhận");

            Context context = new Context();
            context.setVariable("name", name);
            context.setVariable("resUserDTO", resUserDTO);
            context.setVariable("resCartByIdDto", resCartByIdDto);
            context.setVariable("resOrderByIdDto", resOrderByIdDto);
            context.setVariable("resCartItemByIdDto", resCartItemByIdDto);
            String content = templateEngine.process(EMAIL_ORDER_CONFIRMATION_TEMPLATE, context);

            messageHelper.setText(content, true);

            javaMailSender.send(mimeMessage);
        } catch (MessagingException | MailException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendAccountActivationEmail(UserActiveEvent userActiveEvent) {
        try {
            System.out.println("linkActivation = " + linkActivation);
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            var messageHelper = new MimeMessageHelper(mimeMessage, true, UTF_8_ENCODING);

            messageHelper.setFrom(fromEmail);
            messageHelper.setTo(userActiveEvent.getEmail());
            messageHelper.setSubject("Kích Hoạt Tài Khoản");

//            String activationLink = "http://localhost:9191/identity-service/api/v1/auth/activate?token=" + userActiveEvent.getToken();
            String activationLink = linkActivation + userActiveEvent.getToken();
            Context context = new Context();
            context.setVariable("name", userActiveEvent.getName());
            context.setVariable("email", userActiveEvent.getEmail());
            context.setVariable("activationLink", activationLink);
            String content = templateEngine.process(EMAIL_ACCOUNT_ACTIVATION_TEMPLATE, context);

            messageHelper.setText(content, true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException | MailException e) {
            e.printStackTrace();
        }
    }

}
