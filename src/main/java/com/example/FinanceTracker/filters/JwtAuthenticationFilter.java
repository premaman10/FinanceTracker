package com.example.FinanceTracker.filters;

import com.example.FinanceTracker.service.JwtService;
import com.example.FinanceTracker.service.MyUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final MyUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // Skip JWT filter for static resources
        String path = request.getRequestURI();
        if (path.equals("/") || path.endsWith(".html") || path.endsWith(".css") || 
            path.endsWith(".js") || path.endsWith(".ico") || path.startsWith("/images/") || 
            path.startsWith("/fonts/") || path.startsWith("/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        // 1️⃣ Extract JWT token
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                token = authHeader.substring(7);
                username = jwtService.extractUsername(token);
            } catch (Exception e) {
                // Invalid token - continue without authentication
                filterChain.doFilter(request, response);
                return;
            }
        }

        // 2️⃣ Validate & authenticate
        if (username != null &&
                SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                UserDetails userDetails =
                        userDetailsService.loadUserByUsername(username);

                if (jwtService.isTokenValid(token, userDetails)) {

                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authenticationToken.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request)
                    );

                    SecurityContextHolder.getContext()
                            .setAuthentication(authenticationToken);
                }
            } catch (Exception e) {
                // User not found or invalid token - continue without authentication
            }
        }

        // 3️⃣ Continue filter chain
        filterChain.doFilter(request, response);
    }
}
