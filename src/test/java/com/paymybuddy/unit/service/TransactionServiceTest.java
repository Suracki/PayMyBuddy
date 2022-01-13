package com.paymybuddy.unit.service;

import com.nimbusds.jose.shaded.json.JSONArray;
import com.paymybuddy.banking.MockBank;
import com.paymybuddy.data.dao.TransactionDAO;
import com.paymybuddy.data.dao.UsersDAO;
import com.paymybuddy.exceptions.FailToAddUserFundsException;
import com.paymybuddy.exceptions.FailToCreateTransactionRecordException;
import com.paymybuddy.exceptions.FailToMarkTransactionProcessedException;
import com.paymybuddy.exceptions.FailToSubtractUserFundsException;
import com.paymybuddy.logic.TransactionService;
import com.paymybuddy.presentation.model.Transaction;
import com.paymybuddy.presentation.model.User;
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
    @Mock
    UsersDAO usersDAO;
    @Mock
    MockBank mockBank;

    @InjectMocks
    TransactionService transactionService;

    @Test
    public void transactionServiceCanPerformUserToUserTransasctions() throws Exception {
        //Prepare
        ResponseEntity<String> response;
        LocalDateTime timestamp = LocalDateTime.of(2021,12,12,14,39,34);
        Transaction transaction = new Transaction(1, 2, timestamp, "text here", new BigDecimal("10"), false);
        User sender = new User("first","last","address","city","zip","phone","email","pass",new BigDecimal("10"));
        User receiver = new User("first","last","address","city","zip","phone","email","pass",new BigDecimal("10"));

        doReturn(sender).when(usersDAO).getUser(1);
        doReturn(receiver).when(usersDAO).getUser(2);


        //Method
        response = transactionService.performTransactionEx(transaction);

        //Verify
        assertEquals(TRANSACSERVICE_PERFORM_TRANSACTION_SUCCESS, response.toString());
    }

    @Test
    public void transactionServiceWillNotPerformUserToUserTransactionsIfSenderLacksFunds() throws Exception {
        //Prepare
        ResponseEntity<String> response;
        LocalDateTime timestamp = LocalDateTime.of(2021,12,12,14,39,34);
        Transaction transaction = new Transaction(1, 2, timestamp, "text here", new BigDecimal("100"), false);
        User sender = new User("first","last","address","city","zip","phone","email","pass",new BigDecimal("10"));
        sender.setAcctID(1);
        User receiver = new User("first","last","address","city","zip","phone","email","pass",new BigDecimal("10"));

        doReturn(sender).when(usersDAO).getUser(1);
        doReturn(receiver).when(usersDAO).getUser(2);
        doThrow(new FailToSubtractUserFundsException(1,new BigDecimal("100"))).when(usersDAO).subtractFunds(1,new BigDecimal("100"));


        //Method
        response = transactionService.performTransactionEx(transaction);

        //Verify
        assertEquals(TRANSACSERVICE_PERFORM_TRANSACTION_INSUFFICIENT_FUNDS, response.toString());
    }

    @Test
    public void transactionServiceWillRollbackSenderFundsIfTransactionCreateFails() throws Exception {
        //Prepare
        ResponseEntity<String> response;
        LocalDateTime timestamp = LocalDateTime.of(2021,12,12,14,39,34);
        Transaction transaction = new Transaction(1, 2, timestamp, "text here", new BigDecimal("100"), false);
        User sender = new User("first","last","address","city","zip","phone","email","pass",new BigDecimal("10"));
        sender.setAcctID(1);
        User receiver = new User("first","last","address","city","zip","phone","email","pass",new BigDecimal("10"));

        doReturn(sender).when(usersDAO).getUser(1);
        doReturn(receiver).when(usersDAO).getUser(2);
        doThrow(new FailToCreateTransactionRecordException(transaction)).when(transactionDAO).addTransaction(transaction);


        //Method
        response = transactionService.performTransactionEx(transaction);

        //Verify
        assertEquals(TRANSACSERVICE_PERFORM_TRANSACTION_FAILED_CREATING_TRANSACTION_RECORD, response.toString());
    }

    @Test
    public void transactionServiceWillRollbackSenderFundsIfUnableToAddFundsToRecipient() throws Exception {
        //Prepare
        ResponseEntity<String> response;
        LocalDateTime timestamp = LocalDateTime.of(2021,12,12,14,39,34);
        Transaction transaction = new Transaction(1, 2, timestamp, "text here", new BigDecimal("100"), false);
        User sender = new User("first","last","address","city","zip","phone","email","pass",new BigDecimal("10"));
        sender.setAcctID(1);
        User receiver = new User("first","last","address","city","zip","phone","email","pass",new BigDecimal("10"));
        receiver.setAcctID(2);

        doReturn(sender).when(usersDAO).getUser(1);
        doReturn(receiver).when(usersDAO).getUser(2);
        doThrow(new FailToAddUserFundsException(2, new BigDecimal("99.9500"))).when(usersDAO).addFunds(2, new BigDecimal("99.9500"));


        //Method
        response = transactionService.performTransactionEx(transaction);

        //Verify
        assertEquals(TRANSACSERVICE_PERFORM_TRANSACTION_FAILED_ADDING_RECIPIENT_FUNDS, response.toString());
    }

    @Test
    public void transactionServiceWillNotRollbackFundsButWillLogTransactionIDIfUnableToMarkTransactionProcessed() throws Exception {
        //Prepare
        ResponseEntity<String> response;
        LocalDateTime timestamp = LocalDateTime.of(2021,12,12,14,39,34);
        Transaction transaction = new Transaction(1, 2, timestamp, "text here", new BigDecimal("100"), false);
        User sender = new User("first","last","address","city","zip","phone","email","pass",new BigDecimal("10"));
        sender.setAcctID(1);
        User receiver = new User("first","last","address","city","zip","phone","email","pass",new BigDecimal("10"));
        receiver.setAcctID(2);

        doReturn(sender).when(usersDAO).getUser(1);
        doReturn(receiver).when(usersDAO).getUser(2);
        doReturn(3).when(transactionDAO).addTransaction(transaction);
        doThrow(new FailToMarkTransactionProcessedException(transaction)).when(transactionDAO).markTransactionPaid(transaction);


        //Method
        response = transactionService.performTransactionEx(transaction);

        //Verify
        assertEquals(TRANSACSERVICE_PERFORM_TRANSACTION_FAILED_MARKING_TRANSACTION_PROCESSED, response.toString());
    }


    @Test
    public void transactionServiceCanMarkTransactionAsPaid() throws Exception {
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
    public void transactionServiceGivesErrorAttemptingToPayNonexistantTransaction() throws Exception{
        //Prepare
        LocalDateTime timestamp = LocalDateTime.of(2021,12,12,14,39,34);
        Transaction newTransaction = new Transaction(1, 2, timestamp, "text here", new BigDecimal("15.1"), false);
        ResponseEntity<String> response;
        doThrow(new FailToMarkTransactionProcessedException(newTransaction)).when(transactionDAO).markTransactionPaid(newTransaction);

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
