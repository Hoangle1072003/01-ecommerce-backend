package net.javaguides.identity_service.domain.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Le Van Hoang
 * @version 1.0
 * @since 2025-02-25
 * Time: 02:15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqActiveAccountSuspendOTPDto {
    private String email;
    private String otp;
}
