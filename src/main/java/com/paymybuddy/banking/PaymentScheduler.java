package com.paymybuddy.banking;

import com.paymybuddy.exceptions.UpdateBalanceException;
import com.paymybuddy.logic.PaymentService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * PaymentScheduler is used to schedule usage of PaymentService class to process pending Bank Transactions
 * Used for adding funds to User accounts, and for withdrawing funds when requested
 */
@Component
public class PaymentScheduler {

    private static final Logger logger = LogManager.getLogger("PaymentScheduler");

    @Autowired
    PaymentService paymentService;


    /**
     * Scheduled method to automatically call paymentService.processPendingBankTransactions
     * Ensures system is regularly checked for pending bank transactions, and updates are made as needed
     *
     * @annotation Scheduled fixedRateString is set via "${payment.process.schedule.rate}" in application.properties
     */
    @Async
    @Scheduled(fixedRateString = ("${payment.process.schedule.rate}"))
    public void processPendingBankTransactions() {
        int processedPayments = 0;
        try {
            processedPayments = paymentService.processPendingBankTransactions();
            logger.info("Scheduled payment process successfully processed [" + processedPayments + "] transactions.");
        }
        catch (UpdateBalanceException e){
            logger.error("Scheduled payment process failed to add funds with transaction IDs: ");
            for (int id : e.getCancelledTransactions()) {
                logger.error(id);
            }
            logger.info("Scheduled payment process successfully processed [" + e.getSuccessfulTransactions() + "] transactions.");
        }
    }

}
