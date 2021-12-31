package com.paymybuddy.logic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nimbusds.jose.shaded.json.JSONArray;
import com.paymybuddy.data.dao.TransactionDAO;
import com.paymybuddy.logic.gson.LocalDateTimeDeserializer;
import com.paymybuddy.logic.gson.LocalDateTimeSerializer;
import com.paymybuddy.presentation.model.Transaction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class TransactionService {

    private TransactionDAO transactionDAO;
    private static final Logger logger = LogManager.getLogger("TransactionService");

    @Autowired
    public TransactionService(TransactionDAO transactionDAO) {
        this.transactionDAO = transactionDAO;
    }

    public ResponseEntity<String> makePayment(Transaction transaction) {
        //Add relationship to database
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
