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

/**
 * RestController for /transaction endpoint
 *
 * Endpoint includes:
 *
 * POST/PUT/GET transaction
 * GET /transaction/unprocessed
 * GET /transaction/sent/ids & /transaction/sent/details
 * GET /transaction/received/ids & /transaction/received/details
 */
@RestController
public class TransactionController {

    private static final Logger logger = LogManager.getLogger("UserController");

    private TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService){
        this.transactionService = transactionService;
    }

    /**
     * POST mapping to perform a user to user transaction
     *
     * Will remove funds from sender, add funds to recipient, and create a Transaction entry as a record of the payment
     * Will attempt to rollback all changes if the process fails at any point
     *
     * Returns:
     * HttpStatus.BAD_REQUEST if transaction cannot be performed with provided details
     * HttpStatus.INTERNAL_SERVER_ERROR for issues creating/modifying database entries
     * Json string & HttpStatus.CREATED if successful
     *
     * @param transactionDTO details of transaction to be processed
     * @return Json string & HttpStatus.CREATED if successful
     */
    @PostMapping("/transaction")
    @Operation(
            summary = "Make a transaction",
            description = "Add a transaction to the database & perform payment.\nTransactionID will be auto generated.\nFunds removed from sender and added to receiver." +
                    "\n\nResponds with JSON of added transaction, with generated TransactionID")
    public ResponseEntity<String> performTransaction(@RequestBody(description = "")@org.springframework.web.bind.annotation.RequestBody TransactionDTO transactionDTO){

        return transactionService.performTransaction(new Transaction(transactionDTO));

    }

    /**
     * POST mapping to mark an existing transaction as processed
     *
     * Should not be necessary in most cases, but can be used in manual cleanup if performTransaction has failed at this point
     *
     * Returns:
     * HttpStatus.NOT_FOUND if no transaction found with provided ID
     * HttpStatus.OK if successful
     *
     * @param transactionIDDTO details of transaction to be updated
     * @return HttpStatus.OK if successful
     */
    @PutMapping("/transaction")
    @Operation(
            summary = "Mark a transaction as processed",
            description = "Update an existing transaction in the database to show it has been processed")
    public ResponseEntity<String> markTransactionAsProcessed(@RequestBody(description = "")@org.springframework.web.bind.annotation.RequestBody TransactionIDDTO transactionIDDTO){

        return transactionService.markPaid(new Transaction(transactionIDDTO.transactionID));

    }

    /**
     * GET mapping to obtain list of IDs of all unprocessed transactions
     *
     * Returns:
     * HttpStatus.NOT_FOUND if there are no unprocessed transactions
     * Json string & HttpStatus.OK if successful
     *
     * @return Json string & HttpStatus.OK if successful
     */
    @GetMapping("/transaction/unprocessed")
    @Operation(
            summary = "Get all unprocessed transactions",
            description = "Get list of TransactionIDs for all unprocessed transactions")

    public ResponseEntity<String> getUnprocessedTransactionIDs(){

        return transactionService.getAllUnprocessedTransactions();

    }

    /**
     * GET mapping to retrieve transaction details by TransactionID
     *
     * Returns:
     * HttpStatus.NOT_FOUND if there is no matching transactions
     * Json string & HttpStatus.OK if successful
     *
     * @return Json string & HttpStatus.OK if successful
     */
    @GetMapping("/transaction")
    @Operation(
            summary = "Get transaction by TransactionID",
            description = "Get all details for a single transaction by TransactionID")

    public ResponseEntity<String> getTransaction(@RequestParam int transactionID){

        return transactionService.getTransactionByID(new Transaction(transactionID));

    }

    /**
     * GET mapping to get transaction IDs for all of a Users sent transactions
     *
     * Returns:
     * HttpStatus.NOT_FOUND if there are no matching transactions
     * Json string & HttpStatus.OK if successful
     *
     * @return Json string & HttpStatus.OK if successful
     */
    @GetMapping("/transaction/sent/ids")
    @Operation(
            summary = "Get TransactonID for all transactions sent by an AcctID",
            description = "Get TransactonID for all transactions sent by an AcctID")

    public ResponseEntity<String> getSentTransactionIDs(@RequestParam int acctID){

        return transactionService.getAllSentPaymentIDs(acctID);

    }

    /**
     * GET mapping to get transaction details for all of a Users sent transactions
     *
     * Returns:
     * HttpStatus.NOT_FOUND if there are no matching transactions
     * Json string & HttpStatus.OK if successful
     *
     * @return Json string & HttpStatus.OK if successful
     */
    @GetMapping("/transaction/sent/details")
    @Operation(
            summary = "Get all transaction details for all transactions sent by an AcctID",
            description = "Get all transaction details for all transactions sent by an AcctID")

    public ResponseEntity<String> getSentTransactionDetails(@RequestParam int acctID){

        return transactionService.getAllSentPaymentDetails(acctID);

    }

    /**
     * GET mapping to get transaction IDs for all of a Users received transactions
     *
     * Returns:
     * HttpStatus.NOT_FOUND if there are no matching transactions
     * Json string & HttpStatus.OK if successful
     *
     * @return Json string & HttpStatus.OK if successful
     */
    @GetMapping("/transaction/received/ids")
    @Operation(
            summary = "Get TransactonID for all transactions received by an AcctID",
            description = "Get TransactonID for all transactions received by an AcctID")

    public ResponseEntity<String> getReceivedTransactionIDs(@RequestParam int acctID){

        return transactionService.getAllReceivedPaymentIDs(acctID);

    }

    /**
     * GET mapping to get transaction details for all of a Users received transactions
     *
     * Returns:
     * HttpStatus.NOT_FOUND if there are no matching transactions
     * Json string & HttpStatus.OK if successful
     *
     * @return Json string & HttpStatus.OK if successful
     */
    @GetMapping("/transaction/received/details")
    @Operation(
            summary = "Get all transaction details for all transactions received by an AcctID",
            description = "Get all transaction details for all transactions received by an AcctID")

    public ResponseEntity<String> getReceivedTransactionDetails(@RequestParam int acctID){

        return transactionService.getAllReceivedPaymentDetails(acctID);

    }

}
