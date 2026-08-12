package com.example.repository;

import com.example.model.Transaction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTransactionRepository
        implements TransactionRepository {

    private final Map<String, List<Transaction>> transactions =
            new HashMap<>();

    @Override
    public synchronized void save(Transaction transaction) {

        if (transaction == null) {
            throw new IllegalArgumentException(
                    "Transaction cannot be null"
            );
        }

        transactions
                .computeIfAbsent(
                        transaction.getAccountId(),
                        key -> new ArrayList<>()
                )
                .add(transaction);
    }

    @Override
    public synchronized List<Transaction> findByAccountId(
            String accountId) {

        if (accountId == null || accountId.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Account ID cannot be empty"
            );
        }

        List<Transaction> accountTransactions =
                transactions.get(accountId);

        if (accountTransactions == null) {
            return Collections.emptyList();
        }

        // Return a copy so callers cannot modify the repository
        return new ArrayList<>(accountTransactions);
    }
}