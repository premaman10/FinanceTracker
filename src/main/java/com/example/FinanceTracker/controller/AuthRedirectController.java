package com.example.FinanceTracker.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthRedirectController {

    @GetMapping({"/", "/login"})
    public String redirectIfAuthenticated(Authentication authentication) {

        // If user is logged in → go to dashboard
        if (authentication != null && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getPrincipal())) {
            return "redirect:/dashboard.html";
        }

        // Otherwise → show login page
        return "forward:/index.html";
    }
}
