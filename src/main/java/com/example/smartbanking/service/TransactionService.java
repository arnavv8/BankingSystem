package com.example.smartbanking.service;

import com.example.smartbanking.entity.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {

    Transaction credit(String accountNumber, BigDecimal amount, String description); // add

    Transaction debit(String accountNumber, BigDecimal amount, String description);  // remove

    Transaction transfer(String fromAccount, String toAccount, BigDecimal amount, String description); // move

    List<Transaction> getTransactions(String accountNumber, int page, int size); // paged list

    List<Transaction> getTransactionsBetween(String accountNumber, LocalDateTime from, LocalDateTime to); // filter

    Transaction getByReferenceId(String referenceId); // find by ref

    BigDecimal getBalance(String accountNumber); // check balance
}