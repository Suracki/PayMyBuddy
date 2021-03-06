package com.paymybuddy.logic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nimbusds.jose.shaded.json.JSONArray;
import com.paymybuddy.banking.BankController;
import com.paymybuddy.data.dao.TransactionDAO;
import com.paymybuddy.data.dao.UsersDAO;
import com.paymybuddy.exceptions.*;
import com.paymybuddy.logic.gson.LocalDateTimeDeserializer;
import com.paymybuddy.logic.gson.LocalDateTimeSerializer;
import com.paymybuddy.presentation.model.Transaction;
import com.paymybuddy.presentation.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * TransactionService performs operations and generates ResponseEntities for the TransactionController endpoints
 */
@Service
public class TransactionService extends BaseService {

    private BankController bank;
    private TransactionDAO transactionDAO;
    private UsersDAO usersDAO;
    private static final Logger logger = LogManager.getLogger("TransactionService");

    @Autowired
    public TransactionService(TransactionDAO transactionDAO, UsersDAO usersDAO, BankController bank) {
        this.transactionDAO = transactionDAO;
        this.usersDAO = usersDAO;
        this.bank = bank;
    }

    /**
     * Perform a User to User funds transaction
     *
     * Will remove funds from sender, add funds to recipient, and create a Transaction entry as a record of the payment
     * Will attempt to rollback all changes if the process fails at any point
     *
     * However, if the payment is fully processed but the system is unable to mark the transaction as processed due to database issues
     * system will simply log the error for later cleanup, as the funds at that point are transferred successfully.
     *
     * @param transaction Transaction object containing details of transaction to be processed
     * @return ResponseEntity containing the output
     */
    public ResponseEntity<String> performTransaction(Transaction transaction) {

        try {
            BigDecimal appFee = transaction.getAmount().multiply(new BigDecimal("0.0005"));
            BigDecimal receiverTotalAmount = transaction.getAmount().subtract(appFee);
            logger.info("Transaction starting. Amount: [" + transaction.getAmount() + "] Fee: [" + appFee + "] Amount received: ["+ receiverTotalAmount + "]");

            User sender = usersDAO.getUser(transaction.getFromAcctID());
            User receiver = usersDAO.getUser(transaction.getToAcctID());
            logger.info("Sender and Receiver Users loaded successfully");

            int affectedRows = usersDAO.subtractFunds(sender.getAcctID(), transaction.getAmount());
            logger.info("Funds removed from sender: " + transaction.getAmount());

            transaction.setTransactionID(transactionDAO.addTransaction(transaction));
            logger.info("Transaction added to database: " + transaction.getTransactionID());

            affectedRows = usersDAO.addFunds(receiver.getAcctID(), receiverTotalAmount);
            logger.info("Funds added to recipient");

            transaction.setProcessed(true);
            transactionDAO.markTransactionPaid(transaction);
            bank.addFee(appFee);
            logger.info("Transaction complete.");

        }
        catch (FailToLoadUserException e ) {
            ResponseEntity<String> response = new ResponseEntity<String>("Unable to add transaction. Failed to load user [" + e.getAcctID() + "].", new HttpHeaders(), HttpStatus.BAD_REQUEST);
            logger.error("Transaction failed to be added, sender or receiver could not be found", response);
            return response;
        }
        catch (FailToSubtractUserFundsException e) {
            ResponseEntity<String> response = new ResponseEntity<String>("Unable to add transaction. Ensure sufficient funds are available.", new HttpHeaders(), HttpStatus.BAD_REQUEST);
            logger.error("Transaction failed to be added, unable to remove funds from sender", response);
            return response;
        }
        catch (FailToCreateTransactionRecordException e) {
            //Rollback subtracting funds, then send response
            try {
                usersDAO.addFunds(e.getTransaction().getFromAcctID(), e.getTransaction().getAmount());
            }
            catch (FailToAddUserFundsException f) {
                ResponseEntity<String> response = new ResponseEntity<String>("Error adding transaction to database. Error returning funds to user.", new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
                logger.error("Transaction failed to be added, funds could not be returned to sender", response);
                return response;
            }
            ResponseEntity<String> response = new ResponseEntity<String>("Error adding transaction to database. Funds returned to user.", new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
            logger.error("Transaction failed to be added, funds have been returned to sender", response);
            return response;
        }
        catch (FailToAddUserFundsException e) {
            //Rollback subtracting funds
            //Rollback creating transaction record
            //mark transaction as cancelled and return funds to sender
            try {
                transactionDAO.cancelTransaction(transaction);
                usersDAO.addFunds(transaction.getFromAcctID(), transaction.getAmount());
            }
            catch (FailToAddUserFundsException f) {
                ResponseEntity<String> response = new ResponseEntity<String>("Error adding funds to recipient. Error returning funds to user.", new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
                logger.error("Transaction failed to be added, funds could not be returned to sender", response);
                return response;
            }
            ResponseEntity<String> response = new ResponseEntity<String>("Error adding funds to recipient. Funds returned to user.", new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
            logger.error("Transaction failed to be added, funds have been returned to sender", response);
            return response;
        }
        catch (FailToMarkTransactionProcessedException e) {
            //Failed to mark transaction as processed, however funds have been successfully moved.
            //Retry marking transaction as processed, if fails just log. No need to rollback transaction as it was successful.
            try {
                transaction.setProcessed(true);
                transactionDAO.markTransactionPaid(transaction);
                bank.addFee(transaction.getAmount().multiply(new BigDecimal("0.0005")));
                logger.info("Transaction complete.");
            }
            catch (FailToMarkTransactionProcessedException f) {
                ResponseEntity<String> response = new ResponseEntity<String>("Transaction processed successfully. Transaction Record failed to be marked as Processed. TransactionID [" +transaction.getTransactionID() + "]", new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
                logger.error("Transaction processed successfully. Transaction Record failed to be marked as Processed. TransactionID [" +transaction.getTransactionID() + "]", response);
                return response;
            }
        }
        //Build response if successful
        ResponseEntity<String> response = createdResponse(transaction);
        logger.info("Transaction added", response);
        return response;
    }

    /**
     * Mark an existing user to user transaction as paid.
     * Should not be necessary in most cases, but can be used in manual cleanup if performTransaction has failed at this point
     *
     * @param transaction Transaction object containing details of transaction to be processed
     * @return ResponseEntity containing the output
     */
    public ResponseEntity<String> markPaid(Transaction transaction) {
        logger.info("Processing markPaid Transaction request to mark a Transaction as Paid");
        //Attempt to update payment in database
        try {
            int affectedRows = transactionDAO.markTransactionPaid(transaction);
        }
        catch (FailToMarkTransactionProcessedException e) {
            ResponseEntity<String> response = ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            logger.error("Failed to mark transaction as processed",response);
            return response;
        }

        //Build response
        ResponseEntity<String> response = new ResponseEntity<String>("Transaction " + transaction.getTransactionID() + " successfully marked as processed", new HttpHeaders(), HttpStatus.OK);
        logger.info("Transaction marked as paid", response);
        return response;
    }

    /**
     * Get details of a Transaction by TransactionID
     *
     * @param transaction Transaction object containing ID of transaction to be processed
     * @return ResponseEntity containing the output
     */
    public ResponseEntity<String> getTransactionByID(Transaction transaction) {
        logger.info("Processing getTransactionByID Transaction request to get all details of a Transaction entry");
        Transaction foundTransaction = transactionDAO.getTransactionByID(transaction.getTransactionID());

        if (foundTransaction == null) {
            //Transaction could not be found with this ID
            ResponseEntity<String> response = ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            logger.error("Transaction not found with provided TransactionID",response);
            return response;
        }

        //Build response
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
        builder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
        Gson gson = builder.setPrettyPrinting().create();
        String responseString = gson.toJson(foundTransaction);
        ResponseEntity<String> response = new ResponseEntity<String>(responseString, new HttpHeaders(), HttpStatus.OK);

        logger.info("Transaction details obtained", response);
        return response;
    }

    /**
     * Get IDs of all Transactions by sent by a specific AcctID
     *
     * @param fromAcctID source AcctID
     * @return ResponseEntity containing the output
     */
    public ResponseEntity<String> getAllSentPaymentIDs(int fromAcctID) {
        logger.info("Processing getAllSentPaymentIDs Transaction request to get IDs of all sent payments for a User");
        ArrayList<Integer> result = transactionDAO.getAllSentTransactions(fromAcctID);

        if (result.size() == 0){
            //no sent payments found
            ResponseEntity<String> response = ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            logger.error("No sent payments found for provided TransactionID",response);
            return response;
        }

        //Build response
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();
        String responseString = gson.toJson(result);

        ResponseEntity<String> response = new ResponseEntity<String>(responseString, new HttpHeaders(), HttpStatus.OK);

        logger.info("Sent TransactionIDs obtained", response);
        return response;
    }

    /**
     * Get IDs of all Transactions by received by a specific AcctID
     *
     * @param fromAcctID recipient AcctID
     * @return ResponseEntity containing the output
     */
    public ResponseEntity<String> getAllReceivedPaymentIDs(int fromAcctID) {
        logger.info("Processing getAllReceivedPaymentIDs Transaction request to get IDs of all received payments for a User");
        ArrayList<Integer> result = transactionDAO.getAllReceivedTransactions(fromAcctID);

        if (result.size() == 0){
            //no received payments found
            ResponseEntity<String> response = ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            logger.error("No received payments found for provided TransactionID",response);
            return response;
        }

        //Build response
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();
        String responseString = gson.toJson(result);

        ResponseEntity<String> response = new ResponseEntity<String>(responseString, new HttpHeaders(), HttpStatus.OK);

        logger.info("Received TransactionIDs obtained", response);
        return response;
    }

    /**
     * Get JSON details of all Transactions by sent by a specific AcctID
     *
     * @param fromAcctID source AcctID
     * @return ResponseEntity containing the output
     */
    public ResponseEntity<String> getAllSentPaymentDetails(int fromAcctID) {
        logger.info("Processing getAllSentPaymentDetails Transaction request to get details of all sent payments for a User");
        JSONArray json = transactionDAO.getAllSentTransactionDetails(fromAcctID);

        if (json.size() == 0){
            //no sent payments found
            ResponseEntity<String> response = ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            logger.error("No sent payments found for provided TransactionID",response);
            return response;
        }

        //Build response
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();
        String responseString = gson.toJson(json);

        ResponseEntity<String> response = new ResponseEntity<String>(responseString, new HttpHeaders(), HttpStatus.OK);

        logger.info("Sent Transaction Details obtained", response);
        return response;
    }

    /**
     * Get JSON details of all Transactions by received by a specific AcctID
     *
     * @param fromAcctID recipient AcctID
     * @return ResponseEntity containing the output
     */
    public ResponseEntity<String> getAllReceivedPaymentDetails(int fromAcctID) {
        logger.info("Processing getAllReceivedPaymentDetails Transaction request to get details of all received payments for a User");
        JSONArray json = transactionDAO.getAllReceivedTransactionDetails(fromAcctID);

        if (json.size() == 0){
            //no sent payments found
            ResponseEntity<String> response = ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            logger.error("No sent payments found for provided TransactionID",response);
            return response;
        }

        //Build response
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();
        String responseString = gson.toJson(json);

        ResponseEntity<String> response = new ResponseEntity<String>(responseString, new HttpHeaders(), HttpStatus.OK);

        logger.info("Received Transaction Details obtained", response);
        return response;
    }

    /**
     * Get IDs of all unprocessed transactions
     *
     * @return ResponseEntity containing the output
     */
    public ResponseEntity<String> getAllUnprocessedTransactions() {
        logger.info("Processing getAllUnprocessedTransactions Transaction request to IDs of all currently unprocessed transactions");
        ArrayList<Integer> list = transactionDAO.getAllUnprocessedTransactionIDs();

        if (list.size() == 0){
            //no unprocessed payments found
            ResponseEntity<String> response = ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            logger.error("No unprocessed payments found",response);
            return response;
        }

        //Build response
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();
        String responseString = gson.toJson(list);

        ResponseEntity<String> response = new ResponseEntity<String>(responseString, new HttpHeaders(), HttpStatus.OK);

        logger.info("All unprocessed payments reported", response);
        return response;
    }


}
