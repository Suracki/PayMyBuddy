package com.paymybuddy.unit.service;

import com.paymybuddy.data.dao.BankTransactionsDAO;
import com.paymybuddy.data.dao.UsersDAO;
import com.paymybuddy.logic.BankTransactionService;
import com.paymybuddy.presentation.model.BankTransaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.paymybuddy.unit.service.TestServiceConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class BankTransactionServiceTest {

    @Mock
    BankTransactionsDAO bankTransactionsDAO;
    @Mock
    UsersDAO usersDAO;
    @InjectMocks
    BankTransactionService bankTransactionService;

    @Test
    public void setBankTransactionServiceCanCreateTransactionForAddingFunds() {
        //Prepare
        LocalDateTime timestamp = LocalDateTime.of(2021,12,12,14,39,34);
        BankTransaction bankTransaction = new BankTransaction(1,new BigDecimal("10"), false, false);
        bankTransaction.setTransactionDate(timestamp);
        doReturn(5).when(bankTransactionsDAO).addTransaction(bankTransaction);

        //Method
        ResponseEntity<String> response = bankTransactionService.addOrRemoveFunds(bankTransaction);

        //Verification
        assertEquals(BANKTSERVICE_ADDFUNDS_RESPONSE, response.toString());
    }

    @Test
    public void setBankTransactionServiceCanCreateTransactionForRemovingFunds() {
        //Prepare
        LocalDateTime timestamp = LocalDateTime.of(2021,12,12,14,39,34);
        BankTransaction bankTransaction = new BankTransaction(1,new BigDecimal("-10"), false, false);
        bankTransaction.setTransactionDate(timestamp);
        doReturn(1).when(usersDAO).subtractFunds(1,new BigDecimal("-10"));
        doReturn(3).when(bankTransactionsDAO).addTransaction(bankTransaction);

        //Method
        ResponseEntity<String> response = bankTransactionService.addOrRemoveFunds(bankTransaction);

        //Verification
        assertEquals(BANKTSERVICE_REMOVEFUNDS_RESPONSE, response.toString());
    }

    @Test
    public void setBankTransactionServiceGivesErrorResponseForAttemptingToRemoveExcessiveFunds() {
        //Prepare
        LocalDateTime timestamp = LocalDateTime.of(2021,12,12,14,39,34);
        BankTransaction bankTransaction = new BankTransaction(1,new BigDecimal("-10"), false, false);
        bankTransaction.setTransactionDate(timestamp);
        doReturn(-1).when(usersDAO).subtractFunds(1,new BigDecimal("-10"));
        //doReturn(5).when(bankTransactionsDAO).addTransaction(bankTransaction);

        //Method
        ResponseEntity<String> response = bankTransactionService.addOrRemoveFunds(bankTransaction);

        //Verification
        assertEquals(BANKTSERVICE_REMOVEFUNDS_FAILRESPONSE, response.toString());
    }


}
