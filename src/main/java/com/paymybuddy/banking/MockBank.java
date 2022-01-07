package com.paymybuddy.banking;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Primary //Remove @Primary and place in real BankController class if implemented
public class MockBank implements BankController{


    private BigDecimal appProfit;
    private static final Logger logger = LogManager.getLogger("MockBank");

    public MockBank() {
        appProfit = new BigDecimal("0");
    }

    public boolean isBankTransactionProcessed(int transactionID) {
        //Code would go here to interact with bank API and confirm transaction has been processed
        return true;
    }

    public void addFee(BigDecimal fee) {
        //Code would go here to interact with bank API and add profit to our account (or whatever process is appropriate)
        appProfit = appProfit.add(fee);
        logger.info("App Profit updated. Current profit: " + appProfit);
    }

}
