package com.paymybuddy.presentation.apimodels;

import io.swagger.annotations.ApiModel;

@ApiModel(value="RelationshipGetDTO")
public class RelationshipGetDTO extends RelationshipDTO {

    public int listOwnerID;
    public String listOwnerPassword;

}
