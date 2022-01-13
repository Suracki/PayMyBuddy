package com.paymybuddy.unit.service;

import com.nimbusds.jose.shaded.json.JSONArray;
import com.paymybuddy.data.dao.TransactionDAO;
import com.paymybuddy.exceptions.FailToMarkTransactionProcessedException;
import com.paymybuddy.logic.TransactionService;
import com.paymybuddy.presentation.model.Transaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static com.paymybuddy.unit.service.TestServiceConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    TransactionDAO transactionDAO;

    @InjectMocks
    TransactionService transactionService;

//    @Test
//    public void transactionServiceCanAddNewTransactions() {
//        //Prepare
//        LocalDateTime timestamp = LocalDateTime.of(2021,12,12,14,39,34);
//        Transaction newTransaction = new Transaction(1, 2, timestamp, "text here", new BigDecimal("15.1"), false);
//        ResponseEntity<String> response;
//        doReturn(1).when(transactionDAO).addTransaction(newTransaction);
//
//        //Perform
//        response = transactionService.makePayment(newTransaction);
//
//        //Verify
//        assertEquals(TRANSACTSERVICE_MAKEPAYMENT_RESPONSE, response.toString());
//    }

    @Test
    public void transactionServiceCanMarkTransactionAsPaid() throws Exception{
        //Prepare
        LocalDateTime timestamp = LocalDateTime.of(2021,12,12,14,39,34);
        Transaction newTransaction = new Transaction(1, 2, timestamp, "text here", new BigDecimal("15.1"), false);
        ResponseEntity<String> response;
        doReturn(1).when(transactionDAO).markTransactionPaidEx(newTransaction);

        //Perform
        response = transactionService.markPaid(newTransaction);

        //Verify
        assertEquals(TRANSACSERVICE_MARKPAID_RESPONSE, response.toString());
    }

    @Test
    public void transactionServiceGivesErrorAttemptingToPayNonexistantTransaction() throws Exception{
        //Prepare
        LocalDateTime timestamp = LocalDateTime.of(2021,12,12,14,39,34);
        Transaction newTransaction = new Transaction(1, 2, timestamp, "text here", new BigDecimal("15.1"), false);
        ResponseEntity<String> response;
        doThrow(new FailToMarkTransactionProcessedException(newTransaction)).when(transactionDAO).markTransactionPaidEx(newTransaction);

        //Perform
        response = transactionService.markPaid(newTransaction);

        //Verify
        assertEquals(TRANSACSERVICE_MARKPAID_FAILRESPONSE, response.toString());
    }

    @Test
    public void transactionServiceCanGetTransactionDetailsByID() {
        //Prepare
        LocalDateTime timestamp = LocalDateTime.of(2021,12,12,14,39,34);
        Transaction justTransactionID = new Transaction();
        justTransactionID.setTransactionID(1);
        Transaction newTransaction = new Transaction(1, 2, timestamp, "text here", new BigDecimal("15.1"), false);
        ResponseEntity<String> response;
        doReturn(newTransaction).when(transactionDAO).getTransactionByID(anyInt());

        //Perform
        response = transactionService.getTransactionByID(newTransaction);

        //Verify
        assertEquals(TRANSACSERVICE_GETTRANS_RESPONSE, response.toString());
    }

    @Test
    public void transactionServiceReturnsErrorWhenNoTransactionFoundForID() {
        //Prepare
        ResponseEntity<String> response;
        Transaction justTransactionID = new Transaction();
        justTransactionID.setTransactionID(1);
        doReturn(null).when(transactionDAO).getTransactionByID(1);

        //Perform
        response = transactionService.getTransactionByID(justTransactionID);

        //Verify
        assertEquals(TRANSACSERVICE_GETTRANS_FAILRESPONSE, response.toString());
    }

    @Test
    public void transactionsServiceCanGetAllSentPaymentsForAccountID() {
        //Prepare
        ResponseEntity<String> response;
        ArrayList<Integer> payments = new ArrayList<>();
        payments.add(3);
        payments.add(4);
        payments.add(7);
        doReturn(payments).when(transactionDAO).getAllSentTransactions(1);

        //Perform
        response = transactionService.getAllSentPaymentIDs(1);

        //Verify
        assertEquals(TRANSACSERVIVCE_GETALLSENT_RESPONSE, response.toString());
    }

    @Test
    public void transactionsServiceReturnsErrorWhenNoSentPaymentsForAccountID() {
        //Prepare
        ResponseEntity<String> response;
        ArrayList<Integer> payments = new ArrayList<>();
        doReturn(payments).when(transactionDAO).getAllSentTransactions(1);

        //Perform
        response = transactionService.getAllSentPaymentIDs(1);

        //Verify
        assertEquals(TRANSACSERVIVCE_GETALLSENT_FAILRESPONSE, response.toString());
    }

    @Test
    public void transactionsServiceCanGetAllReceivedPaymentsForAccountID() {
        //Prepare
        ResponseEntity<String> response;
        ArrayList<Integer> payments = new ArrayList<>();
        payments.add(3); payments.add(4); payments.add(7);
        doReturn(payments).when(transactionDAO).getAllReceivedTransactions(1);

        //Perform
        response = transactionService.getAllReceivedPaymentIDs(1);

        //Verify
        assertEquals(TRANSACSERVICE_GETALLREC_RESPONSE, response.toString());
    }

    @Test
    public void transactionsServiceReturnsErrorWhenNoReceivedPaymentsForAccountID() {
        //Prepare
        ResponseEntity<String> response;
        ArrayList<Integer> payments = new ArrayList<>();
        doReturn(payments).when(transactionDAO).getAllReceivedTransactions(1);

        //Perform
        response = transactionService.getAllReceivedPaymentIDs(1);

        //Verify
        assertEquals(TRANSACSERVIVCE_GETALLREC_FAILRESPONSE, response.toString());
    }

    @Test
    public void transactionsServiceCanGetAllSentPaymentDetailsForAccountID() {
        //Prepare
        ResponseEntity<String> response;
        JSONArray json = new JSONArray();
        json.add("{testjson1}");
        json.add("{testjson2}");
        json.add("{testjson3}");
        doReturn(json).when(transactionDAO).getAllSentTransactionDetails(1);

        //Perform
        response = transactionService.getAllSentPaymentDetails(1);

        //Verify
        assertEquals(TRANSACSERVICE_GETSENTDETAILS_RESPONSE, response.toString());
    }

    @Test
    public void transactionsServiceReturnsErrorWhenNoSentPaymentDetailsForAccountID() {
        //Prepare
        ResponseEntity<String> response;
        JSONArray json = new JSONArray();
        doReturn(json).when(transactionDAO).getAllSentTransactionDetails(1);

        //Perform
        response = transactionService.getAllSentPaymentDetails(1);

        //Verify
        assertEquals(TRANSACSERVICE_GETSENTDETAILS_FAILRESPONSE, response.toString());
    }

    @Test
    public void transactionsServiceCanGetAllReceivedPaymentDetailsForAccountID() {
        //Prepare
        ResponseEntity<String> response;
        JSONArray json = new JSONArray();
        json.add("{testjson1}");
        json.add("{testjson2}");
        json.add("{testjson3}");
        doReturn(json).when(transactionDAO).getAllReceivedTransactionDetails(1);

        //Perform
        response = transactionService.getAllReceivedPaymentDetails(1);

        //Verify
        assertEquals(TRANSACSERVICE_GETRECEIVEDDETAILS_RESPONSE, response.toString());
    }

    @Test
    public void transactionsServiceReturnsErrorWhenNoReceivedPaymentDetailsForAccountID() {
        //Prepare
        ResponseEntity<String> response;
        JSONArray json = new JSONArray();
        doReturn(json).when(transactionDAO).getAllReceivedTransactionDetails(1);

        //Perform
        response = transactionService.getAllReceivedPaymentDetails(1);

        //Verify
        assertEquals(TRANSACSERVICE_GETRECEIVEDDETAILS_FAILRESPONSE, response.toString());
    }

    @Test
    public void transactionsServiceCanGetListOfAllUnprocessedPayments() {
        //Prepare
        ResponseEntity<String> response;
        ArrayList<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        doReturn(list).when(transactionDAO).getAllUnprocessedTransactionIDs();

        //Perform
        response = transactionService.getAllUnprocessedTransactions();

        //Verify
        assertEquals(TRANSACSERVICE_GETUNPROCESSED_RESPONSE, response.toString());
    }

    @Test
    public void transactionsServiceGivesErrorWhenNoUnprocessedPayments() {
        //Prepare
        ResponseEntity<String> response;
        ArrayList<Integer> list = new ArrayList<>();
        doReturn(list).when(transactionDAO).getAllUnprocessedTransactionIDs();

        //Perform
        response = transactionService.getAllUnprocessedTransactions();

        //Verify
        assertEquals(TRANSACSERVICE_GETUNPROCESSED_FAILRESPONSE, response.toString());
    }

}
