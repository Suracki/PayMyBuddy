package com.paymybuddy.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction {
    private int transactionID;
    private int fromAcctID;
    private int toAcctID;
    private LocalDateTime transactionDate;
    private String description;
    private BigDecimal amount;
    private boolean processed;

    public Transaction() {
    }

    public Transaction(int transactionID, int fromAcctID, int toAcctID, LocalDateTime transactionDate, String description, BigDecimal amount, boolean processed) {
        this.transactionID = transactionID;
        this.fromAcctID = fromAcctID;
        this.toAcctID = toAcctID;
        this.transactionDate = transactionDate;
        this.description = description;
        this.amount = amount;
        this.processed = processed;
    }

    public Transaction(int fromAcctID, int toAcctID, LocalDateTime transactionDate, String description, BigDecimal amount, boolean processed) {
        this.fromAcctID = fromAcctID;
        this.toAcctID = toAcctID;
        this.transactionDate = transactionDate;
        this.description = description;
        this.amount = amount;
        this.processed = processed;
    }

    public int getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(int transactionID) {
        this.transactionID = transactionID;
    }

    public int getFromAcctID() {
        return fromAcctID;
    }

    public void setFromAcctID(int fromAcctID) {
        this.fromAcctID = fromAcctID;
    }

    public int getToAcctID() {
        return toAcctID;
    }

    public void setToAcctID(int toAcctID) {
        this.toAcctID = toAcctID;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }
}
