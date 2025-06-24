package com.example.authservice.user_auth.services;

import com.example.authservice.user_auth.dto.User;
import com.example.authservice.user_auth.repositories.UserRepository;
import com.example.authservice.user_auth.utils.JWTUtil;
import com.example.authservice.user_auth.dto.LoginRequest;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;

@Service
public class LoginService {

    private final UserRepository userRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final PasswordEncoder passwordEncoder;

    public LoginService(UserRepository userRepository,
                        @Qualifier("redisStringTemplate") RedisTemplate<String, String> redisTemplate,
                        PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.redisTemplate = redisTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<?> login(LoginRequest request) {
        // Поиск пользователя
        Optional<User> userOpt = findUserByUsername(request.getUsername());
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        User user = userOpt.get();

        // Примитивная проверка пароля
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        // Генерация токена
        String token = JWTUtil.generateToken(user.getUsername());

        // Сохраняем в Redis с TTL = 1 час
        redisTemplate.opsForValue().set(
                "token:" + user.getId(),
                token,
                Duration.ofHours(1)
        );

        return ResponseEntity.ok(Map.of("token", token));
    }

    private Optional<User> findUserByUsername(String username) {
        Iterable<User> all = userRepository.findAll();
        for (User u : all) {
            if (u.getUsername().equalsIgnoreCase(username)) {
                return Optional.of(u);
            }
        }
        return Optional.empty();
    }

    public ResponseEntity<?> logout(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                Claims claims = JWTUtil.parseToken(token);
                String username = claims.getSubject();

                Optional<User> userOpt = findUserByUsername(username);
                if (userOpt.isPresent()) {
                    String userId = userOpt.get().getId();
                    Boolean deleted = redisTemplate.delete("token:" + userId);

                    if (Boolean.TRUE.equals(deleted)) {
                        return ResponseEntity.ok("Logged out successfully");
                    } else {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Token not found");
                    }
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
                }

            } catch (JwtException e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
            }
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing or invalid Authorization header");
    }
}
