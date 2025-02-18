package net.javaguides.payment_service.utils;

import jakarta.servlet.http.HttpServletRequest;
import net.javaguides.payment_service.schemas.Payment;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * File: VNPayUtil.java
 * Author: Le Van Hoang
 * Date: 25/01/2025
 * Time: 21:29
 * Version: 1.1
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

public class VNPayUtil {
    @Value("${payment.vnPay.secretKey}")
    private static String secretKeys;

    /**
     * Generate HMAC SHA512 hash from key and data
     *
     * @param key  Secret key
     * @param data Data to hash
     * @return HMAC SHA512 hash as a hex string
     */
    public static String hmacSHA512(final String key, final String data) {
        try {
            if (key == null || data == null) {
                throw new IllegalArgumentException("Key or data cannot be null");
            }
            final Mac hmac512 = Mac.getInstance("HmacSHA512");
            byte[] hmacKeyBytes = key.getBytes(StandardCharsets.UTF_8);
            final SecretKeySpec secretKey = new SecretKeySpec(hmacKeyBytes, "HmacSHA512");
            hmac512.init(secretKey);
            byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
            byte[] result = hmac512.doFinal(dataBytes);
            StringBuilder sb = new StringBuilder(2 * result.length);
            for (byte b : result) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (Exception ex) {
            throw new RuntimeException("Error while generating HMAC SHA512", ex);
        }
    }


    /**
     * Generate a random numeric string
     *
     * @param len Length of the random string
     * @return Random numeric string
     */
    public static String getRandomNumber(int len) {
        Random rnd = new Random();
        String chars = "0123456789";
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }

    /**
     * Build URL query string from parameters map
     *
     * @param paramsMap Map of parameters
     * @param encodeKey Whether to encode keys in the query string
     * @return Query string
     */
    public static String getPaymentURL(Map<String, String> paramsMap, boolean encodeKey) {
        return paramsMap.entrySet().stream()
                .filter(entry -> entry.getValue() != null && !entry.getValue().isEmpty())
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> {
                    try {
                        String key = encodeKey ? URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8.toString()) : entry.getKey();
                        String value = URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8.toString());
                        return key + "=" + value;
                    } catch (Exception ex) {
                        throw new RuntimeException("Error encoding URL parameter", ex);
                    }
                })
                .collect(Collectors.joining("&"));
    }

    /**
     * Get current time in VNPAY format (yyyyMMddHHmmss)
     *
     * @return Current time as string
     */
    public static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+7"));
        return sdf.format(new Date());
    }

    /**
     * Get expire time in VNPAY format (yyyyMMddHHmmss)
     *
     * @return Expire time (15 minutes from now) as string
     */
    public static String getExpireTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+7"));
        Date expireTime = new Date(System.currentTimeMillis() + 2 * 60 * 1000); // 2 minutes from now
        return sdf.format(expireTime);
    }

    public static boolean verifySecureHash(Map<String, String> params, String secureHash) throws NoSuchAlgorithmException {
        String secretKey = secretKeys;
        StringBuilder data = new StringBuilder();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (!entry.getKey().equals("vnp_SecureHash") && !entry.getValue().isEmpty()) {
                data.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
        }

        data.append("vnp_SecureHashSecret=").append(secretKey);

        String calculatedHash = hmacSHA512(secretKey, data.toString());

        return calculatedHash.equals(secureHash.toUpperCase());
    }


}
