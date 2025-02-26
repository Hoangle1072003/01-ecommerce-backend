package net.javaguides.identity_service.domain.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Le Van Hoang
 * @version 1.0
 * @since 2025-02-24
 * Time: 17:28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqCancelAccountDto {
    private String email;
    private String reason;
}
