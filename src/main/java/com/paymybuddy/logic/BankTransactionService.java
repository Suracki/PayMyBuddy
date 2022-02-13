package com.paymybuddy.logic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nimbusds.jose.shaded.json.JSONArray;
import com.paymybuddy.data.dao.BankTransactionsDAO;
import com.paymybuddy.data.dao.UsersDAO;
import com.paymybuddy.exceptions.FailToAddUserFundsException;
import com.paymybuddy.exceptions.FailToSubtractUserFundsException;
import com.paymybuddy.exceptions.FailedToInsertException;
import com.paymybuddy.logic.gson.LocalDateTimeDeserializer;
import com.paymybuddy.logic.gson.LocalDateTimeSerializer;
import com.paymybuddy.presentation.model.BankTransaction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * BankTransactionService performs operations and generates ResponseEntities for the BankTransactionController endpoints
 */
@Service
public class BankTransactionService extends BaseService{

    private static final Logger logger = LogManager.getLogger("BankTransactionService");
    BankTransactionsDAO bankTransactionsDAO;
    UsersDAO usersDAO;

    @Autowired
    public BankTransactionService(BankTransactionsDAO bankTransactionsDAO, UsersDAO usersDAO) {
        this.usersDAO = usersDAO;
        this.bankTransactionsDAO = bankTransactionsDAO;
    }

    /**
     * Add funds to a User's balance from their bank account, or withdraw funds from balance to their bank.
     *
     * For withdrawals, funds are removed immediately. Bank transfer request is then entered for later processing by bank.
     * For adding funds, bank transfer request is entered. Funds will be added separately once that is processed.
     *
     * @param bankTransaction BankTransaction object containing details of desired transaction
     * @return ResponseEntity containing the output
     */
    public ResponseEntity<String> addOrRemoveFunds(BankTransaction bankTransaction){

        logger.info("Processing addOrRemoveFunds Bank Transaction request");
        //Is transaction a withdrawal
        if(bankTransaction.getAmount().compareTo(new BigDecimal("0")) < 0) {
            logger.info("Transaction is withdrawal, attempting to remove funds from User.");
            //Remove funds from user account to allow withdrawal
            //Use absolute (.abs) value of bigdecimal to convert from -ve to +ve
            try {
                usersDAO.subtractFunds(bankTransaction.getAcctID(), bankTransaction.getAmount().abs());
            }
            catch (FailToSubtractUserFundsException e) {
                //failed to remove funds
                ResponseEntity<String> response = new ResponseEntity<String>("Unable to add bank transaction. Failed to remove funds from user account.", new HttpHeaders(), HttpStatus.BAD_REQUEST);
                logger.error("Bank transaction failed to be added", response);
                return response;
            }
        }

        //Create bank transaction
        logger.info("Creating Bank Transaction record in database for later processing.");
        try {
            bankTransaction.setTransactionID(bankTransactionsDAO.addTransaction(bankTransaction));
        }
        catch (FailedToInsertException e) {
            if(bankTransaction.getAmount().compareTo(new BigDecimal("0")) < 0) {
                //Return funds if they had been removed
                try {
                    usersDAO.addFunds(bankTransaction.getAcctID(), bankTransaction.getAmount().abs());
                }
                catch (FailToAddUserFundsException f) {
                    ResponseEntity<String> response = new ResponseEntity<String>("Unable to add bank transaction. Failed to restore removed funds", new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
                    logger.error("Failed to create Bank Transaction in database. Funds ["+ bankTransaction.getAmount().abs() +"]removed from acctID ["+ bankTransaction.getAcctID()+ "]");
                    return response;
                }
            }
            ResponseEntity<String> response = new ResponseEntity<String>("Unable to add bank transaction.", new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
            logger.error("Failed to create Bank Transaction in database");
            return response;
        }


        //Build response
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
        builder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
        Gson gson = builder.setPrettyPrinting().create();
        String responseString = gson.toJson(bankTransaction);
        HttpHeaders responseHeaders = new HttpHeaders();

        ResponseEntity<String> response = new ResponseEntity<>(responseString, responseHeaders, HttpStatus.OK);
        logger.info("Bank Transaction created in database", response);
        return response;
    }

    /**
     * Get JSON details of all Bank Transactions for a specific AcctID
     *
     * @param acctID source AcctID
     * @return ResponseEntity containing the output
     */
    public ResponseEntity<String> getAllBankTransactionDetails(int acctID) {
        logger.info("Processing getAllSentPaymentDetails Transaction request to get details of all sent payments for a User");
        JSONArray json = bankTransactionsDAO.getAllBankTransactionDetails(acctID);

        if (json.size() == 0){
            //no transactions found
            ResponseEntity<String> response = ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            logger.error("No bank transactions found for provided TransactionID",response);
            return response;
        }

        //Build response
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();
        String responseString = gson.toJson(json);

        ResponseEntity<String> response = new ResponseEntity<String>(responseString, new HttpHeaders(), HttpStatus.OK);

        logger.info("Bank Transaction Details obtained", response);
        return response;
    }

    /**
     * Get details of a BankTransaction by TransactionID
     *
     * @param transactionID ID of transaction to be processed
     * @return ResponseEntity containing the output
     */
    public ResponseEntity<String> getBankTransactionByID(int transactionID) {
        logger.info("Processing getTransactionByID Transaction request to get all details of a Transaction entry");
        BankTransaction foundTransaction = bankTransactionsDAO.getTransactionDetails(transactionID);

        if (foundTransaction == null) {
            //Transaction could not be found with this ID
            ResponseEntity<String> response = ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            logger.error("BankTransaction not found with provided TransactionID",response);
            return response;
        }

        //Build response
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
        builder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
        Gson gson = builder.setPrettyPrinting().create();
        String responseString = gson.toJson(foundTransaction);
        ResponseEntity<String> response = new ResponseEntity<String>(responseString, new HttpHeaders(), HttpStatus.OK);

        logger.info("BankTransaction details obtained", response);
        return response;
    }
}
