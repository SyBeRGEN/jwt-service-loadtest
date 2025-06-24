package com.example.authservice.user_auth.services;

import com.example.authservice.user_auth.dto.User;
import com.example.authservice.user_auth.dto.VerificationRequest;
import com.example.authservice.user_auth.dto.VerificationResponse;
import com.example.authservice.user_auth.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class RegistrationService {

    private final RestTemplate restTemplate;

    @Value("${verification.service.url}")
    private String verificationServiceUrl;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationService(RestTemplate restTemplate, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.restTemplate = restTemplate;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<?> register(@Valid @RequestBody User user) {
        // Проверка: пользователь уже существует?
        Optional<User> existing = findUserByUsername(user.getUsername());
        if (existing.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        }

        // Верификация данных
        VerificationRequest verifyRequest = new VerificationRequest(
                user.getEmail(),
                user.getPhone(),
                "TR", // или динамически определяй
                user.getUsername()
        );

        VerificationResponse verifyResponse = restTemplate.postForObject(
                verificationServiceUrl,
                verifyRequest,
                VerificationResponse.class
        );

        assert verifyResponse != null;
        if (!verifyResponse.emailValid || !verifyResponse.phoneValid || !verifyResponse.usernameClean) {
            return ResponseEntity.badRequest().body("User data verification failed");
        }

        // Генерация уникального ID и сохранение
        String userId = UUID.randomUUID().toString();
        user.setId(userId);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        // Возврат
        Map<String, String> response = new HashMap<>();
        response.put("userId", userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Поиск по username вручную, т.к. редиска
    private Optional<User> findUserByUsername(String username) {
        Iterable<User> all = userRepository.findAll();
        for (User u : all) {
            if (u.getUsername().equalsIgnoreCase(username)) {
                return Optional.of(u);
            }
        }
        return Optional.empty();
    }
}
