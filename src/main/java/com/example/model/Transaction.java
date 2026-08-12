package com.example.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction {

    private final String transactionId;
    private final String accountId;
    private final TransactionType type;
    private final BigDecimal amount;
    private final String referenceAccountId;
    private final LocalDateTime timestamp;

    public Transaction(
            String transactionId,
            String accountId,
            TransactionType type,
            BigDecimal amount,
            String referenceAccountId,
            LocalDateTime timestamp) {

        this.transactionId = transactionId;
        this.accountId = accountId;
        this.type = type;
        this.amount = amount;
        this.referenceAccountId = referenceAccountId;
        this.timestamp = timestamp;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getAccountId() {
        return accountId;
    }

    public TransactionType getType() {
        return type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getReferenceAccountId() {
        return referenceAccountId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}