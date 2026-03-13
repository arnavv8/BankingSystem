package com.example.smartbanking.controller;

import com.example.smartbanking.dto.AccountCreateRequest;
import com.example.smartbanking.dto.ApiResponse;
import com.example.smartbanking.entity.Account;
import com.example.smartbanking.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@Tag(name = "Accounts", description = "User bank account operations")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Create a new Savings/Current account for a user.
     */
    @Operation(summary = "Create a new account")
    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createAccount(@RequestBody AccountCreateRequest request) {

        Account created = accountService.createAccount(
                request.getUserId(),
                request.getAccountType()
        );

        return ResponseEntity.ok(
                new ApiResponse("Account created successfully", created.getAccountNumber())
        );
    }

    /**
     * Get all accounts of a user.
     */
    @Operation(summary = "Get all accounts for a user")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Account>> getUserAccounts(@PathVariable Long userId) {
        return ResponseEntity.ok(accountService.getAccountsForUser(userId));
    }

    /**
     * Get account details by account number.
     */
    @Operation(summary = "Get account details")
    @GetMapping("/{accountNumber}")
    public ResponseEntity<Account> getAccount(@PathVariable String accountNumber) {
        return ResponseEntity.ok(accountService.getAccountByNumber(accountNumber));
    }

    /**
     * Freeze an account — used by admin or fraud engine.
     */
    @Operation(summary = "Freeze an account (Admin only)")
    @PostMapping("/freeze/{accountNumber}")
    public ResponseEntity<ApiResponse> freezeAccount(@PathVariable String accountNumber) {
        accountService.freezeAccount(accountNumber);
        return ResponseEntity.ok(new ApiResponse("Account frozen successfully"));
    }

    /**
     * Unfreeze an account.
     */
    @Operation(summary = "Unfreeze an account (Admin only)")
    @PostMapping("/unfreeze/{accountNumber}")
    public ResponseEntity<ApiResponse> unfreezeAccount(@PathVariable String accountNumber) {
        accountService.unfreezeAccount(accountNumber);
        return ResponseEntity.ok(new ApiResponse("Account unfrozen successfully"));
    }
}