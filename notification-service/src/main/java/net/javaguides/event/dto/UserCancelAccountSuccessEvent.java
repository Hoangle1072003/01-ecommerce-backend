package net.javaguides.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Le Van Hoang
 * @version 1.0
 * @since 2025-02-24
 * Time: 23:07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCancelAccountSuccessEvent {
    private LocalDateTime timestamp;
    private String email;
    private String reason;
}
