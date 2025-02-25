package net.javaguides.identity_service.domain.response;

import lombok.Data;
import net.javaguides.identity_service.utils.constant.CartStatusEnum;

import java.util.UUID;

/**
 * @author Le Van Hoang
 * @version 1.0
 * @since 2025-02-24
 * Time: 23:45
 */
@Data
public class ResCartByUser {
    private String id;
    private UUID userId;
    private CartStatusEnum status;
    private double total;
}

