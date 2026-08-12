package com.example.repository;

import com.example.model.Transaction;

import java.util.List;

public interface TransactionRepository {

    void save(Transaction transaction);

    List<Transaction> findByAccountId(String accountId);
}