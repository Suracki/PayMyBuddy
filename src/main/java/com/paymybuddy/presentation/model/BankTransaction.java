package com.paymybuddy.presentation.model;

import com.paymybuddy.presentation.apimodels.BankTransactionDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BankTransaction {

    private int transactionID;
    private int acctID;
    private BigDecimal amount;
    private boolean processed;
    private boolean cancelled;
    private LocalDateTime transactionDate;
    private String IBAN;
    private String BIC;

    public BankTransaction() {}

    public BankTransaction(BankTransactionDTO bankTransactionDTO){
        transactionID = -1;
        this.acctID = bankTransactionDTO.acctID;
        this.amount = bankTransactionDTO.amount;
        this.processed = false;
        this.cancelled = false;
        this.transactionDate = LocalDateTime.now();
        this.IBAN = bankTransactionDTO.IBAN;
        this.BIC = bankTransactionDTO.BIC;
    }

    public BankTransaction(int acctID, BigDecimal amount, boolean processed, boolean cancelled) {
        this.acctID = acctID;
        this.amount = amount;
        this.processed = processed;
        this.cancelled = cancelled;
        this.transactionDate = LocalDateTime.now();
    }

    public BankTransaction(int acctID, BigDecimal amount, String bankAcctIBAN, String bankAcctBIC, boolean processed, boolean cancelled) {
        this.acctID = acctID;
        this.amount = amount;
        this.IBAN = bankAcctIBAN;
        this.BIC = bankAcctBIC;
        this.processed = processed;
        this.cancelled = cancelled;
        this.transactionDate = LocalDateTime.now();
    }

    public BankTransaction(int transactionID, int acctID, BigDecimal amount, String bankAcctIBAN, String bankAcctBIC, boolean processed, boolean cancelled, LocalDateTime transactionDate) {
        this.transactionID = transactionID;
        this.acctID = acctID;
        this.amount = amount;
        this.IBAN = bankAcctIBAN;
        this.BIC = bankAcctBIC;
        this.processed = processed;
        this.cancelled = cancelled;
        this.transactionDate = transactionDate;
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

    public String getIBAN() {
        return IBAN;
    }

    public void setIBAN(String IBAN) {
        this.IBAN = IBAN;
    }

    public String getBIC() {
        return BIC;
    }

    public void setBIC(String BIC) {
        this.BIC = BIC;
    }
}
