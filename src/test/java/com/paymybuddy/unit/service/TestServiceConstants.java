package com.paymybuddy.unit.service;

public class TestServiceConstants {

    //Constants for UserServiceTest
    public static final String USERSSERVICE_CREATED_RESPONSE = "<201 CREATED Created,{\n" +
            "  \"acctID\": 1,\n" +
            "  \"firstName\": \"First\",\n" +
            "  \"lastName\": \"Last\",\n" +
            "  \"address\": \"address\",\n" +
            "  \"city\": \"city\",\n" +
            "  \"zip\": \"zip\",\n" +
            "  \"phone\": \"phone\",\n" +
            "  \"email\": \"email\",\n" +
            "  \"password\": \"password\",\n" +
            "  \"balance\": 0\n" +
            "},[]>";
    public static final String USERSERVICE_CREATED_FAILRESPONSE = "<409 CONFLICT Conflict,[]>";

    public static final String USERSERVICE_UPDATED_RESPONSE = "<200 OK OK,3,[]>";
    public static final String USERSERVICE_UPDATED_FAILRESPONSE = "<404 NOT_FOUND Not Found,[]>";

    public static final String USERSERVICE_DELETED_RESPONSE = "<200 OK OK,1,[]>";
    public static final String USERSERVICE_DELETED_FAILRESPONSE = "<404 NOT_FOUND Not Found,[]>";

    //Constants for RelationshipServiceTest
    public static final String RELSERVICE_CREATED_RESPONSE = "<201 CREATED Created,{\n" +
            "  \"listID\": 3,\n" +
            "  \"listOwnerID\": 1,\n" +
            "  \"friendID\": 2\n" +
            "},[]>";
    public static final String RELSERVICE_CREATED_FAILRESPONSE = "<409 CONFLICT Conflict,[]>";

    public static final String RELSERVICE_DELETED_RESPONSE = "<200 OK OK,1,[]>";
    public static final String RELSERVICE_DELETED_FAILRESPONSE = "<404 NOT_FOUND Not Found,[]>";

    public static final String RELSERVICE_GETLIST_RESPONSE = "<200 OK OK,[\n" +
            "  2,\n" +
            "  3\n" +
            "],[]>";
    public static final String RELSERVICE_GETLIST_EMPTYRESPONSE = "<200 OK OK,[],[]>";
}
