package com.example.myapplication12;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Transaction {
    private String transactionId;
    private String type; // DEPOSIT, WITHDRAW, TRANSFER
    private double amount;
    private String description;
    private String timestamp;

    public Transaction(String type, double amount, String description) {
        this.transactionId = "TXN_" + System.currentTimeMillis();
        this.type = type;
        this.amount = amount;
        this.description = description;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        this.timestamp = sdf.format(new Date());
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public String getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s: $%.2f - %s", timestamp, type, amount, description);
    }
}
