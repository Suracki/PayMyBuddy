package com.paymybuddy.presentation.apimodels;

import io.swagger.annotations.ApiModel;

import java.math.BigDecimal;

@ApiModel(value="TransactionDTO")
public class TransactionDTO {
    public int fromAcctID;
    public int toAcctID;
    public String description;
    public BigDecimal amount;
}
