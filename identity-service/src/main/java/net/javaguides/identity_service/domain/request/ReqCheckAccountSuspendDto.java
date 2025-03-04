package net.javaguides.identity_service.domain.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Le Van Hoang
 * @version 1.0
 * @since 2025-03-04
 * Time: 12:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqCheckAccountSuspendDto {
    private String email;
}
