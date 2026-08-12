package com.example.service;

import com.example.model.Account;
import com.example.model.Transaction;
import com.example.model.TransactionType;
import com.example.repository.AccountRepository;
import com.example.repository.TransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class BankingService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public BankingService(
            AccountRepository accountRepository,
            TransactionRepository transactionRepository) {

        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    /**
     * Creates a new account.
     */
    public Account createAccount(
            String accountId,
            BigDecimal initialBalance) {

        validateAccountId(accountId);

        Account existingAccount =
                accountRepository.findById(accountId);

        if (existingAccount != null) {
            throw new IllegalArgumentException(
                    "Account already exists: " + accountId
            );
        }

        Account account =
                new Account(accountId, initialBalance);

        accountRepository.save(account);

        return account;
    }

    /**
     * Returns the current balance of an account.
     */
    public BigDecimal getBalance(String accountId) {

        Account account =
                getAccount(accountId);

        return account.getBalance();
    }

    /**
     * Deposits money into an account.
     */
    public void deposit(
            String accountId,
            BigDecimal amount) {

        Account account =
                getAccount(accountId);

        account.deposit(amount);

        Transaction transaction =
                new Transaction(
                        generateTransactionId(),
                        accountId,
                        TransactionType.DEPOSIT,
                        amount,
                        null,
                        LocalDateTime.now()
                );

        transactionRepository.save(transaction);
    }

    /**
     * Withdraws money from an account.
     */
    public void withdraw(
            String accountId,
            BigDecimal amount) {

        Account account =
                getAccount(accountId);

        account.withdraw(amount);

        Transaction transaction =
                new Transaction(
                        generateTransactionId(),
                        accountId,
                        TransactionType.WITHDRAWAL,
                        amount,
                        null,
                        LocalDateTime.now()
                );

        transactionRepository.save(transaction);
    }

    /**
     * Transfers money from one account to another.
     *
     * Both accounts are locked in a consistent order
     * to avoid deadlocks when multiple threads perform
     * transfers simultaneously.
     */
    public void transfer(
            String fromAccountId,
            String toAccountId,
            BigDecimal amount) {

        validateAccountId(fromAccountId);
        validateAccountId(toAccountId);

        if (fromAccountId.equals(toAccountId)) {
            throw new IllegalArgumentException(
                    "Source and destination accounts must be different"
            );
        }

        validateAmount(amount);

        Account fromAccount =
                getAccount(fromAccountId);

        Account toAccount =
                getAccount(toAccountId);

        /*
         * Always lock accounts in a consistent order.
         * This prevents deadlock when two threads transfer
         * in opposite directions.
         */
        Account firstLock;
        Account secondLock;

        if (fromAccount.getId().compareTo(toAccount.getId()) < 0) {
            firstLock = fromAccount;
            secondLock = toAccount;
        } else {
            firstLock = toAccount;
            secondLock = fromAccount;
        }

        synchronized (firstLock) {

            synchronized (secondLock) {

                // Withdraw first.
                fromAccount.withdraw(amount);

                // Deposit to destination.
                toAccount.deposit(amount);

                LocalDateTime timestamp =
                        LocalDateTime.now();

                // Transaction for source account.
                Transaction transferOut =
                        new Transaction(
                                generateTransactionId(),
                                fromAccountId,
                                TransactionType.TRANSFER_OUT,
                                amount,
                                toAccountId,
                                timestamp
                        );

                // Transaction for destination account.
                Transaction transferIn =
                        new Transaction(
                                generateTransactionId(),
                                toAccountId,
                                TransactionType.TRANSFER_IN,
                                amount,
                                fromAccountId,
                                timestamp
                        );

                transactionRepository.save(
                        transferOut
                );

                transactionRepository.save(
                        transferIn
                );
            }
        }
    }

    /**
     * Returns transaction history for an account.
     */
    public List<Transaction> getTransactionHistory(
            String accountId) {

        getAccount(accountId);

        return transactionRepository
                .findByAccountId(accountId);
    }

    private Account getAccount(String accountId) {

        validateAccountId(accountId);

        Account account =
                accountRepository.findById(accountId);

        if (account == null) {
            throw new IllegalArgumentException(
                    "Account not found: " + accountId
            );
        }

        return account;
    }

    private void validateAccountId(String accountId) {

        if (accountId == null ||
                accountId.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Account ID cannot be empty"
            );
        }
    }

    private void validateAmount(BigDecimal amount) {

        if (amount == null ||
                amount.compareTo(BigDecimal.ZERO) <= 0) {

            throw new IllegalArgumentException(
                    "Amount must be greater than zero"
            );
        }
    }

    private String generateTransactionId() {

        return UUID.randomUUID().toString();
    }
}