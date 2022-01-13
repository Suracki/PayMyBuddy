package com.paymybuddy.logic;

import com.paymybuddy.banking.BankController;
import com.paymybuddy.data.dao.BankTransactionsDAO;
import com.paymybuddy.data.dao.UsersDAO;
import com.paymybuddy.exceptions.FailToAddUserFundsException;
import com.paymybuddy.exceptions.UpdateBalanceException;
import com.paymybuddy.presentation.model.BankTransaction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;

@Service
public class PaymentService extends BaseService {


    private static final Logger logger = LogManager.getLogger("PaymentService");
    @Autowired
    BankTransactionsDAO bankTransactionsDAO;
    @Autowired
    BankController bank;
    @Autowired
    UsersDAO usersDAO;

    public int processPendingBankTransactions() throws UpdateBalanceException {

        logger.info("processPendingBankTransactions() called");
        //Get all unprocessed bank transactions from database
        ArrayList<BankTransaction> pendingTransactions = bankTransactionsDAO.getUnprocessedTransactionDetails();
        ArrayList<Integer> cancelledTransactionIDs = new ArrayList<>();
        int processedTransactions = 0;
        logger.info("pendingTransactions: " + pendingTransactions.size());

        //For each unprocessed transaction, query bank to confirm if status has updated
        for (BankTransaction bankTransaction : pendingTransactions) {
            logger.info("processing TransactionID " + bankTransaction.getTransactionID());
            if(bank.isBankTransactionProcessed(bankTransaction)){
                //Transaction has been processed by bank
                logger.info("TransactionID " + bankTransaction.getTransactionID() + " confirmed processed by bank");

                logger.info("TransactionID [" + bankTransaction.getTransactionID() + "] amount: " + bankTransaction.getAmount());
                //If the transaction amount is +ve we need to add funds
                //If it is -ve (a withdrawal) the funds were removed when the transaction was created
                if(bankTransaction.getAmount().compareTo(new BigDecimal("0")) > 0) {
                    logger.info("TransactionID " + bankTransaction.getTransactionID() + " adding funds to user");
                    //Add funds to user
                    try {
                        int updatedRows = usersDAO.addFunds(bankTransaction.getAcctID(), bankTransaction.getAmount());
                        bankTransactionsDAO.markTransactionProcessed(bankTransaction.getTransactionID());
                        processedTransactions++;
                    }
                    catch (FailToAddUserFundsException e) {
                        //Unable to update funds; account has reached cap
                        //Set transaction to cancelled
                        logger.info("TransactionID " + bankTransaction.getTransactionID() + " failed to add funds to user, cancelling transaction");
                        bankTransactionsDAO.markTransactionCancelled(bankTransaction.getTransactionID());
                        cancelledTransactionIDs.add(bankTransaction.getTransactionID());

                    }
                }
                else {
                    //transaction was a withdrawal; funds have already been removed, we can just mark as processed
                    logger.info("TransactionID " + bankTransaction.getTransactionID() + " is a withdrawal, no fund adjustment required");
                    bankTransactionsDAO.markTransactionProcessed(bankTransaction.getTransactionID());
                    processedTransactions++;
                }

            }

        }

        //If we had any transactions fail to process, throw UpdateBalanceException
        if (cancelledTransactionIDs.size() > 0) {
            throw new UpdateBalanceException(cancelledTransactionIDs, processedTransactions);
        }
        else {
            return processedTransactions;
        }


    }

}
