package com.paymybuddy.exceptions;

import com.paymybuddy.presentation.model.Transaction;

public class FailToMarkTransactionProcessedException extends Exception {

    Transaction transaction;

    public FailToMarkTransactionProcessedException(Transaction transaction) {
        this.transaction = transaction;
    }

}