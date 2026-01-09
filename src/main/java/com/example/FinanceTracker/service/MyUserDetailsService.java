package com.example.FinanceTracker.service;

import com.example.FinanceTracker.model.User;
import com.example.FinanceTracker.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));

        // For GOOGLE users, password may be null; they never use email/password login,
        // but we still need a UserDetails so JWT-authenticated requests work.
        String password = user.getPassword() != null ? user.getPassword() : "";

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(password)
                .authorities("USER")
                .build();
    }
}
