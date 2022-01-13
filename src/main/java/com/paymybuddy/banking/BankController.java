package com.paymybuddy.banking;

import com.paymybuddy.presentation.model.BankTransaction;

import java.math.BigDecimal;

public interface BankController {

    //Methods to interact with bank API and confirm if transaction has been processed
    public boolean isBankTransactionProcessed(BankTransaction transaction);

    //Method to interact with bank API and add profit to our account (or whatever process is appropriate)
    public void addFee(BigDecimal fee);
}
