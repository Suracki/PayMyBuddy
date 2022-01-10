package com.paymybuddy.exceptions;

import com.paymybuddy.presentation.model.Transaction;

public class FailToMarkTransactionProcessedException extends Exception {

    private Transaction transaction;

    public Transaction getTransaction() {
        return transaction;
    }

    public FailToMarkTransactionProcessedException(Transaction transaction) {
        this.transaction = transaction;
    }

}