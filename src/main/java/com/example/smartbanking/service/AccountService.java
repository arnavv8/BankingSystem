package com.example.smartbanking.service;

import com.example.smartbanking.dto.AccountCreateRequest;
import com.example.smartbanking.entity.Account;

import java.util.List;

public interface AccountService {

    Account createAccount(AccountCreateRequest request, String userEmail);

    List<Account> getUserAccounts(String userEmail);

    List<Account> getPendingAccounts();

    void approveAccount(Long accountId);

    void rejectAccount(Long accountId, String reason);

    Account getAccountByNumber(String accountNumber);

    void freezeAccount(String accountNumber);

    void unfreezeAccount(String accountNumber);
}
