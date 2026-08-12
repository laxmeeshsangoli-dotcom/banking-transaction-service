package com.example.repository;

import com.example.model.Account;

import java.util.HashMap;
import java.util.Map;

public class InMemoryAccountRepository
        implements AccountRepository {

    private final Map<String, Account> accounts =
            new HashMap<>();

    @Override
    public synchronized void save(Account account) {

        if (account == null) {
            throw new IllegalArgumentException(
                    "Account cannot be null"
            );
        }

        accounts.put(
                account.getId(),
                account
        );
    }

    @Override
    public synchronized Account findById(
            String accountId) {

        return accounts.get(accountId);
    }
}