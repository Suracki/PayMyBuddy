package com.paymybuddy.banking;

import java.math.BigDecimal;

public interface BankController {

    //Method to interact with bank API and confirm if transaction has been processed
    public boolean isBankTransactionProcessed(int transactionID);

    //Method to interact with bank API and add profit to our account (or whatever process is appropriate)
    public void addFee(BigDecimal fee);
}
