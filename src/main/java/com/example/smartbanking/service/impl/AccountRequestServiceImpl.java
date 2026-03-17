package com.example.smartbanking.service.impl;

import com.example.smartbanking.dto.AccountCreateRequest;
import com.example.smartbanking.entity.Account;
import com.example.smartbanking.entity.AccountCreationRequest;
import com.example.smartbanking.entity.AccountType;
import com.example.smartbanking.entity.RequestStatus;
import com.example.smartbanking.entity.User;
import com.example.smartbanking.repository.AccountCreationRequestRepository;
import com.example.smartbanking.repository.AccountRepository;
import com.example.smartbanking.repository.UserRepository;
import com.example.smartbanking.service.AccountRequestService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AccountRequestServiceImpl implements AccountRequestService {

    private final AccountCreationRequestRepository requestRepository;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public AccountRequestServiceImpl(AccountCreationRequestRepository requestRepository,
                                     AccountRepository accountRepository,
                                     UserRepository userRepository) {
        this.requestRepository = requestRepository;
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public AccountCreationRequest submitRequest(AccountCreateRequest dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found: " + dto.getUserId()));

        AccountCreationRequest req = new AccountCreationRequest();
        req.setUser(user);
        req.setName(dto.getName());
        req.setAddress(dto.getAddress());
        req.setPhoneNumber(dto.getPhoneNumber());
        req.setAccountType(dto.getAccountType());
        req.setStatus(RequestStatus.PENDING);

        // Auto‑generate a unique account number and reserve it immediately
        req.setReservedAccountNumber(generateAccountNumber());

        return requestRepository.save(req);
    }

    @Override
    @Transactional
    public AccountCreationRequest approveRequest(Long requestId, String adminRemarks) {
        AccountCreationRequest req = findById(requestId);

        if (req.getStatus() != RequestStatus.PENDING) {
            throw new RuntimeException("Request is not in PENDING state.");
        }

        // Create the actual Account using the reserved number (constructor already exists in your Account entity)
        Account account = new Account(
                req.getReservedAccountNumber(),
                req.getAccountType(),
                req.getUser()
        );
        accountRepository.save(account);

        // Update request status
        req.setStatus(RequestStatus.APPROVED);
        req.setReviewedAt(LocalDateTime.now());
        req.setAdminRemarks(adminRemarks);

        return requestRepository.save(req);
    }

    @Override
    @Transactional
    public AccountCreationRequest rejectRequest(Long requestId, String adminRemarks) {
        AccountCreationRequest req = findById(requestId);

        if (req.getStatus() != RequestStatus.PENDING) {
            throw new RuntimeException("Request is not in PENDING state.");
        }

        req.setStatus(RequestStatus.REJECTED);
        req.setReviewedAt(LocalDateTime.now());
        req.setAdminRemarks(adminRemarks);

        return requestRepository.save(req);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountCreationRequest> getPendingRequests() {
        return requestRepository.findByStatus(RequestStatus.PENDING);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountCreationRequest> getRequestsByUser(Long userId) {
        return requestRepository.findByUser_Id(userId);
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private AccountCreationRequest findById(Long id) {
        return requestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account request not found: " + id));
    }

    /**
     * Generates a unique 12‑character account number.
     * Format: "ACC" + 9 random digits, e.g. ACC847261930
     */
    private String generateAccountNumber() {
        String number;
        do {
            long random = (long) (Math.random() * 9_000_000_000L) + 1_000_000_000L;
            number = "ACC" + random;
        } while (accountRepository.existsByAccountNumber(number)
                || requestRepository.existsByReservedAccountNumber(number));
        return number;
    }
}