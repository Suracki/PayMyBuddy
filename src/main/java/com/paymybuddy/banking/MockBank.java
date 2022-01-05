package com.paymybuddy.banking;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class MockBank implements BankController{


    private BigDecimal appProfit;

    public MockBank() {
        appProfit = new BigDecimal("0");
    }

    public boolean isBankTransactionProcessed(int transactionID) {
        return true;
    }

}
