package net.javaguides.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Le Van Hoang
 * @version 1.0
 * @since 2025-02-24
 * Time: 14:22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserActiveSuspendEvent {
    private String email;
    private String otp;
}
