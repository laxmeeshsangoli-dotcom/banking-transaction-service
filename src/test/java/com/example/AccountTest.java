package com.example;

import com.example.model.Account;
import com.example.exception.InsufficientBalanceException;
import com.example.exception.InvalidAmountException;

import java.math.BigDecimal;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AccountTest {

    @Test
    public void shouldCreateAccountWithInitialBalance() {

        Account account = new Account(
                "ACC-100",
                BigDecimal.valueOf(1000)
        );

        assertEquals(
                "ACC-100",
                account.getId()
        );

        assertEquals(
                BigDecimal.valueOf(1000),
                account.getBalance()
        );
    }

    @Test
    public void shouldDepositMoney() {

        Account account = new Account(
                "ACC-100",
                BigDecimal.valueOf(1000)
        );

        account.deposit(
                BigDecimal.valueOf(500)
        );

        assertEquals(
                BigDecimal.valueOf(1500),
                account.getBalance()
        );
    }

    @Test
    public void shouldWithdrawMoney() {

        Account account = new Account(
                "ACC-100",
                BigDecimal.valueOf(1000)
        );

        account.withdraw(
                BigDecimal.valueOf(300)
        );

        assertEquals(
                BigDecimal.valueOf(700),
                account.getBalance()
        );
    }

    @Test(expected = InsufficientBalanceException.class)
    public void shouldNotAllowOverdraft() {

        Account account = new Account(
                "ACC-100",
                BigDecimal.valueOf(1000)
        );

        account.withdraw(
                BigDecimal.valueOf(1500)
        );
    }

    @Test(expected = InvalidAmountException.class)
    public void shouldRejectZeroDeposit() {

        Account account = new Account(
                "ACC-100",
                BigDecimal.valueOf(1000)
        );

        account.deposit(BigDecimal.ZERO);
    }

    @Test(expected = InvalidAmountException.class)
    public void shouldRejectNegativeDeposit() {

        Account account = new Account(
                "ACC-100",
                BigDecimal.valueOf(1000)
        );

        account.deposit(
                BigDecimal.valueOf(-100)
        );
    }

    @Test(expected = InvalidAmountException.class)
    public void shouldRejectZeroWithdrawal() {

        Account account = new Account(
                "ACC-100",
                BigDecimal.valueOf(1000)
        );

        account.withdraw(BigDecimal.ZERO);
    }
}