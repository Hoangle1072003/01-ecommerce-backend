package net.javaguides.notification_service.utils;

/**
 * File: EmailUtil.java
 * Author: Le Van Hoang
 * Date: 27/01/2025
 * Time: 16:09
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

public class EmailUtil {

    public static String getEmailMessage(String name) {
        return "Hello " + name + ",\n\nYour new account has been created. Please click the link below to verify your account. \n\n";
    }
}
