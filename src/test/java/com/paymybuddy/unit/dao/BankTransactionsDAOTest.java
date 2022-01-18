package com.paymybuddy.unit.dao;

import com.nimbusds.jose.shaded.json.JSONArray;
import com.paymybuddy.data.dao.BankTransactionsDAO;
import com.paymybuddy.data.dao.TransactionDAO;
import com.paymybuddy.data.dao.dbConfig.DatabaseTestConnection;
import com.paymybuddy.data.dao.dbConfig.TestDAO;
import com.paymybuddy.presentation.model.BankTransaction;
import com.paymybuddy.presentation.model.Transaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BankTransactionsDAOTest {

    private static BankTransactionsDAO bankTransactionDAO;
    private static TestDAO testDAO;

    @BeforeAll
    private static void setUp() {
        bankTransactionDAO = new BankTransactionsDAO();
        bankTransactionDAO.databaseConnection = new DatabaseTestConnection();
        testDAO = new TestDAO();
        testDAO.clearDB();
    }

    @BeforeEach
    private void setUpDb() {
        testDAO.setUpTestDB();
    }

    @AfterEach
    private void cleanUp() {
        testDAO.clearDB();
    }

    @Test
    public void bankTransactionsDAOCanAddATransaction() throws Exception{
        //Prepare
        int transactionID = -1;
        BankTransaction newBankTransaction = new BankTransaction(1, new BigDecimal("10"), "FR1420041010050500013M02606","BNPAGFGX", false, false);

        //Method
        transactionID = bankTransactionDAO.addTransaction(newBankTransaction);

        //Verification
        assertEquals(5, transactionID);
    }

    @Test
    public void bankTransactionsDAOCanGetAllUnprocessedTransactionIDs(){
        //Prepare
        ArrayList<Integer> ids;

        //Method
        ids = bankTransactionDAO.getAllUnprocessedTransactionIDs();

        //Verification
        assertEquals(2, ids.size());
    }

    @Test
    public void bankTransactionsDAOCanMarkPaymentProcessed() {
        //Prepare
        int affectedRows = -1;

        //Method
        affectedRows = bankTransactionDAO.markTransactionProcessed(3);

        //Verification
        assertEquals(1, affectedRows);
    }

    @Test
    public void bankTransactionsDAOCanMarkPaymentCancelled() {
        //Prepare
        int affectedRows = -1;

        //Method
        affectedRows = bankTransactionDAO.markTransactionCancelled(3);

        //Verification
        assertEquals(1, affectedRows);
    }

    @Test
    public void bankTransactionsDAOCanGetTransactionDetails() {
        //Prepare
        BankTransaction bankTransaction;

        //Method
        bankTransaction = bankTransactionDAO.getTransactionDetails(1);

        //Verification
        assertEquals(1, bankTransaction.getTransactionID());
        assertEquals(3, bankTransaction.getAcctID());
    }

    @Test
    public void bankTransactionDAOCanGetDetailsForAllUnprocessedTransactions() {
        //Prepare
        ArrayList<BankTransaction> bankTransactions;

        //Method
        bankTransactions = bankTransactionDAO.getUnprocessedTransactionDetails();

        //Verification
        assertEquals(2, bankTransactions.size());
    }

    @Test
    public void bankTransactionsDaoCanGetAllBankTransactionDetailsForAUser() {
        //Prepare
        JSONArray json;

        //Method
        json = bankTransactionDAO.getAllBankTransactionDetails(1);

        //Verification
        assertEquals(1, json.size());
    }

}
