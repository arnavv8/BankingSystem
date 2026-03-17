package com.example.smartbanking.controller;

import com.example.smartbanking.dto.AccountCreateRequest;
import com.example.smartbanking.dto.ApiResponse;
import com.example.smartbanking.dto.RejectRequest;
import com.example.smartbanking.entity.Account;
import com.example.smartbanking.entity.AccountCreationRequest;
import com.example.smartbanking.service.AccountRequestService;
import com.example.smartbanking.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@Tag(name = "Accounts", description = "User bank account operations")
public class AccountController {

    private final AccountService accountService;
    private final AccountRequestService accountRequestService;

    public AccountController(AccountService accountService,
                             AccountRequestService accountRequestService) {
        this.accountService = accountService;
        this.accountRequestService = accountRequestService;
    }

    /**
     * User submits a new account creation request (PENDING).
     * We pre-reserve an account number to guarantee uniqueness on approval.
     */

	@Operation(
	    summary = "Request a new account (pending admin approval)",
	    requestBody = @RequestBody(
	        required = true,
	        content = @Content(
	            mediaType = "application/x-www-form-urlencoded",     // 👈 switch to form
	            schema = @Schema(implementation = AccountCreateRequest.class)
	        )
	    )
	)
	@PostMapping(
	    value = "/request",
	    consumes = "application/x-www-form-urlencoded"               // 👈 important
	)
	public ResponseEntity<ApiResponse> requestAccount(@Valid AccountCreateRequest request) {
	    // Spring will bind form fields to your DTO setters
	    var submitted = accountRequestService.submitRequest(request);
	    return ResponseEntity.ok(new ApiResponse(
	        "Account request submitted successfully. Awaiting admin approval.",
	        submitted.getReservedAccountNumber()
	    ));
	}


    /**
     * Get all account requests made by a user.
     */
    @Operation(summary = "Get all account requests for a user")
    @GetMapping("/requests/user/{userId}")
    public ResponseEntity<List<AccountCreationRequest>> getUserRequests(@PathVariable Long userId) {
        return ResponseEntity.ok(accountRequestService.getRequestsByUser(userId));
    }

    /**
     * (Admin) List all PENDING requests.
     * (If you still want filtering by status, see Option B)
     */
    @Operation(summary = "List all PENDING account requests (Admin)")
    @GetMapping("/requests/pending")
    public ResponseEntity<List<AccountCreationRequest>> listPendingRequests() {
        return ResponseEntity.ok(accountRequestService.getPendingRequests());
    }

    /**
     * (Admin) Approve request -> creates Account with the reserved account number.
     * Admin can pass optional remarks.
     */
    @Operation(summary = "Approve an account request (Admin)")
    @PostMapping("/requests/{requestId}/approve")
    public ResponseEntity<ApiResponse> approveRequest(
            @PathVariable Long requestId,
            @RequestParam(required = false, defaultValue = "Approved by admin") String adminRemarks) {

        AccountCreationRequest approved = accountRequestService.approveRequest(requestId, adminRemarks);
        return ResponseEntity.ok(new ApiResponse(
                "Account approved and created successfully",
                approved.getReservedAccountNumber()
        ));
    }

    /**
     * (Admin) Reject request with a reason.
     */
    @Operation(summary = "Reject an account request (Admin)")
    @PostMapping("/requests/{requestId}/reject")
    public ResponseEntity<ApiResponse> rejectRequest(
            @PathVariable Long requestId,
            @RequestBody @Valid RejectRequest body) {

        AccountCreationRequest rejected = accountRequestService.rejectRequest(requestId, body.getReason());
        return ResponseEntity.ok(new ApiResponse(
                "Account request rejected",
                rejected.getId()
        ));
    }

    // --- Existing account APIs still work as-is ---

    @Operation(summary = "Get all accounts for a user")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Account>> getUserAccounts(@PathVariable Long userId) {
        return ResponseEntity.ok(accountService.getAccountsForUser(userId));
    }

    @Operation(summary = "Get account details")
    @GetMapping("/{accountNumber}")
    public ResponseEntity<Account> getAccount(@PathVariable String accountNumber) {
        return ResponseEntity.ok(accountService.getAccountByNumber(accountNumber));
    }

    @Operation(summary = "Freeze an account (Admin only)")
    @PostMapping("/freeze/{accountNumber}")
    public ResponseEntity<ApiResponse> freezeAccount(@PathVariable String accountNumber) {
        accountService.freezeAccount(accountNumber);
        return ResponseEntity.ok(new ApiResponse("Account frozen successfully"));
    }

    @Operation(summary = "Unfreeze an account (Admin only)")
    @PostMapping("/unfreeze/{accountNumber}")
    public ResponseEntity<ApiResponse> unfreezeAccount(@PathVariable String accountNumber) {
        accountService.unfreezeAccount(accountNumber);
        return ResponseEntity.ok(new ApiResponse("Account unfrozen successfully"));
    }
}