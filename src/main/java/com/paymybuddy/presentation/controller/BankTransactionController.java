package com.paymybuddy.presentation.controller;

import com.paymybuddy.logic.BankTransactionService;
import com.paymybuddy.presentation.apimodels.BankTransactionDTO;
import com.paymybuddy.presentation.model.BankTransaction;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * RestController for /banktransaction endpoint
 *
 */
@RestController
public class BankTransactionController {

    private static final Logger logger = LogManager.getLogger("BankTransactionController");

    private BankTransactionService bankTransactionService;

    @Autowired
    public BankTransactionController(BankTransactionService bankTransactionService){
        this.bankTransactionService = bankTransactionService;
    }

    /**
     * Mapping for POST
     *
     * Returns:
     * HttpStatus.BAD_REQUEST if user has insufficient funds for a withdrawal
     * HttpStatus.INTERNAL_SERVER_ERROR for any database related issues
     * Json string & HttpStatus.CREATED if successful
     *
     * @param bankTransactionDTO details of transaction request
     * @return Json string & HttpStatus.CREATED if successful
     */
    @PostMapping("/banktransaction")
    @Operation(
            summary = "Add new bank transaction",
            description = "Add a bank transaction to the database.\nThis will start the process of adding or withdrawing funds for a user.\nWithdrawal requests result in immediate removal of balance, adding funds waits for bank processing.\nTransactionID will be auto generated" +
                    "\n\nResponds with JSON of added banktransaction, with generated TransactionID")
    public ResponseEntity<String> addOrRemoveFunds(@RequestBody(description = "")@org.springframework.web.bind.annotation.RequestBody BankTransactionDTO bankTransactionDTO){

        return bankTransactionService.addOrRemoveFunds(new BankTransaction(bankTransactionDTO));

    }

    /**
     * Mapping for GET
     *
     * Returns:
     * HttpStatus.NOT_FOUND if user has not performed any bank transactions
     * Json string & HttpStatus.OK if successful
     *
     * @param acctID of user
     * @return Json string & HttpStatus.OK if successful
     */
    @GetMapping("/banktransaction/all")
    @Operation(
            summary = "Get details of a user's bank transactions",
            description = "Get details of all bank transactions for a user from the database." +
                    "\n\nResponds with JSON of all bank transactions for provided user in JSON format")
    public ResponseEntity<String> getAllBankTransactionsForUser(@RequestParam("acctID")int acctID){

        return bankTransactionService.getAllBankTransactionDetails(acctID);

    }

    /**
     * Mapping for GET
     *
     * Returns:
     * HttpStatus.NOT_FOUND if user has not performed any bank transactions
     * Json string & HttpStatus.OK if successful
     *
     * @param transactionID of desired bank transaction
     * @return Json string & HttpStatus.OK if successful
     */
    @GetMapping("/banktransaction")
    @Operation(
            summary = "Get details of a user's bank transactions",
            description = "Get details of all bank transactions for a user from the database." +
                    "\n\nResponds with JSON of all bank transactions for provided user in JSON format")
    public ResponseEntity<String> getSingleBankTransaction(@RequestParam("transactionID")int transactionID){

        return bankTransactionService.getBankTransactionByID(transactionID);

    }

}
