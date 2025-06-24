package com.example.authservice.user_auth.controllers;

import com.example.authservice.user_auth.dto.LoginRequest;
import com.example.authservice.user_auth.services.LoginService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class LoginController {

    private final LoginService loginService;
    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public LoginController(LoginService loginService,
                           @Qualifier("redisStringTemplate") RedisTemplate<String, String> redisTemplate) {
        this.loginService = loginService;
        this.redisTemplate = redisTemplate;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        return loginService.login(request);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
        return loginService.logout(authHeader);
    }

}

