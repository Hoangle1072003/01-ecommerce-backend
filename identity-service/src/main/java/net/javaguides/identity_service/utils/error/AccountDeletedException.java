package net.javaguides.identity_service.utils.error;

/**
 * @author Le Van Hoang
 * @version 1.0
 * @since 2025-02-24
 * Time: 22:21
 */

public class AccountDeletedException extends RuntimeException {
    public AccountDeletedException(String message) {
        super(message);
    }
}
