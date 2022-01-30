package com.paymybuddy.presentation.apimodels;

import io.swagger.annotations.ApiModel;

import java.math.BigDecimal;

@ApiModel(value="UserDTO")
public class UserDTO {

    public int acctID = 0;
    public String firstName;
    public String lastName;
    public String address;
    public String city;
    public String zip;
    public String phone;
    public String email;
    public String password;
    public BigDecimal balance;
    public String IBAN;
    public String BIC;


}
