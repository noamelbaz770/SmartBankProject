package com.example.myapplication12;

import android.util.Log;

public class BankAccount {
    private String accountNumber;
    private String ownerName;
    private double balance;

    public BankAccount(String accountNumber, String ownerName, double initialBalance) throws InvalidAmountException {
        if (initialBalance < 0) {
            throw new InvalidAmountException("Initial balance cannot be negative.");
        }
        this.accountNumber = accountNumber;
        this.ownerName = ownerName;
        this.balance = initialBalance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Deposit amount must be greater than zero.");
        }
        balance += amount;
    }

    public void withdraw(double amount) throws InsufficientFundsException, InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Withdrawal amount must be greater than zero.");
        }
        if (amount > balance) {
            throw new InsufficientFundsException("Insufficient funds! Available balance: $" + balance + ", Requested: $" + amount);
        }
        balance -= amount;
    }

    public void transfer(BankAccount target, double amount) throws InsufficientFundsException, InvalidAmountException, IllegalArgumentException, NullPointerException {
        if (target == null) {
            throw new NullPointerException("Target bank account cannot be null.");
        }
        if (this.accountNumber.equals(target.getAccountNumber())) {
            throw new IllegalArgumentException("Cannot transfer funds to the same account.");
        }
        if (amount <= 0) {
            throw new InvalidAmountException("Transfer amount must be greater than zero.");
        }
        if (amount > balance) {
            throw new InsufficientFundsException("Insufficient funds for transfer! Available balance: $" + balance);
        }

        // Perform withdrawal from source and deposit to target
        this.withdraw(amount);
        try {
            target.deposit(amount);
        } catch (InvalidAmountException e) {
            // Rollback if deposit fails (should not happen if amount > 0 verified above)
            this.balance += amount;
            throw e;
        }
    }

    public void printDetails() {
        Log.d("BankAccount", "Account: " + accountNumber + ", Owner: " + ownerName + ", Balance: $" + balance);
    }

    @Override
    public String toString() {
        return String.format("Acc: %s | Owner: %s | Balance: $%.2f", accountNumber, ownerName, balance);
    }
}
