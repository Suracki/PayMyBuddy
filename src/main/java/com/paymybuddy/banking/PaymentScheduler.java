package com.paymybuddy.banking;

import com.paymybuddy.exceptions.UpdateBalanceException;
import com.paymybuddy.logic.PaymentService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PaymentScheduler {

    private static final Logger logger = LogManager.getLogger("PaymentScheduler");

    @Autowired
    PaymentService paymentService;

    @Async
    @Scheduled(fixedRateString = ("${payment.process.schedule.rate}"))
    public void processPendingBankTransactions() {
        int processedPayments = 0;
        try {
            processedPayments = paymentService.processPendingBankTransactions();
            logger.info("Sceduled payment process successfully processed [" + processedPayments + "] transactions.");
        }
        catch (UpdateBalanceException e){
            logger.error("Sceduled payment process failed to add funds with transaction IDs: ");
            for (int id : e.getCancelledTransactions()) {
                logger.error(id);
            }
            logger.info("Sceduled payment process successfully processed [" + e.getSuccessfulTransactions() + "] transactions.");
        }
    }

}
