package com.example.smartbanking.service.impl;

import com.example.smartbanking.dto.AccountCreateRequest;
import com.example.smartbanking.entity.Account;
import com.example.smartbanking.entity.AccountStatus;
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

    @Override
    public Account createAccount(AccountCreateRequest request, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("Authenticated user not found: " + userEmail));

        Account account = new Account();
        account.setAccountNumber(generateUniqueAccountNumber());
        account.setAccountType(request.getAccountType());
        account.setUser(user);
        account.setStatus(AccountStatus.PENDING);
        account.setFrozen(false);

        return accountRepository.save(account);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Account> getUserAccounts(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("Authenticated user not found: " + userEmail));
        return accountRepository.findByUser_Id(user.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Account> getPendingAccounts() {
        return accountRepository.findByStatus(AccountStatus.PENDING);
    }

    @Override
    public void approveAccount(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountId));

        if (account.getStatus() == AccountStatus.APPROVED) {
            return;
        }

        if (account.getStatus() == AccountStatus.REJECTED) {
            throw new IllegalStateException("Rejected account cannot be approved.");
        }

        account.setStatus(AccountStatus.APPROVED);
        accountRepository.save(account);
    }

    @Override
    public void rejectAccount(Long accountId, String reason) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountId));

        if (account.getStatus() == AccountStatus.APPROVED) {
            throw new IllegalStateException("Approved account cannot be rejected.");
        }

        if (account.getStatus() == AccountStatus.REJECTED) {
            return;
        }

        account.setStatus(AccountStatus.REJECTED);
        accountRepository.save(account);
    }

    @Override
    @Transactional(readOnly = true)
    public Account getAccountByNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountNumber));
    }

    @Override
    public void freezeAccount(String accountNumber) {
        Account account = getAccountByNumber(accountNumber);
        if (Boolean.FALSE.equals(account.getFrozen())) {
            account.setFrozen(true);
            accountRepository.save(account);
        }
    }

    @Override
    public void unfreezeAccount(String accountNumber) {
        Account account = getAccountByNumber(accountNumber);
        if (Boolean.TRUE.equals(account.getFrozen())) {
            account.setFrozen(false);
            accountRepository.save(account);
        }
    }

    private String generateUniqueAccountNumber() {
        String acc;
        do {
            acc = "SB" + (10000000 + random.nextInt(90000000));
        } while (accountRepository.existsByAccountNumber(acc));
        return acc;
    }
}