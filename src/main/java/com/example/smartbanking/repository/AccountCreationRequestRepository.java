package com.example.smartbanking.repository;

import com.example.smartbanking.entity.AccountCreationRequest;
import com.example.smartbanking.entity.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountCreationRequestRepository extends JpaRepository<AccountCreationRequest, Long> {

    List<AccountCreationRequest> findByStatus(AccountStatus status);

    // IMPORTANT: Your entity has `User user`, not `Long userId`
    List<AccountCreationRequest> findByUser_Id(Long userId);

    // Needed to ensure uniqueness while reserving account numbers
    boolean existsByReservedAccountNumber(String reservedAccountNumber);
}