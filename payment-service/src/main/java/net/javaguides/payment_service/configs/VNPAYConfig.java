package net.javaguides.payment_service.configs;

import lombok.Data;
import net.javaguides.payment_service.utils.VNPayUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * File: VNPAYConfig.java
 * Author: Le Van Hoang
 * Date: 25/01/2025
 * Time: 21:22
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */
@Data
@Configuration
public class VNPAYConfig {
    @Value("${payment.vnPay.url}")
    private String vnp_PayUrl;
    @Value("${payment.vnPay.returnUrl}")
    private String vnp_ReturnUrl;
    @Value("${payment.vnPay.tmnCode}")
    private String vnp_TmnCode;
    @Value("${payment.vnPay.secretKey}")
    private String secretKey;
    @Value("${payment.vnPay.version}")
    private String vnp_Version;
    @Value("${payment.vnPay.command}")
    private String vnp_Command;
    @Value("${payment.vnPay.orderType}")
    private String orderType;

    //    public Map<String, String> getVNPayConfig() {
//        Map<String, String> vnpParamsMap = new HashMap<>();
//        vnpParamsMap.put("vnp_Version", this.vnp_Version);
//        vnpParamsMap.put("vnp_Command", this.vnp_Command);
//        vnpParamsMap.put("vnp_TmnCode", this.vnp_TmnCode);
//        vnpParamsMap.put("vnp_CurrCode", "VND");
//        vnpParamsMap.put("vnp_TxnRef", VNPayUtil.getRandomNumber(8));
//        vnpParamsMap.put("vnp_OrderInfo", "Thanh toan don hang:" + VNPayUtil.getRandomNumber(8));
//        vnpParamsMap.put("vnp_OrderType", this.orderType);
//        vnpParamsMap.put("vnp_Locale", "vn");
//        vnpParamsMap.put("vnp_ReturnUrl", this.vnp_ReturnUrl);
//        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
//        String vnpCreateDate = formatter.format(calendar.getTime());
//        vnpParamsMap.put("vnp_CreateDate", vnpCreateDate);
//        calendar.add(Calendar.MINUTE, 2);
//        String vnp_ExpireDate = formatter.format(calendar.getTime());
//        vnpParamsMap.put("vnp_ExpireDate", vnp_ExpireDate);
//        return vnpParamsMap;
//    }
    public Map<String, String> getVNPayConfig(String orderId) {
        Map<String, String> vnpParamsMap = new HashMap<>();
        vnpParamsMap.put("vnp_Version", this.vnp_Version);
        vnpParamsMap.put("vnp_Command", this.vnp_Command);
        vnpParamsMap.put("vnp_TmnCode", this.vnp_TmnCode);
        vnpParamsMap.put("vnp_CurrCode", "VND");

        vnpParamsMap.put("vnp_TxnRef", orderId);
        vnpParamsMap.put("vnp_OrderInfo", "Thanh toán đơn hàng: " + orderId);

        vnpParamsMap.put("vnp_OrderType", this.orderType);
        vnpParamsMap.put("vnp_Locale", "vn");
        vnpParamsMap.put("vnp_ReturnUrl", this.vnp_ReturnUrl);


        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");

        String vnpCreateDate = formatter.format(calendar.getTime());
        vnpParamsMap.put("vnp_CreateDate", vnpCreateDate);

//        String vnpTransactionDate = formatter.format(calendar.getTime());
//        vnpParamsMap.put("vnp_TransactionDate", vnpTransactionDate);

        calendar.add(Calendar.MINUTE, 2);
        String vnp_ExpireDate = formatter.format(calendar.getTime());
        vnpParamsMap.put("vnp_ExpireDate", vnp_ExpireDate);

        return vnpParamsMap;
    }

}
