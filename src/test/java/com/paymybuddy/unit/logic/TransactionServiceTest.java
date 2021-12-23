package com.paymybuddy.unit.logic;

import com.paymybuddy.data.dao.TransactionDAO;
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

import static com.paymybuddy.unit.logic.TestServiceConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    TransactionDAO transactionDAO;

    @InjectMocks
    TransactionService transactionService;

    @Test
    public void transactionServiceCanAddNewTransactions() {
        //Prepare
        LocalDateTime timestamp = LocalDateTime.of(2021,12,12,14,39,34);
        Transaction newTransaction = new Transaction(1, 2, timestamp, "text here", new BigDecimal("15.1"), false);
        ResponseEntity<String> response;
        doReturn(1).when(transactionDAO).addTransaction(newTransaction);

        //Perform
        response = transactionService.makePayment(newTransaction);

        //Verify
        assertEquals(TRANSACTSERVICE_MAKEPAYMENT_RESPONSE, response.toString());
    }

    @Test
    public void transactionServiceCanMarkTransactionAsPaid(){
        //Prepare
        LocalDateTime timestamp = LocalDateTime.of(2021,12,12,14,39,34);
        Transaction newTransaction = new Transaction(1, 2, timestamp, "text here", new BigDecimal("15.1"), false);
        ResponseEntity<String> response;
        doReturn(1).when(transactionDAO).markTransactionPaid(newTransaction);

        //Perform
        response = transactionService.markPaid(newTransaction);

        //Verify
        assertEquals(TRANSACSERVICE_MARKPAID_RESPONSE, response.toString());
    }

    @Test
    public void transactionServiceGivesErrorAttemptingToPayNonexistantTransaction(){
        //Prepare
        LocalDateTime timestamp = LocalDateTime.of(2021,12,12,14,39,34);
        Transaction newTransaction = new Transaction(1, 2, timestamp, "text here", new BigDecimal("15.1"), false);
        ResponseEntity<String> response;
        doReturn(-1).when(transactionDAO).markTransactionPaid(newTransaction);

        //Perform
        response = transactionService.markPaid(newTransaction);

        //Verify
        assertEquals(TRANSACSERVICE_MARKPAID_FAILRESPONSE, response.toString());
    }

    @Test
    public void transactionServiceCanGetTransactionDetailsByID() {
        //Prepare
        LocalDateTime timestamp = LocalDateTime.of(2021,12,12,14,39,34);
        Transaction newTransaction = new Transaction(1, 2, timestamp, "text here", new BigDecimal("15.1"), false);
        ResponseEntity<String> response;
        doReturn(newTransaction).when(transactionDAO).getTransactionByID(1);

        //Perform
        response = transactionService.getTransactionByID(1);

        //Verify
        assertEquals(TRANSACSERVICE_GETTRANS_RESPONSE, response.toString());
    }

    @Test
    public void transactionServiceReturnsErrorWhenNoTransactionFoundForID() {
        //Prepare
        ResponseEntity<String> response;
        doReturn(null).when(transactionDAO).getTransactionByID(1);

        //Perform
        response = transactionService.getTransactionByID(1);

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

}
