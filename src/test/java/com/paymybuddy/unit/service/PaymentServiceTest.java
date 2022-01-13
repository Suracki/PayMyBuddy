package com.paymybuddy.unit.service;

import com.paymybuddy.banking.MockBank;
import com.paymybuddy.data.dao.BankTransactionsDAO;
import com.paymybuddy.data.dao.UsersDAO;
import com.paymybuddy.exceptions.FailToAddUserFundsException;
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
import static org.mockito.Mockito.doThrow;

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
        ArrayList<BankTransaction> pendingTransactions = new ArrayList<>();
        BankTransaction bankTransactionOne = new BankTransaction(1, new BigDecimal("10"), "FR1420041010050500013M02606","BNPAGFGX", false, false);
        BankTransaction bankTransactionTwo = new BankTransaction(2, new BigDecimal("10"), "FR1420041010050500013M02606","BNPAGFGX", false, false);
        pendingTransactions.add(bankTransactionOne);
        pendingTransactions.add(bankTransactionTwo);
        doReturn(pendingTransactions).when(bankTransactionsDAO).getUnprocessedTransactionDetails();
        doReturn(true).when(bank).isBankTransactionProcessed(bankTransactionOne);
        doReturn(true).when(bank).isBankTransactionProcessed(bankTransactionTwo);
        doReturn(1).when(usersDAO).addFunds(1, new BigDecimal("10"));

        //Method
        int result = paymentService.processPendingBankTransactions();

        //Verification
        assertEquals(2, result);

    }

    @Test
    public void paymentServiceCanCancelPendingBankTransactionsIfUserReachesBalanceCap() throws Exception {
        //Prepare
        ArrayList<BankTransaction> pendingTransactions = new ArrayList<>();
        BankTransaction bankTransactionOne = new BankTransaction(1, new BigDecimal("10"), "FR1420041010050500013M02606","BNPAGFGX", false, false);
        bankTransactionOne.setTransactionID(1);
        BankTransaction bankTransactionTwo = new BankTransaction(2, new BigDecimal("10"), "FR1420041010050500013M02606","BNPAGFGX", false, false);
        bankTransactionTwo.setTransactionID(2);
        pendingTransactions.add(bankTransactionOne);
        pendingTransactions.add(bankTransactionTwo);

        doReturn(pendingTransactions).when(bankTransactionsDAO).getUnprocessedTransactionDetails();
        doReturn(true).when(bank).isBankTransactionProcessed(bankTransactionOne);
        doReturn(true).when(bank).isBankTransactionProcessed(bankTransactionTwo);
        doThrow(new FailToAddUserFundsException(1, new BigDecimal("10"))).when(usersDAO).addFunds(1, new BigDecimal("10"));
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
