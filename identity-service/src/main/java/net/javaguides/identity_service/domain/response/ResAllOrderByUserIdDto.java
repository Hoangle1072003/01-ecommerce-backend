package net.javaguides.identity_service.domain.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.javaguides.identity_service.utils.constant.OrderStatusEnum;
import net.javaguides.identity_service.utils.constant.PaymentMethod;
import net.javaguides.identity_service.utils.constant.PaymentStatus;

/**
 * @author Le Van Hoang
 * @version 1.0
 * @since 2025-02-25
 * Time: 00:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResAllOrderByUserIdDto {
    private String id;
    private String cartId;
    private String userId;
    private String shipping;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private OrderStatusEnum orderStatusEnum;
    private String paymentId;
    private Double totalAmount;
}

