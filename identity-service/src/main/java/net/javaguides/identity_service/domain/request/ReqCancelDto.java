package net.javaguides.identity_service.domain.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.javaguides.identity_service.utils.constant.AuthProvider;

/**
 * @author Le Van Hoang
 * @version 1.0
 * @since 2025-02-24
 * Time: 21:53
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqCancelDto {
    private String email;
    private String reason;
    private AuthProvider provider;
}
