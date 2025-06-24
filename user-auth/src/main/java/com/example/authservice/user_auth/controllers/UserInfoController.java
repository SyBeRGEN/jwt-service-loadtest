package com.example.authservice.user_auth.controllers;

import com.example.authservice.user_auth.dto.User;
import com.example.authservice.user_auth.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserInfoController {

    private final UserRepository userRepository;
    private final RedisTemplate<String, String> redisTemplate;

    public UserInfoController(UserRepository userRepository,
                              @Qualifier("redisStringTemplate") RedisTemplate<String, String> redisTemplate) {
        this.userRepository = userRepository;
        this.redisTemplate = redisTemplate;
    }

    @GetMapping("/user-info")
    public ResponseEntity<?> getUserInfo() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<User> userOpt = findUserByUsername(username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        User user = userOpt.get();

        Map<String, String> userData = Map.of(
                "username", user.getUsername(),
                "email", user.getEmail(),
                "phone", user.getPhone()
        );

        return ResponseEntity.ok(userData);
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
}

