package com.example.authservice.user_auth.dto;

public class VerificationRequest {
    private String email;
    private String phone;
    private String region;
    private String username;

    public VerificationRequest(String email, String phone, String region, String username) {
        this.email = email;
        this.phone = phone;
        this.region = region;
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
