package com.paymybuddy.exceptions;

import com.paymybuddy.presentation.model.Transaction;

public class FailToCreateTransactionRecordException extends Exception {

    private Transaction transaction;

    public Transaction getTransaction() {
        return transaction;
    }

    public FailToCreateTransactionRecordException(Transaction transaction) {
        this.transaction = transaction;
    }

}

