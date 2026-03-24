package com.example.smartbanking.controller;

import com.example.smartbanking.dto.AccountCreateRequest;
import com.example.smartbanking.dto.ApiResponse;
import com.example.smartbanking.entity.Account;
import com.example.smartbanking.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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

    @Operation(summary = "Create a new account request for the logged-in user")
    @PostMapping
    public ResponseEntity<ApiResponse> createAccount(@Valid @RequestBody AccountCreateRequest request,
                                                     Authentication authentication) {
        Account created = accountService.createAccount(request, authentication.getName());
        return ResponseEntity.ok(new ApiResponse(
                "Account request submitted successfully. Awaiting admin approval.",
                created.getAccountNumber()
        ));
    }

    @Operation(summary = "Get all accounts for the logged-in user")
    @GetMapping("/me")
    public ResponseEntity<List<Account>> getMyAccounts(Authentication authentication) {
        return ResponseEntity.ok(accountService.getUserAccounts(authentication.getName()));
    }

    @Operation(summary = "Get account details by account number")
    @GetMapping("/{accountNumber}")
    public ResponseEntity<Account> getAccount(@PathVariable String accountNumber) {
        return ResponseEntity.ok(accountService.getAccountByNumber(accountNumber));
    }
}