package com.example.smartbanking.service;

import com.example.smartbanking.entity.Account;
import com.example.smartbanking.entity.AccountType;

import java.util.List;

public interface AccountService {

    /**
     * Creates a new account (SAVINGS / CURRENT) for a user.
     *
     * @param userId the user for whom the account is created
     * @param accountType type of account (SAVINGS, CURRENT)
     * @return created Account entity
     */
    Account createAccount(Long userId, AccountType accountType);

    /**
     * Returns all accounts belonging to a specific user.
     *
     * @param userId ID of the user
     * @return list of accounts
     */
    List<Account> getAccountsForUser(Long userId);

    /**
     * Returns account details by account number.
     *
     * @param accountNumber string account number
     * @return Account
     */
    Account getAccountByNumber(String accountNumber);

    /**
     * Freezes the account (used by admin or fraud detection engine).
     *
     * @param accountNumber account to freeze
     */
    void freezeAccount(String accountNumber);

    /**
     * Unfreezes a previously frozen account.
     *
     * @param accountNumber account to unfreeze
     */
    void unfreezeAccount(String accountNumber);
}
