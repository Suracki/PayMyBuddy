package com.paymybuddy.logic;

import com.paymybuddy.banking.MockBank;
import com.paymybuddy.data.dao.BankTransactionsDAO;
import com.paymybuddy.data.dao.UsersDAO;
import com.paymybuddy.exceptions.UpdateBalanceException;
import com.paymybuddy.presentation.model.BankTransaction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;

@Service
public class PaymentService {


    private static final Logger logger = LogManager.getLogger("PaymentService");
    @Autowired
    BankTransactionsDAO bankTransactionsDAO;
    @Autowired
    MockBank bank;
    @Autowired
    UsersDAO usersDAO;

    public int processPendingBankTransactions() throws UpdateBalanceException {

        logger.info("processPendingBankTransactions() called");
        //Get all unprocessed bank transactions from database
        ArrayList<Integer> pendingTransactionIDs = bankTransactionsDAO.getAllUnprocessedTransactionIDs();
        ArrayList<Integer> cancelledTransactionIDs = new ArrayList<>();
        int processedTransactions = 0;
        logger.info("pendingTransactionIDs: " + pendingTransactionIDs.size());

        //For each unprocessed transaction, query bank to confirm if status has updated
        for (Integer id : pendingTransactionIDs) {
            logger.info("processing TransactionID " + id);
            if(bank.isBankTransactionProcessed(id)){
                //Transaction has been processed by bank
                logger.info("TransactionID " + id + " confirmed processed by bank");

                //Get transaction details
                BankTransaction bankTransaction = bankTransactionsDAO.getTransactionDetails(id);

                logger.info("TransactionID [" + bankTransaction.getTransactionID() + "] amount: " + bankTransaction.getAmount());
                //If the transaction amount is +ve we need to add funds
                //If it is -ve (a withdrawal) the funds were removed when the transaction was created
                if(bankTransaction.getAmount().compareTo(new BigDecimal("0")) > 0) {
                    logger.info("TransactionID " + id + " adding funds to user");
                    //Add funds to user
                    int updatedRows = usersDAO.addFunds(bankTransaction.getAcctID(), bankTransaction.getAmount());
                    if (updatedRows == -1) {
                        //Unable to update funds; account has reached cap
                        //Set transaction to cancelled
                        logger.info("TransactionID " + id + " failed to add funds to user, cancelling transaction");
                        bankTransactionsDAO.markTransactionCancelled(id);
                        cancelledTransactionIDs.add(id);
                    }
                    else {
                        //Funds have been added, mark transaction as processed
                        bankTransactionsDAO.markTransactionProcessed(id);
                        processedTransactions++;
                    }
                }
                else {
                    //transaction was a withdrawal; funds have already been removed, we can just mark as processed
                    logger.info("TransactionID " + id + " is a withdrawal, no fund adjustment required");
                    bankTransactionsDAO.markTransactionProcessed(id);
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
