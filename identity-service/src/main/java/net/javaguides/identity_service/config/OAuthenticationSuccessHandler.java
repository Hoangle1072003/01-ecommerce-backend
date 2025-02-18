package net.javaguides.identity_service.config;

import com.google.auth.oauth2.IdTokenCredentials;
import com.google.auth.oauth2.IdTokenProvider;
import com.google.auth.oauth2.UserCredentials;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import net.javaguides.identity_service.domain.Role;
import net.javaguides.identity_service.domain.User;
import net.javaguides.identity_service.repository.IRoleRepository;
import net.javaguides.identity_service.repository.IUserRepository;
import net.javaguides.identity_service.utils.constant.AuthProvider;
import net.javaguides.identity_service.utils.constant.StatusEnum;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * File: OAuthenticationSuccessHandler.java
 * Author: Le Van Hoang
 * Date: 10/02/2025
 * Time: 15:00
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Component
@RequiredArgsConstructor
public class OAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final IRoleRepository roleRepository;
    private final IUserRepository userRepository;
    private final OAuth2AuthorizedClientService authorizedClientService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        System.out.println("Authentication success Google");

        DefaultOAuth2User user = (DefaultOAuth2User) authentication.getPrincipal();

        System.out.println("User: " + user);

        String email = (String) user.getAttributes().get("email");
        String name = (String) user.getAttributes().get("name");
        String picture = (String) user.getAttributes().get("picture");

        User userByEmail = userRepository.findByEmail(email);
        if (userByEmail == null) {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setName(name);
            newUser.setImageUrl(picture);
            newUser.setProvider(AuthProvider.GOOGLE);
            newUser.setStatus(StatusEnum.ACTIVATED);
            newUser.setProviderId(user.getName());
            Role role = roleRepository.findByName("USER");
            newUser.setRole(role);
            userRepository.save(newUser);
        }

        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient(
                oauthToken.getAuthorizedClientRegistrationId(),
                oauthToken.getName()
        );
        System.out.println("Authorized Client: " + authorizedClient);

        if (authorizedClient != null) {
            String accessToken = authorizedClient.getAccessToken().getTokenValue();
            System.out.println("Access Token: " + accessToken);

            response.sendRedirect(accessToken);
            return;
        }
        new DefaultRedirectStrategy().sendRedirect(request, response, "http://localhost:3001");
    }
}
