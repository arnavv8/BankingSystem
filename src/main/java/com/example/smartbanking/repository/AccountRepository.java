package com.example.smartbanking.repository;

import com.example.smartbanking.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Account findByAccountNumber(String accountNumber); // get one

    List<Account> findByUserId(Long userId); // all accounts of user
}