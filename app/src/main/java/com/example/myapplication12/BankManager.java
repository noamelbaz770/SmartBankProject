package com.example.myapplication12;

import java.util.ArrayList;
import java.util.List;

public class BankManager {
    private ArrayList<BankAccount> accounts;
    private ArrayList<Transaction> transactions;

    public BankManager() {
        accounts = new ArrayList<>();
        transactions = new ArrayList<>();

        // Initialize default accounts for convenience
        try {
            accounts.add(new BankAccount("ACC-001", "Alice Smith", 1000.0));
            accounts.add(new BankAccount("ACC-002", "Bob Jones", 500.0));
            accounts.add(new BankAccount("ACC-003", "Charlie Brown", 250.0));
        } catch (InvalidAmountException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<BankAccount> getAccounts() {
        return accounts;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public BankAccount findAccount(String accountNumber) {
        for (BankAccount acc : accounts) {
            if (acc.getAccountNumber().equalsIgnoreCase(accountNumber)) {
                return acc;
            }
        }
        return null;
    }

    public void addAccount(BankAccount account) {
        if (account != null) {
            accounts.add(account);
        }
    }

    public void recordTransaction(String type, double amount, String description) {
        Transaction txn = new Transaction(type, amount, description);
        transactions.add(txn);
    }

    // Exception demonstration helpers
    public String testArithmeticException(int a, int b) {
        int result = 0;
        try {
            result = a / b;
            return "Result: " + result;
        } catch (ArithmeticException e) {
            return "Caught ArithmeticException: Division by zero is not allowed!";
        } finally {
            // finally block demo
            System.out.println("ArithmeticException test finally executed.");
        }
    }

    public String testNumberFormatException(String input) {
        try {
            double parsed = Double.parseDouble(input);
            return "Successfully parsed: " + parsed;
        } catch (NumberFormatException e) {
            return "Caught NumberFormatException: Invalid number format '" + input + "'";
        } finally {
            System.out.println("NumberFormatException test finally executed.");
        }
    }

    public String testNullPointerException() {
        try {
            BankAccount nullAcc = null;
            String owner = nullAcc.getOwnerName(); // Will throw NullPointerException
            return owner;
        } catch (NullPointerException e) {
            return "Caught NullPointerException: Attempted operation on a null object reference!";
        } finally {
            System.out.println("NullPointerException test finally executed.");
        }
    }

    public String testArrayIndexOutOfBoundsException() {
        try {
            String[] sampleAccounts = {"ACC-001", "ACC-002"};
            String acc = sampleAccounts[5]; // Will throw ArrayIndexOutOfBoundsException
            return acc;
        } catch (ArrayIndexOutOfBoundsException e) {
            return "Caught ArrayIndexOutOfBoundsException: Index 5 is out of bounds for length 2!";
        } finally {
            System.out.println("ArrayIndexOutOfBoundsException test finally executed.");
        }
    }

    public String testIllegalArgumentException() {
        try {
            BankAccount acc = accounts.get(0);
            acc.transfer(acc, 50.0); // Transfer to self throws IllegalArgumentException
            return "Transfer completed";
        } catch (Exception e) {
            return "Caught Exception (IllegalArgumentException/Custom): " + e.getMessage();
        } finally {
            System.out.println("IllegalArgumentException test finally executed.");
        }
    }
}
