package com.paymybuddy.exceptions;

import java.math.BigDecimal;

public class NoEntriesFoundException  extends Exception {

    private String operation;

    public String getOperation() {
        return operation;
    }

    public NoEntriesFoundException(String operation) {
        this.operation = operation;
    }

}
