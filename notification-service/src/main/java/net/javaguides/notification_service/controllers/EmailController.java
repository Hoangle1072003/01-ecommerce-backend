package net.javaguides.notification_service.controllers;

import lombok.RequiredArgsConstructor;
import net.javaguides.notification_service.dto.request.ReqEmailSimpleDto;
import net.javaguides.notification_service.services.IEmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * File: EmailController.java
 * Author: Le Van Hoang
 * Date: 27/01/2025
 * Time: 16:12
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */
@RestController
@RequestMapping("/api/v1/notification")
@RequiredArgsConstructor
public class EmailController {
    private final IEmailService emailService;

//    @PostMapping
//    public ResponseEntity<String> sendEmail(@RequestBody ReqEmailSimpleDto reqEmailSimpleDto) {
//        try {
//            emailService.sendThankYouEmail(reqEmailSimpleDto.getName(), reqEmailSimpleDto.getTo());
//            return ResponseEntity.ok("Email sent successfully");
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body("Failed to send email: " + e.getMessage());
//        }
//    }

    @PostMapping
    public ResponseEntity<String> sendEmail(@RequestBody ReqEmailSimpleDto reqEmailSimpleDto) {
        try {
            emailService.sendOrderConfirmationEmail(reqEmailSimpleDto.getName(), reqEmailSimpleDto.getTo(), reqEmailSimpleDto.getResUserDTO());
            return ResponseEntity.ok("Email sent successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to send email: " + e.getMessage());
        }
    }

}
