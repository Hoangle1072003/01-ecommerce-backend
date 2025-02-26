package net.javaguides.identity_service.service;

/**
 * @author Le Van Hoang
 * @version 1.0
 * @since 2025-02-24
 * Time: 17:19
 */
public interface IOTPService {
    String generateOtp(String key);

    boolean verifyOtp(String key, String otp);

    void deleteOtp(String key);
}
