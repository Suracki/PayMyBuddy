package com.paymybuddy.presentation.controller;

import com.paymybuddy.logic.TransactionService;
import com.paymybuddy.presentation.apimodels.TransactionDTO;
import com.paymybuddy.presentation.apimodels.TransactionIDDTO;
import com.paymybuddy.presentation.model.Transaction;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TransactionController {

    private static final Logger logger = LogManager.getLogger("UserController");

    private TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService){
        this.transactionService = transactionService;
    }

    @PostMapping("/transaction")
    @Operation(
            summary = "Add new transaction to database",
            description = "Add a transaction to the database.\nTransactionID will be auto generated" +
                    "\n\nResponds with JSON of added transaction, with generated TransactionID")
    public ResponseEntity<String> addTransaction(@RequestBody(description = "")@org.springframework.web.bind.annotation.RequestBody TransactionDTO transactionDTO){

        return transactionService.makePayment(new Transaction(transactionDTO));

    }

    @PutMapping("/transaction")
    @Operation(
            summary = "Mark a transaction as processed",
            description = "Update an existing transaction in the database to show it has been processed")
    public ResponseEntity<String> markTransactionAsProcessed(@RequestBody(description = "")@org.springframework.web.bind.annotation.RequestBody TransactionIDDTO transactionIDDTO){

        return transactionService.markPaid(new Transaction(transactionIDDTO.transactionID));

    }

    @GetMapping("/transaction")
    @Operation(
            summary = "Get transaction by TransactionID",
            description = "Get all details for a single transaction by TransactionID")

    public ResponseEntity<String> getTransaction(@RequestParam int transactionID){

        return transactionService.getTransactionByID(new Transaction(transactionID));

    }

    @GetMapping("/transaction/sent/ids")
    @Operation(
            summary = "Get TransactonID for all transactions sent by an AcctID",
            description = "Get TransactonID for all transactions sent by an AcctID")

    public ResponseEntity<String> getSentTransactionIDs(@RequestParam int acctID){

        return transactionService.getAllSentPaymentIDs(acctID);

    }

    @GetMapping("/transaction/sent/detail")
    @Operation(
            summary = "Get all transaction details for all transactions sent by an AcctID",
            description = "Get all transaction details for all transactions sent by an AcctID")

    public ResponseEntity<String> getSentTransactionDetails(@RequestParam int acctID){

        return transactionService.getAllSentPaymentDetails(acctID);

    }

    @GetMapping("/transaction/received/ids")
    @Operation(
            summary = "Get TransactonID for all transactions received by an AcctID",
            description = "Get TransactonID for all transactions received by an AcctID")

    public ResponseEntity<String> getReceivedTransactionIDs(@RequestParam int acctID){

        return transactionService.getAllReceivedPaymentIDs(acctID);

    }

    @GetMapping("/transaction/received/detail")
    @Operation(
            summary = "Get all transaction details for all transactions received by an AcctID",
            description = "Get all transaction details for all transactions received by an AcctID")

    public ResponseEntity<String> getReceivedTransactionDetails(@RequestParam int acctID){

        return transactionService.getAllReceivedPaymentDetails(acctID);

    }

    @GetMapping("/transaction/unprocessed")
    @Operation(
            summary = "Get all unprocessed transactions",
            description = "Get list of TransactionIDs for all unprocessed transactions")

    public ResponseEntity<String> getUnprocessedTransactionIDs(){

        return transactionService.getAllUnprocessedTransactions();

    }

}
