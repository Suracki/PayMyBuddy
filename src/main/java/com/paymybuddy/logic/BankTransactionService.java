package com.paymybuddy.logic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.paymybuddy.data.dao.BankTransactionsDAO;
import com.paymybuddy.data.dao.UsersDAO;
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

@Service
public class BankTransactionService {

    private static final Logger logger = LogManager.getLogger("BankTransactionService");
    @Autowired
    BankTransactionsDAO bankTransactionsDAO;
    @Autowired
    UsersDAO usersDAO;


    public ResponseEntity<String> addOrRemoveFunds(BankTransaction bankTransaction){

        //Is transaction a withdrawal
        if(bankTransaction.getAmount().compareTo(new BigDecimal("0")) < 0) {
            //Remove funds from user account to allow withdrawal
            int affectedRows = usersDAO.subtractFunds(bankTransaction.getAcctID(), bankTransaction.getAmount());
            if (affectedRows == -1) {
                //failed to remove funds
                ResponseEntity<String> response = new ResponseEntity<String>("Unable to add bank transaction. Failed to remove funds from user account.", new HttpHeaders(), HttpStatus.BAD_REQUEST);
                logger.error("Bank transaction failed to be added", response);
                return response;
            }
        }

        //Create bank transaction
        bankTransaction.setTransactionID(bankTransactionsDAO.addTransaction(bankTransaction));

        //Build response
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
        builder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
        Gson gson = builder.setPrettyPrinting().create();
        String responseString = gson.toJson(bankTransaction);
        HttpHeaders responseHeaders = new HttpHeaders();

        return new ResponseEntity<>(responseString, responseHeaders, HttpStatus.OK);
    }

}
