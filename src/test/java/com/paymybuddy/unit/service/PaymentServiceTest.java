package com.paymybuddy.unit.service;

import com.paymybuddy.banking.MockBank;
import com.paymybuddy.data.dao.BankTransactionsDAO;
import com.paymybuddy.data.dao.UsersDAO;
import com.paymybuddy.exceptions.UpdateBalanceException;
import com.paymybuddy.logic.PaymentService;
import com.paymybuddy.presentation.model.BankTransaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @Mock
    BankTransactionsDAO bankTransactionsDAO;
    @Mock
    MockBank bank;
    @Mock
    UsersDAO usersDAO;
    @InjectMocks
    PaymentService paymentService;

    @Test
    public void paymentServiceCanProcessPendingBankTransactions() throws Exception{
        //Prepare
        ArrayList<Integer> pendingTransactionIDs = new ArrayList<>();
        pendingTransactionIDs.add(1);
        pendingTransactionIDs.add(2);
        doReturn(pendingTransactionIDs).when(bankTransactionsDAO).getAllUnprocessedTransactionIDs();
        BankTransaction bankTransactionOne = new BankTransaction(1,new BigDecimal("10"),false, false);
        BankTransaction bankTransactionTwo = new BankTransaction(2,new BigDecimal("-20"),false, false);
        doReturn(bankTransactionOne).when(bankTransactionsDAO).getTransactionDetails(1);
        doReturn(bankTransactionTwo).when(bankTransactionsDAO).getTransactionDetails(2);
        doReturn(true).when(bank).isBankTransactionProcessed(1);
        doReturn(true).when(bank).isBankTransactionProcessed(2);
        doReturn(1).when(usersDAO).addFunds(1, new BigDecimal("10"));

        //Method
        int result = paymentService.processPendingBankTransactions();

        //Verification
        assertEquals(2, result);

    }

    @Test
    public void paymentServiceCanCancelPendingBankTransactionsIfUserReachesBalanceCap() {
        //Prepare
        ArrayList<Integer> pendingTransactionIDs = new ArrayList<>();
        pendingTransactionIDs.add(1);
        pendingTransactionIDs.add(2);
        doReturn(pendingTransactionIDs).when(bankTransactionsDAO).getAllUnprocessedTransactionIDs();
        BankTransaction bankTransactionOne = new BankTransaction(1,new BigDecimal("10"),false, false);
        BankTransaction bankTransactionTwo = new BankTransaction(2,new BigDecimal("-20"),false, false);
        doReturn(bankTransactionOne).when(bankTransactionsDAO).getTransactionDetails(1);
        doReturn(bankTransactionTwo).when(bankTransactionsDAO).getTransactionDetails(2);
        doReturn(true).when(bank).isBankTransactionProcessed(1);
        doReturn(true).when(bank).isBankTransactionProcessed(2);
        doReturn(-1).when(usersDAO).addFunds(1, new BigDecimal("10"));
        UpdateBalanceException exception = new UpdateBalanceException(null, 0);

        //Method
        try {
            int result = paymentService.processPendingBankTransactions();
        }
        catch (UpdateBalanceException e) {
            exception = e;
        }

        //Verification
        assertEquals(1, exception.getCancelledTransactions().size());
        assertEquals(1, exception.getSuccessfulTransactions());

    }


}
