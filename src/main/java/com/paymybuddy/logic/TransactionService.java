package com.paymybuddy.logic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

        //Build response
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
        builder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
        Gson gson = builder.setPrettyPrinting().create();
        String responseString = gson.toJson(transaction);
        HttpHeaders responseHeaders = new HttpHeaders();

        return new ResponseEntity<String>(responseString, responseHeaders, HttpStatus.CREATED);
    }

    public ResponseEntity<String> markPaid(Transaction transaction) {
        //Attempt to update payment in database
        int affectedRows = transactionDAO.markTransactionPaid(transaction);

        if(affectedRows == -1) {
            //Transaction could not be found with this ID
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        //Build response
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
        builder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
        Gson gson = builder.setPrettyPrinting().create();
        String responseString = gson.toJson(affectedRows);
        HttpHeaders responseHeaders = new HttpHeaders();

        return new ResponseEntity<String>(responseString, responseHeaders, HttpStatus.OK);
    }

    public ResponseEntity<String> getTransactionByID(int transactionID) {
        Transaction foundTransaction = transactionDAO.getTransactionByID(transactionID);

        if (foundTransaction == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        //Build response
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
        builder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
        Gson gson = builder.setPrettyPrinting().create();
        String responseString = gson.toJson(foundTransaction);
        HttpHeaders responseHeaders = new HttpHeaders();

        return new ResponseEntity<String>(responseString, responseHeaders, HttpStatus.OK);
    }

    public ResponseEntity<String> getAllSentPaymentIDs(int fromAcctID) {
        ArrayList<Integer> result = transactionDAO.getAllSentTransactions(fromAcctID);

        if (result.size() == 0){
            //no sent payments found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        //Build response
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();
        String responseString = gson.toJson(result);
        HttpHeaders responseHeaders = new HttpHeaders();

        return new ResponseEntity<String>(responseString, responseHeaders, HttpStatus.OK);
    }

    public ResponseEntity<String> getAllReceivedPaymentIDs(int fromAcctID) {
        ArrayList<Integer> result = transactionDAO.getAllReceivedTransactions(fromAcctID);

        if (result.size() == 0){
            //no received payments found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        //Build response
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();
        String responseString = gson.toJson(result);
        HttpHeaders responseHeaders = new HttpHeaders();

        return new ResponseEntity<String>(responseString, responseHeaders, HttpStatus.OK);
    }
}
