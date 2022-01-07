package com.paymybuddy.logic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nimbusds.jose.shaded.json.JSONArray;
import com.paymybuddy.banking.BankController;
import com.paymybuddy.data.dao.TransactionDAO;
import com.paymybuddy.data.dao.UsersDAO;
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


    public ResponseEntity<String> makePayment(Transaction transaction) {
        //Add transaction to database
        transaction.setTransactionID(transactionDAO.addTransaction(transaction));

        //Check if failed, return error
        if (transaction.getTransactionID() == -1) {
            ResponseEntity<String> response = new ResponseEntity<String>("Unable to add transaction. Ensure sender and receiver exist and are active.", new HttpHeaders(), HttpStatus.BAD_REQUEST);
            logger.error("Transaction failed to be added", response);
            return response;
        }

        //Build response if successful
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
        builder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
        Gson gson = builder.setPrettyPrinting().create();
        String responseString = gson.toJson(transaction);
        ResponseEntity<String> response = new ResponseEntity<String>(responseString, new HttpHeaders(), HttpStatus.CREATED);

        logger.info("Transaction added", response);
        return response;
    }

    public ResponseEntity<String> performTransaction(Transaction transaction) {
        //Get sender User
        User sender = usersDAO.getUser(transaction.getFromAcctID());
        User receiver = usersDAO.getUser(transaction.getToAcctID());
        BigDecimal appFee = transaction.getAmount().multiply(new BigDecimal("0.0005"));
        BigDecimal receiverTotalAmount = transaction.getAmount().subtract(appFee);
        logger.info("Transaction starting. Amount: [" + transaction.getAmount() + "] Fee: [" + appFee + "] Amount received: ["+ receiverTotalAmount + "]");
        //If either user failed to load
        if (sender == null || receiver == null) {
            ResponseEntity<String> response = new ResponseEntity<String>("Unable to add transaction. Ensure sender and receiver exist and are active.", new HttpHeaders(), HttpStatus.BAD_REQUEST);
            logger.error("Transaction failed to be added, sender or receiver could not be found", response);
            return response;
        }
        logger.info("Sender and Receiver Users loaded successfully");
        //If sender balance is less than amount to be sent
        if (sender.getBalance().compareTo(transaction.getAmount()) < 0) {
            //Insufficient balance
            ResponseEntity<String> response = new ResponseEntity<String>("Unable to add transaction. Ensure sufficient funds are available.", new HttpHeaders(), HttpStatus.BAD_REQUEST);
            logger.error("Transaction failed to be added, insufficient funds available", response);
            return response;
        }
        logger.info("Sender has sufficient balance available");
        //Subtract funds from user
        int affectedRows = usersDAO.subtractFunds(sender.getAcctID(), transaction.getAmount());
        //Failed to remove balance, unknown reason
        if (affectedRows < 1) {
            ResponseEntity<String> response = new ResponseEntity<String>("Unable to add transaction. Ensure sufficient funds are available.", new HttpHeaders(), HttpStatus.BAD_REQUEST);
            logger.error("Transaction failed to be added, funds could not be removed from sender", response);
            return response;
        }
        logger.info("Funds removed from sender: " + transaction.getAmount());
        //Add transaction to database
        transaction.setTransactionID(transactionDAO.addTransaction(transaction));
        //Failed to add transaction to database
        if (transaction.getTransactionID() == -1) {
            //Attempt to return balance to user
            affectedRows = usersDAO.addFunds(sender.getAcctID(), transaction.getAmount());
            if (affectedRows == -1) {
                //failed to return balance
                ResponseEntity<String> response = new ResponseEntity<String>("Error adding transaction to database. Error returning funds to user.", new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
                logger.error("Transaction failed to be added, funds could not be returned to sender", response);
                return response;
            }
            ResponseEntity<String> response = new ResponseEntity<String>("Error adding transaction to database. Funds returned to user.", new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
            logger.error("Transaction failed to be added, funds have been returned to sender", response);
            return response;
        }
        logger.info("Transaction added to database: " + transaction.getTransactionID());
        //Add funds to recipient
        affectedRows = usersDAO.addFunds(receiver.getAcctID(), receiverTotalAmount);
        //Failed to add funds to recipient
        if (affectedRows == -1) {
            //mark transaction as cancelled and return funds to sender
            transactionDAO.cancelTransaction(transaction);
            affectedRows = usersDAO.addFunds(sender.getAcctID(), transaction.getAmount());
            if (affectedRows == -1) {
                //failed to return balance
                ResponseEntity<String> response = new ResponseEntity<String>("Error adding funds to recipient. Error returning funds to user.", new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
                logger.error("Transaction failed to be added, funds could not be returned to sender", response);
                return response;
            }
            ResponseEntity<String> response = new ResponseEntity<String>("Error adding funds to recipient. Funds returned to user.", new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
            logger.error("Transaction failed to be added, funds have been returned to sender", response);
            return response;
        }
        //transaction completed, mark as processed
        markPaid(transaction);
        transaction.setProcessed(true);
        bank.addFee(appFee);
        logger.info("Funds added to recipient, transaction complete");


        //Build response if successful
        ResponseEntity<String> response = createdResponse(transaction);
        logger.info("Transaction added", response);
        return response;
    }

    public ResponseEntity<String> markPaid(Transaction transaction) {
        //Attempt to update payment in database
        int affectedRows = transactionDAO.markTransactionPaid(transaction);

        System.out.println("Affected: " + affectedRows);

        if(affectedRows == 0) {
            //Transaction could not be found with this ID
            ResponseEntity<String> response = ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            logger.error("Failed to mark transaction as processed",response);
            return response;
        }

        //Build response
        ResponseEntity<String> response = new ResponseEntity<String>("Transaction " + transaction.getTransactionID() + " successfully marked as processed", new HttpHeaders(), HttpStatus.OK);
        logger.info("Transaction marked as paid", response);
        return response;
    }

    public ResponseEntity<String> getTransactionByID(Transaction transaction) {
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

    public ResponseEntity<String> getAllSentPaymentIDs(int fromAcctID) {
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

    public ResponseEntity<String> getAllReceivedPaymentIDs(int fromAcctID) {
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

    public ResponseEntity<String> getAllSentPaymentDetails(int fromAcctID) {
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

    public ResponseEntity<String> getAllReceivedPaymentDetails(int fromAcctID) {
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

    public ResponseEntity<String> getAllUnprocessedTransactions() {
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
