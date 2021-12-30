package com.paymybuddy.presentation.apimodels;

import io.swagger.annotations.ApiModel;

@ApiModel(value="UserPass")
public class UserPassDTO {
    public int acctID;
    public String newPassword;
}


