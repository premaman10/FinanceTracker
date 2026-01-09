package com.example.FinanceTracker.oauth;

import com.example.FinanceTracker.model.User;
import com.example.FinanceTracker.repo.UserRepository;
import com.example.FinanceTracker.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomOAuth2UserService implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {

        String email;
        String name;

        Object principal = authentication.getPrincipal();

        // ✅ Google OIDC
        if (principal instanceof OidcUser oidcUser) {
            email = oidcUser.getEmail();
            name = oidcUser.getFullName();
        }
        // ✅ OAuth2 fallback
        else if (principal instanceof OAuth2User oAuth2User) {
            email = oAuth2User.getAttribute("email");
            name = oAuth2User.getAttribute("name");

            if (email == null) {
                email = authentication.getName();
            }
        }
        // ✅ Final fallback
        else {
            email = authentication.getName();
            name = email;
        }

        // ✅ Create or update user
        String finalEmail = email;
        User user = userRepository.findByEmail(email).orElseGet(() -> {
            User u = new User();
            u.setEmail(finalEmail);
            u.setName(name != null ? name : finalEmail);
            u.setProvider("GOOGLE");
            u.setPassword(null); // OAuth users don’t need password
            return u;
        });

        userRepository.save(user);

        // 🔐 Generate JWT
        String jwt = jwtService.generateToken(email);

        // 🚨 VERY IMPORTANT — clear OAuth session
        SecurityContextHolder.clearContext();

        // 🔁 Redirect to dashboard with token
        response.sendRedirect("/dashboard.html?token=" + jwt);
    }
}
