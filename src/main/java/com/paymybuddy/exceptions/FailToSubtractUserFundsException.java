package com.paymybuddy.exceptions;

import java.math.BigDecimal;

public class FailToSubtractUserFundsException extends Exception {

    private int acctID;
    private BigDecimal amount;

    public int getAcctID() {
        return acctID;
    }

    public FailToSubtractUserFundsException(int acctID, BigDecimal amount) {
        this.acctID = acctID;
        this.amount = amount;
    }

}

