package com.paymybuddy.unit.logic;

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
            "  \"password\": \"*********\",\n" +
            "  \"balance\": 0\n" +
            "},[]>";
    public static final String USERSERVICE_CREATED_FAILRESPONSE = "<409 CONFLICT Conflict,[]>";

    public static final String USERSERVICE_UPDATED_RESPONSE = "<200 OK OK,{\n" +
            "  \"acctID\": 3,\n" +
            "  \"firstName\": \"First\",\n" +
            "  \"lastName\": \"Last\",\n" +
            "  \"address\": \"address\",\n" +
            "  \"city\": \"city\",\n" +
            "  \"zip\": \"zip\",\n" +
            "  \"phone\": \"phone\",\n" +
            "  \"email\": \"email\",\n" +
            "  \"password\": \"*********\",\n" +
            "  \"balance\": 0\n" +
            "},[]>";
    public static final String USERSERVICE_UPDATED_FAILRESPONSE = "<404 NOT_FOUND Not Found,Update failed. User does not exist, or password incorrect,[]>";

    public static final String USERSERVICE_DELETED_RESPONSE = "<200 OK OK,1 user(s) deleted,[]>";
    public static final String USERSERVICE_DELETED_FAILRESPONSE = "<404 NOT_FOUND Not Found,Delete failed. User does not exist, or password incorrect,[]>";

    //Constants for RelationshipServiceTest
    public static final String RELSERVICE_CREATED_RESPONSE = "<201 CREATED Created,{\n" +
            "  \"listID\": 3,\n" +
            "  \"listOwnerID\": 1,\n" +
            "  \"friendID\": 2\n" +
            "},[]>";
    public static final String RELSERVICE_CREATED_FAILRESPONSE = "<409 CONFLICT Conflict,[]>";

    public static final String RELSERVICE_DELETED_RESPONSE = "<200 OK OK,Relationship between users 1 and 2 deleted,[]>";
    public static final String RELSERVICE_DELETED_FAILRESPONSE = "<404 NOT_FOUND Not Found,Delete failed. Relationship does not exist, or password incorrect,[]>";

    public static final String RELSERVICE_GETLIST_RESPONSE = "<200 OK OK,[\n" +
            "  2,\n" +
            "  3\n" +
            "],[]>";
    public static final String RELSERVICE_GETLIST_EMPTYRESPONSE = "<200 OK OK,[],[]>";

    //Constants for TransactionServiceTest
    public static final String TRANSACTSERVICE_MAKEPAYMENT_RESPONSE = "<201 CREATED Created,{\n" +
            "  \"transactionID\": 1,\n" +
            "  \"fromAcctID\": 1,\n" +
            "  \"toAcctID\": 2,\n" +
            "  \"transactionDate\": \"12:Dec:2021 14:39:34\",\n" +
            "  \"description\": \"text here\",\n" +
            "  \"amount\": 15.1,\n" +
            "  \"processed\": false\n" +
            "},[]>";

    public static final String TRANSACSERVICE_MARKPAID_RESPONSE = "<200 OK OK,1,[]>";
    public static final String TRANSACSERVICE_MARKPAID_FAILRESPONSE = "<404 NOT_FOUND Not Found,[]>";

    public static final String TRANSACSERVICE_GETTRANS_RESPONSE = "<200 OK OK,{\n" +
            "  \"transactionID\": 0,\n" +
            "  \"fromAcctID\": 1,\n" +
            "  \"toAcctID\": 2,\n" +
            "  \"transactionDate\": \"12:Dec:2021 14:39:34\",\n" +
            "  \"description\": \"text here\",\n" +
            "  \"amount\": 15.1,\n" +
            "  \"processed\": false\n" +
            "},[]>";
    public static final String TRANSACSERVICE_GETTRANS_FAILRESPONSE = "<404 NOT_FOUND Not Found,[]>";

    public static final String TRANSACSERVIVCE_GETALLSENT_RESPONSE = "<200 OK OK,[\n" +
            "  3,\n" +
            "  4,\n" +
            "  7\n" +
            "],[]>";
    public static final String TRANSACSERVIVCE_GETALLSENT_FAILRESPONSE = "<404 NOT_FOUND Not Found,[]>";

    public static final String TRANSACSERVICE_GETALLREC_RESPONSE = "<200 OK OK,[\n" +
            "  3,\n" +
            "  4,\n" +
            "  7\n" +
            "],[]>";
    public static final String TRANSACSERVIVCE_GETALLREC_FAILRESPONSE = "<404 NOT_FOUND Not Found,[]>";
}
