package net.javaguides.identity_service.utils.error;

/**
 * File: AccountNotActivatedException.java
 * Author: Le Van Hoang
 * Date: 09/02/2025
 * Time: 20:28
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

public class AccountNotActivatedException extends Exception {
    public AccountNotActivatedException(String message) {
        super(message);
    }
}
