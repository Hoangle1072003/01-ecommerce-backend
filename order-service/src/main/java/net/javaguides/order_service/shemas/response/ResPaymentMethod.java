package net.javaguides.order_service.shemas.response;

import lombok.Data;
import net.javaguides.order_service.utils.constants.PaymentMethod;

/**
 * File: ResPaymentMethod.java
 * Author: Le Van Hoang
 * Date: 31/01/2025
 * Time: 16:26
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Data
public class ResPaymentMethod {
    private PaymentMethod paymentMethod;
}
