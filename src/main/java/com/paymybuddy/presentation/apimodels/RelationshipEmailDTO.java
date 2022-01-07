package com.paymybuddy.presentation.apimodels;

import io.swagger.annotations.ApiModel;

@ApiModel(value="RelationshipDTO")
public class RelationshipEmailDTO {

    public int listOwnerID;
    public String friendEmail;
}
