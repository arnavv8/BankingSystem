package com.example.smartbanking.service;

import com.example.smartbanking.dto.AccountCreateRequest;
import com.example.smartbanking.entity.AccountCreationRequest;

import java.util.List;

public interface AccountRequestService {

    /** User submits an account creation request; stays PENDING until admin acts */
    AccountCreationRequest submitRequest(AccountCreateRequest request);

    /** Admin approves: activates the reserved account */
    AccountCreationRequest approveRequest(Long requestId, String adminRemarks);

    /** Admin rejects the request */
    AccountCreationRequest rejectRequest(Long requestId, String adminRemarks);

    /** List all pending requests (for admin dashboard) */
    List<AccountCreationRequest> getPendingRequests();

    /** List all requests made by a specific user */
    List<AccountCreationRequest> getRequestsByUser(Long userId);
}
