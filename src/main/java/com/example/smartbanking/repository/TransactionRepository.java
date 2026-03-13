package com.example.smartbanking.repository;

import com.example.smartbanking.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByAccountAccountNumber(String accountNumber); // all txns

    List<Transaction> findByAccountAccountNumberOrderByTimestampDesc(
            String accountNumber); // sorted

    List<Transaction> findByAccountAccountNumberAndTimestampBetween(
            String accountNumber,
            LocalDateTime from,
            LocalDateTime to); // date filter

    Transaction findByReferenceId(String referenceId); // unique lookup
}