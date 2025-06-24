package com.example.verification_service.dto;

public class VerificationResponse {
    private boolean emailValid;
    private boolean phoneValid;
    private boolean usernameClean;

    public VerificationResponse(boolean emailValid, boolean phoneValid, boolean usernameClean) {
        this.emailValid = emailValid;
        this.phoneValid = phoneValid;
        this.usernameClean = usernameClean;
    }

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
