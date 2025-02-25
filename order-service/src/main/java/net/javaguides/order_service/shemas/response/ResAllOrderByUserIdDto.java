package net.javaguides.order_service.shemas.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.javaguides.order_service.utils.constants.OrderStatusEnum;
import net.javaguides.order_service.utils.constants.PaymentMethod;
import net.javaguides.order_service.utils.constants.PaymentStatus;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * @author Le Van Hoang
 * @version 1.0
 * @since 2025-02-25
 * Time: 00:38
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
