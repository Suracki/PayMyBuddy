package com.paymybuddy.presentation.apimodels;

import io.swagger.annotations.ApiModel;

import java.math.BigDecimal;

@ApiModel(value="BankTransactionDTO")
public class BankTransactionDTO {

    public int acctID;
    public BigDecimal amount;
    public String IBAN;
    public String BIC;

}
