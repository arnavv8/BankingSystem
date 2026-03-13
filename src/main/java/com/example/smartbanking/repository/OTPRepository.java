package com.example.smartbanking.repository;

import com.example.smartbanking.entity.OTP;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OTPRepository extends JpaRepository<OTP, Long> {

    OTP findTopByUserEmailAndUsedFalseOrderByExpiresAtDesc(String email); // latest active otp

    List<OTP> findByExpiresAtBefore(LocalDateTime time); // cleanup
}