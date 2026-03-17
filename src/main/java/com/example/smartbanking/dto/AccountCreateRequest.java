
package com.example.smartbanking.dto;

import com.example.smartbanking.entity.AccountType;

public class AccountCreateRequest {

    private Long userId;        // ID of the logged-in user
    private String name;        // Full name of the applicant
    private String address;     // Residential address
    private String phoneNumber; // Contact phone number
    private AccountType accountType; // SAVINGS or CURRENT

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public AccountType getAccountType() { return accountType; }
    public void setAccountType(AccountType accountType) { this.accountType = accountType; }
}