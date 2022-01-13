package com.paymybuddy.exceptions;

public class FailedToSelectException  extends Exception {

    private String operation;

    public String getOperation() {
        return operation;
    }

    public FailedToSelectException(String operation) {
        this.operation = operation;
    }

}