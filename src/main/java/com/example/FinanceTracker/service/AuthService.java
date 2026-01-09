package com.example.FinanceTracker.service;

import com.example.FinanceTracker.model.DTO.SignupRequest;
import com.example.FinanceTracker.model.User;
import com.example.FinanceTracker.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(SignupRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("User already exists");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());

        // 🔥 THIS IS THE KEY FIX
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.setProvider("LOCAL");

        userRepository.save(user);
    }
}
