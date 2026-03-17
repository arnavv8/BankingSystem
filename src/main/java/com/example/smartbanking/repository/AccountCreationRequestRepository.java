package com.example.smartbanking.repository;

import com.example.smartbanking.entity.AccountCreationRequest;
import com.example.smartbanking.entity.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountCreationRequestRepository extends JpaRepository<AccountCreationRequest, Long> {

    // Either name is OK in Spring Data (findBy... or findAllBy...).
    // Keep one style consistently; both return List<AccountCreationRequest>.
    List<AccountCreationRequest> findByStatus(RequestStatus status);
    // List<AccountCreationRequest> findAllByStatus(RequestStatus status); // alternative

    // IMPORTANT: your entity has 'User user', not 'Long userId'
    // So use the association path "user.id"
    List<AccountCreationRequest> findByUser_Id(Long userId);

    // Useful to ensure the reserved number is unique across pending/approved requests
    boolean existsByReservedAccountNumber(String reservedAccountNumber);
}