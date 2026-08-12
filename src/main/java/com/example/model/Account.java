package com.example.model;

import java.math.BigDecimal;

import com.example.exception.InsufficientBalanceException;
import com.example.exception.InvalidAmountException;

public class Account {

    private final String id;
    private BigDecimal balance;

    public Account(String id, BigDecimal initialBalance) {

        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Account ID cannot be empty"
            );
        }

        if (initialBalance == null ||
                initialBalance.compareTo(BigDecimal.ZERO) < 0) {

            throw new InvalidAmountException(
                    "Initial balance cannot be negative"
            );
        }

        this.id = id;
        this.balance = initialBalance;
    }

    public String getId() {
        return id;
    }

    public synchronized BigDecimal getBalance() {
        return balance;
    }

    public synchronized void deposit(BigDecimal amount) {

        validateAmount(amount);

        balance = balance.add(amount);
    }

    public synchronized void withdraw(BigDecimal amount) {

        validateAmount(amount);

        if (balance.compareTo(amount) < 0) {

            throw new InsufficientBalanceException(
                    "Insufficient balance"
            );
        }

        balance = balance.subtract(amount);
    }

    private void validateAmount(BigDecimal amount) {

        if (amount == null ||
                amount.compareTo(BigDecimal.ZERO) <= 0) {

            throw new InvalidAmountException(
                    "Amount must be greater than zero"
            );
        }
    }
}