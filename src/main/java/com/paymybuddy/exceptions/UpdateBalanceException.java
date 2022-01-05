package com.paymybuddy.exceptions;

import java.util.ArrayList;

public class UpdateBalanceException extends Exception {

    private ArrayList<Integer> cancelledTransactions;
    private int successfulTransactions;

    public ArrayList<Integer> getCancelledTransactions() {
        return cancelledTransactions;
    }

    public int getSuccessfulTransactions() {
        return successfulTransactions;
    }

    public UpdateBalanceException(ArrayList<Integer> cancelledTransactions, int successfulTransactions) {
        this.cancelledTransactions = cancelledTransactions;
        this.successfulTransactions = successfulTransactions;
    }

}
