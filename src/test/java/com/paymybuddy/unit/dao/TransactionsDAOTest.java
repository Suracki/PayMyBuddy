package com.paymybuddy.unit.dao;

import com.nimbusds.jose.shaded.json.JSONArray;
import com.paymybuddy.data.dao.TransactionDAO;
import com.paymybuddy.data.dao.dbConfig.DatabaseTestConnection;
import com.paymybuddy.data.dao.dbConfig.TestDAO;
import com.paymybuddy.presentation.model.Transaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionsDAOTest {

    private static TransactionDAO transactionsDAO;
    private static TestDAO testDAO;

    @BeforeAll
    private static void setUp() {
        transactionsDAO = new TransactionDAO();
        transactionsDAO.databaseConnection = new DatabaseTestConnection();
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
    public void transactionsDAOCanAddAPayment() {
        //Prepare
        int transactionID = -1;
        Transaction newTransaction = new Transaction(1, 2, LocalDateTime.now(), "Test Description", new BigDecimal("10.03"), false);

        //Method
        transactionID = transactionsDAO.addTransaction(newTransaction);

        //Verification
        assertEquals(11, transactionID);
    }

    @Test
    public void transactionsDAOCanGetPaymentInformation() {
        //Prepare
        Transaction transaction;

        //Method
        transaction = transactionsDAO.getTransactionByID(1);

        //Verification
        assertEquals(1, transaction.getFromAcctID());
        assertEquals(2, transaction.getToAcctID());
    }

    @Test void transactionsDAOCanMarkAPaymentProcessed() throws Exception {
        //Prepare
        Transaction beforeTransaction = transactionsDAO.getTransactionByID(1);
        Transaction updatedTransaction = transactionsDAO.getTransactionByID(1);
        Transaction afterTransaction;

        //Method
        updatedTransaction.setProcessed(true);
        transactionsDAO.markTransactionPaidEx(updatedTransaction);
        afterTransaction = transactionsDAO.getTransactionByID(1);

        //Verification
        assertFalse(beforeTransaction.isProcessed());
        assertTrue(afterTransaction.isProcessed());
    }

    @Test
    public void transactionsDAOCanGetAllSentPaymentsForAUser() {
        //Prepare
        ArrayList<Integer> list;

        //Method
        list = transactionsDAO.getAllSentTransactions(1);

        //Verification
        assertEquals(5, list.size());

    }

    @Test
    public void transactionsDAOCanGetAllSentPaymentDetailsForAUser() {
        //Prepare
        JSONArray json;

        //Method
        json = transactionsDAO.getAllSentTransactionDetails(1);

        //Verification
        assertEquals(5, json.size());

    }

    @Test
    public void transactionsDAOCanGetAllReceivedPaymentsForAUser() {
        //Prepare
        ArrayList<Integer> list;

        //Method
        list = transactionsDAO.getAllReceivedTransactions(1);

        //Verification
        assertEquals(2, list.size());

    }

    @Test
    public void transactionsDAOCanGetAllReceivedPaymentDetailsForAUser() {
        //Prepare
        JSONArray json;

        //Method
        json = transactionsDAO.getAllReceivedTransactionDetails(1);

        //Verification
        assertEquals(2, json.size());

    }

    @Test
    public void transactionsDAOCanGetAllUnprocessedTransactionIDs(){
        //Prepare
        ArrayList<Integer> ids;

        //Method
        ids = transactionsDAO.getAllUnprocessedTransactionIDs();

        //Verification
        assertEquals(10, ids.size());
    }
}
