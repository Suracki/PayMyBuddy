package com.paymybuddy.banking;

import com.paymybuddy.presentation.model.BankTransaction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


/**
 * Mock implementation for BankController
 *
 * Allows system to operate without interfacing with actual banking API for development/testing purposes
 * Final version can add another implementation of BankController and instead mark that as @Primary to automate usage
 */
@Service
@Primary //Remove @Primary and place in real BankController class if implemented
public class MockBank implements BankController{


    private BigDecimal appProfit;
    private static final Logger logger = LogManager.getLogger("MockBank");

    public MockBank() {
        appProfit = new BigDecimal("0");
    }

    /**
     * Method to check if a payment has been sent to bank API yet, and if it has been processed
     * In current mock implementation, all transactions are assumed to have been approved at this stage
     * As such, method will always return true
     *
     * @param transaction BankTransaction object containing details of transaction to be investigated
     * @return true if payment has been processed, false if payment is pending or has just now been requested
     */
    public boolean isBankTransactionProcessed(BankTransaction transaction) {
        //Code would go here to interact with bank API and confirm transaction has been processed
        logger.info("Contacting bank for transaction [" + transaction.getTransactionID() + "], payment approved.");
        return true;
    }

    /**
     * Method to interact with bank API and add profit gained from transaction fee to PayMyBuddy account
     * In current mock implementation, will simply track how much profit has been gained in current runtime
     *
     * @param fee BigDecimal value containing amount of funds to be added
     */
    public void addFee(BigDecimal fee) {
        //Code would go here to interact with bank API and add profit to our account (or whatever process is appropriate)
        appProfit = appProfit.add(fee);
        logger.info("App Profit updated. Current profit: " + appProfit);
    }

}
