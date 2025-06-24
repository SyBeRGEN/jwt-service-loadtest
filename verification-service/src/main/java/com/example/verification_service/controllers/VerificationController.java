package com.example.verification_service.controllers;

import com.example.verification_service.dto.VerificationRequest;
import com.example.verification_service.dto.VerificationResponse;
import com.example.verification_service.sevices.VerificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class VerificationController {

    private final VerificationService verificationService;

    public VerificationController(VerificationService verificationService) {
        this.verificationService = verificationService;
    }

    @PostMapping("/verify")
    public ResponseEntity<VerificationResponse> verify(@RequestBody VerificationRequest request) {
        boolean emailValid = verificationService.isEmailValid(request.getEmail());
        boolean phoneValid = verificationService.isPhoneValid(request.getPhone(), request.getRegion());
        boolean usernameClean = verificationService.isUsernameClean(request.getUsername());

        return ResponseEntity.ok(new VerificationResponse(emailValid, phoneValid, usernameClean));
    }
}
