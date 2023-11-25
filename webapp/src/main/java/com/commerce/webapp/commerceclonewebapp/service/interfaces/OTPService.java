package com.commerce.webapp.commerceclonewebapp.service.interfaces;

public interface OTPService {

    public void sendOTP(String email, int retryAttempt);

    public boolean verifyOtp(String email, String json);
}
