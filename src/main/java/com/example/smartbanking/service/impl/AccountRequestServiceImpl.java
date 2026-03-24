package com.example.smartbanking.service.impl;

import com.example.smartbanking.dto.AccountCreateRequest;
import com.example.smartbanking.entity.*;
import com.example.smartbanking.repository.AccountCreationRequestRepository;
import com.example.smartbanking.repository.AccountRepository;
import com.example.smartbanking.repository.UserRepository;
import com.example.smartbanking.service.AccountRequestService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
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

    // ============================================================
    // SUBMIT ACCOUNT CREATION REQUEST (USER)
    // ============================================================
    @Override
    public AccountCreationRequest submitRequest(AccountCreateRequest dto, String userEmail) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() ->
                        new RuntimeException("Authenticated user not found: " + userEmail));

        AccountCreationRequest req = new AccountCreationRequest();
        req.setUser(user);
        req.setName(dto.getName());
        req.setAddress(dto.getAddress());
        req.setPhoneNumber(dto.getPhoneNumber());
        req.setAccountType(dto.getAccountType());
        req.setStatus(AccountStatus.PENDING);

        // Generate and reserve account number
        req.setReservedAccountNumber(generateAccountNumber());

        return requestRepository.save(req);
    }

    // ============================================================
    // APPROVE REQUEST (ADMIN)
    // ANY ONE ADMIN CAN APPROVE
    // ============================================================
    @Override
    public AccountCreationRequest approveRequest(Long requestId,
                                                 String adminEmail,
                                                 String adminRemarks) {

        AccountCreationRequest req = findById(requestId);

        // ✅ FIXED: Use AccountStatus
        if (req.getStatus() != AccountStatus.PENDING) {
            throw new RuntimeException("Request is not in PENDING state.");
        }

        // Validate admin
        User admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new RuntimeException("Admin not found: " + adminEmail));

        if (admin.getRoles().contains(Role.ADMIN)){
            throw new RuntimeException("Only admins can approve requests");
        }

        // Create actual account
        Account account = new Account();
        account.setAccountNumber(req.getReservedAccountNumber());
        account.setAccountType(req.getAccountType());
        account.setUser(req.getUser());
        account.setStatus(AccountStatus.APPROVED);
        account.setFrozen(false);

        accountRepository.save(account);

        // Update request
        req.setStatus(AccountStatus.APPROVED);
        req.setReviewedAt(LocalDateTime.now());
        req.setAdminRemarks(adminRemarks);

        return requestRepository.save(req);
    }

    // ============================================================
    // REJECT REQUEST (ADMIN)
    // ============================================================
    @Override
    public AccountCreationRequest rejectRequest(Long requestId,
                                                String adminEmail,
                                                String adminRemarks) {

        AccountCreationRequest req = findById(requestId);

        if (req.getStatus() != AccountStatus.PENDING) {
            throw new RuntimeException("Request is not in PENDING state.");
        }

        // Validate admin
        User admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new RuntimeException("Admin not found: " + adminEmail));

        if (admin.getRoles().contains(Role.ADMIN)) {
            throw new RuntimeException("Only admins can reject requests");
        }

        req.setStatus(AccountStatus.REJECTED);
        req.setReviewedAt(LocalDateTime.now());
        req.setAdminRemarks(adminRemarks);

        return requestRepository.save(req);
    }

    // ============================================================
    // GET ALL PENDING REQUESTS (ADMIN)
    // ============================================================
    @Override
    @Transactional(readOnly = true)
    public List<AccountCreationRequest> getPendingRequests() {
        return requestRepository.findByStatus(AccountStatus.PENDING);
    }

    // ============================================================
    // GET USER'S REQUESTS
    // ============================================================
    @Override
    @Transactional(readOnly = true)
    public List<AccountCreationRequest> getRequestsByUser(String userEmail) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() ->
                        new RuntimeException("User not found: " + userEmail));

        return requestRepository.findByUser_Id(user.getId());
    }

    // ============================================================
    // HELPER METHODS
    // ============================================================
    private AccountCreationRequest findById(Long id) {
        return requestRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Account request not found: " + id));
    }

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