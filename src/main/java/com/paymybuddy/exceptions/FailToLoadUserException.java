package com.paymybuddy.exceptions;

public class FailToLoadUserException extends Exception {

    private int acctID;

    public int getAcctID() {
        return acctID;
    }

    public FailToLoadUserException(int acctID) {
        this.acctID = acctID;
    }

}
