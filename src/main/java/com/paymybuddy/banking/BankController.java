package com.paymybuddy.banking;

import com.paymybuddy.presentation.model.BankTransaction;

import java.math.BigDecimal;


/**
 * Interface for BankController
 *
 * Allows implementation of methods to connect with a banking API for processing payments
 */
public interface BankController {

    /**
     * Method to check if a payment has been sent to bank API yet, and if it has been processed
     * If payment has been processed by bank and is ready for funds to be added to user account, should return true
     * If this payment has not yet been requested to be processed by the bank, method should enter this request, and return false
     *
     * @param transaction BankTransaction object containing details of transaction to be investigated
     * @return true if payment has been processed, false if payment is pending or has just now been requested
     */
    public boolean isBankTransactionProcessed(BankTransaction transaction);

    /**
     * Method to interact with bank API and add profit gained from transaction fee to PayMyBuddy account
     *
     * @param fee BigDecimal value containing amount of funds to be added
     */
    public void addFee(BigDecimal fee);
}
