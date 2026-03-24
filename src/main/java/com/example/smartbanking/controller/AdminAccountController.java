package com.example.smartbanking.controller;

import com.example.smartbanking.dto.ApiResponse;
import com.example.smartbanking.dto.RejectRequest;
import com.example.smartbanking.entity.Account;
import com.example.smartbanking.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/accounts")
@Tag(name = "Admin - Accounts", description = "Admin operations for account approval")
public class AdminAccountController {

    private final AccountService accountService;

    public AdminAccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @Operation(summary = "Get all pending account requests")
    @GetMapping("/pending")
    public ResponseEntity<List<Account>> getPendingAccounts() {
        return ResponseEntity.ok(accountService.getPendingAccounts());
    }

    @Operation(summary = "Approve a pending account")
    @PostMapping("/{id}/approve")
    public ResponseEntity<ApiResponse> approveAccount(@PathVariable Long id) {
        accountService.approveAccount(id);
        return ResponseEntity.ok(new ApiResponse("Account approved successfully"));
    }

    @Operation(summary = "Reject a pending account")
    @PostMapping("/{id}/reject")
    public ResponseEntity<ApiResponse> rejectAccount(@PathVariable Long id,
                                                     @RequestBody @Valid RejectRequest request) {
        accountService.rejectAccount(id, request.getReason());
        return ResponseEntity.ok(new ApiResponse("Account rejected successfully"));
    }

    @Operation(summary = "Freeze an account")
    @PostMapping("/freeze/{accountNumber}")
    public ResponseEntity<ApiResponse> freezeAccount(@PathVariable String accountNumber) {
        accountService.freezeAccount(accountNumber);
        return ResponseEntity.ok(new ApiResponse("Account frozen successfully"));
    }

    @Operation(summary = "Unfreeze an account")
    @PostMapping("/unfreeze/{accountNumber}")
    public ResponseEntity<ApiResponse> unfreezeAccount(@PathVariable String accountNumber) {
        accountService.unfreezeAccount(accountNumber);
        return ResponseEntity.ok(new ApiResponse("Account unfrozen successfully"));
    }
}