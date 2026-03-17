package com.example.smartbanking.controller;

import com.example.smartbanking.dto.ApiResponse;
import com.example.smartbanking.entity.AccountCreationRequest;
import com.example.smartbanking.service.AccountRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/accounts")
@Tag(name = "Admin - Accounts", description = "Admin operations for account approval")
public class AdminAccountController {

    private final AccountRequestService accountRequestService;

    public AdminAccountController(AccountRequestService accountRequestService) {
        this.accountRequestService = accountRequestService;
    }

    /**
     * List all pending account creation requests.
     */
    @Operation(summary = "Get all pending account requests")
    @GetMapping("/requests/pending")
    public ResponseEntity<List<AccountCreationRequest>> getPendingRequests() {
        return ResponseEntity.ok(accountRequestService.getPendingRequests());
    }

    /**
     * Approve an account creation request.
     */
    @Operation(summary = "Approve an account creation request")
    @PostMapping("/requests/{requestId}/approve")
    public ResponseEntity<ApiResponse> approveRequest(
            @PathVariable Long requestId,
            @RequestParam(required = false, defaultValue = "Approved") String remarks) {

        AccountCreationRequest updated = accountRequestService.approveRequest(requestId, remarks);
        return ResponseEntity.ok(new ApiResponse(
                "Account approved and activated.",
                updated.getReservedAccountNumber()
        ));
    }

    /**
     * Reject an account creation request.
     */
    @Operation(summary = "Reject an account creation request")
    @PostMapping("/requests/{requestId}/reject")
    public ResponseEntity<ApiResponse> rejectRequest(
            @PathVariable Long requestId,
            @RequestParam(required = false, defaultValue = "Rejected") String remarks) {

        accountRequestService.rejectRequest(requestId, remarks);
        return ResponseEntity.ok(new ApiResponse("Account request rejected."));
    }
}
