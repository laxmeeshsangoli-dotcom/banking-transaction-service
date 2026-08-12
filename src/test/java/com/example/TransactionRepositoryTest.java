package com.example;

import com.example.model.Transaction;
import com.example.model.TransactionType;
import com.example.repository.InMemoryTransactionRepository;
import com.example.repository.TransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TransactionRepositoryTest {

    @Test
    public void shouldSaveTransaction() {

        TransactionRepository repository =
                new InMemoryTransactionRepository();

        Transaction transaction = new Transaction(
                "TXN-001",
                "ACC-100",
                TransactionType.DEPOSIT,
                BigDecimal.valueOf(500),
                null,
                LocalDateTime.now()
        );

        repository.save(transaction);

        List<Transaction> transactions =
                repository.findByAccountId("ACC-100");

        assertEquals(1, transactions.size());

        assertEquals(
                "TXN-001",
                transactions.get(0).getTransactionId()
        );
    }

    @Test
    public void shouldReturnEmptyListForUnknownAccount() {

        TransactionRepository repository =
                new InMemoryTransactionRepository();

        List<Transaction> transactions =
                repository.findByAccountId("ACC-999");

        assertNotNull(transactions);
        assertTrue(transactions.isEmpty());
    }

    @Test
    public void shouldStoreMultipleTransactionsForSameAccount() {

        TransactionRepository repository =
                new InMemoryTransactionRepository();

        Transaction deposit = new Transaction(
                "TXN-001",
                "ACC-100",
                TransactionType.DEPOSIT,
                BigDecimal.valueOf(500),
                null,
                LocalDateTime.now()
        );

        Transaction withdrawal = new Transaction(
                "TXN-002",
                "ACC-100",
                TransactionType.WITHDRAWAL,
                BigDecimal.valueOf(200),
                null,
                LocalDateTime.now()
        );

        repository.save(deposit);
        repository.save(withdrawal);

        List<Transaction> transactions =
                repository.findByAccountId("ACC-100");

        assertEquals(2, transactions.size());

        assertEquals(
                "TXN-001",
                transactions.get(0).getTransactionId()
        );

        assertEquals(
                "TXN-002",
                transactions.get(1).getTransactionId()
        );
    }

    @Test
    public void shouldKeepTransactionsOfDifferentAccountsSeparate() {

        TransactionRepository repository =
                new InMemoryTransactionRepository();

        Transaction transaction1 = new Transaction(
                "TXN-001",
                "ACC-100",
                TransactionType.DEPOSIT,
                BigDecimal.valueOf(500),
                null,
                LocalDateTime.now()
        );

        Transaction transaction2 = new Transaction(
                "TXN-002",
                "ACC-200",
                TransactionType.DEPOSIT,
                BigDecimal.valueOf(1000),
                null,
                LocalDateTime.now()
        );

        repository.save(transaction1);
        repository.save(transaction2);

        assertEquals(
                1,
                repository.findByAccountId("ACC-100").size()
        );

        assertEquals(
                1,
                repository.findByAccountId("ACC-200").size()
        );
    }
}