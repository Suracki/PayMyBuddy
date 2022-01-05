package com.paymybuddy.presentation.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BankTransaction {

    private int transactionID;
    private int acctID;
    private BigDecimal amount;
    private boolean processed;
    private boolean cancelled;
    private LocalDateTime transactionDate;

    public BankTransaction() {}

    public BankTransaction(int acctID, BigDecimal amount, boolean processed, boolean cancelled) {
        this.acctID = acctID;
        this.amount = amount;
        this.processed = processed;
        this.cancelled = cancelled;
        this.transactionDate = LocalDateTime.now();
    }

    public int getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(int transactionID) {
        this.transactionID = transactionID;
    }

    public int getAcctID() {
        return acctID;
    }

    public void setAcctID(int acctID) {
        this.acctID = acctID;
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

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }
}
