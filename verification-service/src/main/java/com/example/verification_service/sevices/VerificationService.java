package com.example.verification_service.sevices;

import com.google.i18n.phonenumbers.*;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Service
public class VerificationService {

    private static final Set<String> BAD_WORDS = new HashSet<>(Arrays.asList(
            "badword", "admin", "fuck", "shit", "root", "bastard", "idiot" // добавь свои
    ));

    private final PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

    public boolean isEmailValid(String email) {
        return EmailValidator.getInstance().isValid(email);
    }

    public boolean isPhoneValid(String phone, String region) {
        try {
            Phonenumber.PhoneNumber number = phoneUtil.parse(phone, region);
            return phoneUtil.isValidNumber(number);
        } catch (NumberParseException e) {
            return false;
        }
    }

    public boolean isUsernameClean(String username) {
        String lower = username.toLowerCase();
        return BAD_WORDS.stream().noneMatch(lower::contains);
    }
}

