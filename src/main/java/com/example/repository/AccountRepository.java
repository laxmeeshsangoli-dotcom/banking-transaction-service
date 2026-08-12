package com.example.repository;

import com.example.model.Account;

public interface AccountRepository {

    void save(Account account);

    Account findById(String accountId);
}