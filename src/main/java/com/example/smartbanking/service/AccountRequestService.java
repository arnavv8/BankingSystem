package com.example.smartbanking.service;

import com.example.smartbanking.dto.AccountCreateRequest;
import com.example.smartbanking.entity.AccountCreationRequest;

import java.util.List;

public interface AccountRequestService {

    AccountCreationRequest submitRequest(AccountCreateRequest dto, String userEmail);

    AccountCreationRequest approveRequest(Long requestId, String adminEmail, String adminRemarks);

    AccountCreationRequest rejectRequest(Long requestId, String adminEmail, String adminRemarks);

    List<AccountCreationRequest> getPendingRequests();

    List<AccountCreationRequest> getRequestsByUser(String userEmail);
}
