package com.paymybuddy.presentation.apimodels;

import io.swagger.annotations.ApiModel;

@ApiModel(value="Relationship")
public class RelationshipDTO {

    public int listOwnerID;
    public int friendID;
    public String listOwnerPassword;

}
