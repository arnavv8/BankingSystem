package com.example.smartbanking.service.impl;

import com.example.smartbanking.entity.Account;
import com.example.smartbanking.entity.AccountType;
import com.example.smartbanking.entity.User;
import com.example.smartbanking.exception.AccountNotFoundException;
import com.example.smartbanking.repository.AccountRepository;
import com.example.smartbanking.repository.UserRepository;
import com.example.smartbanking.service.AccountService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.List;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final SecureRandom random = new SecureRandom();

    public AccountServiceImpl(AccountRepository accountRepository,
                              UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    // -----------------------------
    // CREATE ACCOUNT
    // -----------------------------
    @Override
    public Account createAccount(Long userId, AccountType accountType) {

        // 1) Load the User entity (Account has @ManyToOne User)
        User user = userRepository.findById(userId)
        		.orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        // 2) Generate a unique account number
        String accountNumber = generateUniqueAccountNumber();

        // 3) Build and save Account
        Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setAccountType(accountType);
        account.setUser(user);
        // balance, createdAt, frozen are defaulted in your entity

        return accountRepository.save(account);
    }

    private String generateUniqueAccountNumber() {
        String acc;
        do {
            acc = "SB" + (10000000 + random.nextInt(90000000)); // e.g., SB12345678
        } while (accountRepository.existsByAccountNumber(acc));
        return acc;
    }

    // -----------------------------
    // GET ALL ACCOUNTS FOR A USER
    // -----------------------------
    @Override
    @Transactional(readOnly = true)
    public List<Account> getAccountsForUser(Long userId) {
        // Note: repository method navigates association path user.id
        return accountRepository.findByUser_Id(userId);
    }

    // -----------------------------
    // GET SINGLE ACCOUNT
    // -----------------------------
    @Override
    @Transactional(readOnly = true)
    public Account getAccountByNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(
                        "Account not found: " + accountNumber
                ));
    }

    // -----------------------------
    // FREEZE ACCOUNT
    // -----------------------------
    @Override
    public void freezeAccount(String accountNumber) {
        Account account = getAccountByNumber(accountNumber);
        if (Boolean.FALSE.equals(account.getFrozen())) {
            account.setFrozen(true);
            accountRepository.save(account);
        }
    }

    // -----------------------------
    // UNFREEZE ACCOUNT
    // -----------------------------
    @Override
    public void unfreezeAccount(String accountNumber) {
        Account account = getAccountByNumber(accountNumber);
        if (Boolean.TRUE.equals(account.getFrozen())) {
            account.setFrozen(false);
            accountRepository.save(account);
        }
    }
}