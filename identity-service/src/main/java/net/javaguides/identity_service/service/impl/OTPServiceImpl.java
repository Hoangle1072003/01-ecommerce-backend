package net.javaguides.identity_service.service.impl;

import lombok.RequiredArgsConstructor;
import net.javaguides.identity_service.service.IOTPService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author Le Van Hoang
 * @version 1.0
 * @since 2025-02-24
 * Time: 17:19
 */
@Service
@RequiredArgsConstructor
public class OTPServiceImpl implements IOTPService {
    private final StringRedisTemplate redisTemplate;
    private static final int OTP_LENGTH = 6;
    private static final long EXPIRE_TIME = 5;

    private String generateRandomOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    @Override
    public String generateOtp(String key) {
        String otp = generateRandomOtp();
        String redisKey = "otp:user:" + key;
        redisTemplate.opsForValue().set(redisKey, otp, EXPIRE_TIME, TimeUnit.MINUTES);
        return otp;
    }

    @Override
    public boolean verifyOtp(String key, String otp) {
        String redisKey = "otp:user:" + key;
        String redisOtp = redisTemplate.opsForValue().get(redisKey);
        if (otp.equals(redisOtp)) {
//            redisTemplate.delete(redisKey);
            return true;
        }
        return false;
    }

    @Override
    public void deleteOtp(String key) {
        String redisKey = "otp:user:" + key;
        redisTemplate.delete(redisKey);
    }
}
