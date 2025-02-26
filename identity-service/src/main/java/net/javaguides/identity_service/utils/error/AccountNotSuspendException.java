package net.javaguides.identity_service.utils.error;

/**
 * @author Le Van Hoang
 * @version 1.0
 * @since 2025-02-25
 * Time: 01:21
 */
public class AccountNotSuspendException extends RuntimeException {
    public AccountNotSuspendException(String message) {
        super(message);
    }
}
