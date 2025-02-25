package net.javaguides.identity_service.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author Le Van Hoang
 * @version 1.0
 * @since 2025-02-25
 * Time: 00:21
 */
@Slf4j
@Component
public class AuthenticationRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes servletRequestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (servletRequestAttributes != null) {
            var authHeader = servletRequestAttributes.getRequest().getHeader("Authorization");
            if (StringUtils.hasText(authHeader)) {
                template.header("Authorization", authHeader);
            }
        } else {
            log.warn("No HTTP Request context found. Skipping Authorization header.");
        }
    }

}