package com.example.smartbanking.util;

public class Constants {

    public static final int OTP_EXPIRY_MINUTES = 5; 
    public static final int MAX_OTP_ATTEMPTS = 3;

    public static final String ROLE_USER = "USER";
    public static final String ROLE_ADMIN = "ADMIN";

    public static final String TXN_PREFIX = "TXN-";

    public static final int MAX_DAILY_TRANSFERS = 10;
    public static final long HIGH_VALUE_ALERT = 50000L; // fraud trigger
}