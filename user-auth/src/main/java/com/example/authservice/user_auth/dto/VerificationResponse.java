package com.example.authservice.user_auth.dto;

public class VerificationResponse {
    public boolean emailValid;
    public boolean phoneValid;
    public boolean usernameClean;

    public boolean isEmailValid() {
        return emailValid;
    }

    public void setEmailValid(boolean emailValid) {
        this.emailValid = emailValid;
    }

    public boolean isUsernameClean() {
        return usernameClean;
    }

    public void setUsernameClean(boolean usernameClean) {
        this.usernameClean = usernameClean;
    }

    public boolean isPhoneValid() {
        return phoneValid;
    }

    public void setPhoneValid(boolean phoneValid) {
        this.phoneValid = phoneValid;
    }
}
