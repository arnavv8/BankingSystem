package com.example.smartbanking.service;

import com.example.smartbanking.entity.User;
import com.example.smartbanking.entity.OTP;

public interface OTPService {

    OTP generateAndSendOtp(User user);   // create + email OTP

    boolean validateOtp(String email, String code);   // check OTP

    void markOtpUsed(OTP otp);   // prevent reuse

    void removeExpiredOtps();   // cleanup
}