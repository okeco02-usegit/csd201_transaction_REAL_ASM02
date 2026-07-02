package model;

import java.util.Date;

public class Transaction {
    private String transactionId;
    private String type; // "Deposit", "Withdraw", "Transfer"
    private double amount;
    private Date timestamp;
    private String description;

    public Transaction(String transactionId, String type, double amount, String description) {
        this.transactionId = transactionId;
        this.type = type;
        this.amount = amount;
        this.timestamp = new Date(); 
        this.description = description;
    }

    public Transaction(String transactionId, String type, double amount, Date timestamp, String description) {
        this.transactionId = transactionId;
        this.type = type;
        this.amount = amount;
        this.timestamp = timestamp;
        this.description = description;
    }

    public String getTransactionId() { return transactionId; }
    public String getType() { return type; }
    public double getAmount() { return amount; }
    public Date getTimestamp() { return timestamp; }
    public String getDescription() { return description; }

    @Override
    public String toString() {
        return String.format("[%s] ID: %s | %s: $%.2f | %s", 
            timestamp.toString(), transactionId, type, amount, description);
    }
}