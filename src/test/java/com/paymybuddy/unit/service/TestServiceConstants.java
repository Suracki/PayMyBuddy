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
            "  \"password\": \"*********\",\n" +
            "  \"balance\": 0\n" +
            "},[]>";
    public static final String USERSERVICE_CREATED_FAILRESPONSE = "<409 CONFLICT Conflict,[]>";
    public static final String USERSERVICE_GET_RESPONSE = "<200 OK OK,{\n" +
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

    public static final String USERSERVICE_CHANGEPASS_RESPONSE = "<200 OK OK,Password updated,[]>";
    public static final String USERSERVICE_CHANGEPASS_FAILRESPONSE = "<404 NOT_FOUND Not Found,Update failed. User does not exist, or original password incorrect,[]>";
    public static final String USERSERVICE_AUTH_RESPONSE = "<200 OK OK,User 1 authenticated.,[]>";
    public static final String USERSERVICE_AUTH_FAILRESPONSE = "<404 NOT_FOUND Not Found,Auth failed. User does not exist, or password incorrect,[]>";

    //Constants for RelationshipServiceTest
    public static final String RELSERVICE_CREATED_RESPONSE = "<201 CREATED Created,{\n" +
            "  \"listID\": 3,\n" +
            "  \"listOwnerID\": 1,\n" +
            "  \"friendID\": 2\n" +
            "},[]>";
    public static final String RELSERVICE_CREATED_FAILRESPONSE = "<409 CONFLICT Conflict,[]>";
    public static final String RELSERVICE_CREATED_EMAIL_RESPONSE = "<201 CREATED Created,{\n" +
            "  \"listID\": 3,\n" +
            "  \"listOwnerID\": 1,\n" +
            "  \"friendID\": 0,\n" +
            "  \"friendEmail\": \"email\"\n" +
            "},[]>";

    public static final String RELSERVICE_DELETED_RESPONSE = "<200 OK OK,Relationship between users 1 and 2 deleted,[]>";
    public static final String RELSERVICE_DELETED_FAILRESPONSE = "<404 NOT_FOUND Not Found,Delete failed. Relationship does not exist, or password incorrect,[]>";

    public static final String RELSERVICE_GETLIST_RESPONSE = "<200 OK OK,[\n" +
            "  {\n" +
            "    \"ListID\": 1,\n" +
            "    \"ListOwnerID\": 1,\n" +
            "    \"ListFriendID\": 2\n" +
            "  }\n" +
            "],[]>";
    public static final String RELSERVICE_GETLIST_EMPTYRESPONSE = "<200 OK OK,[],[]>";

    //Constants for TransactionServiceTest
    public static final String TRANSACSERVICE_PERFORM_TRANSACTION_SUCCESS = "<201 CREATED Created,{\n" +
            "  \"transactionID\": 0,\n" +
            "  \"fromAcctID\": 1,\n" +
            "  \"toAcctID\": 2,\n" +
            "  \"transactionDate\": \"12:Dec:2021 14:39:34\",\n" +
            "  \"description\": \"text here\",\n" +
            "  \"amount\": 10,\n" +
            "  \"processed\": true\n" +
            "},[]>";
    public static final String TRANSACSERVICE_PERFORM_TRANSACTION_INSUFFICIENT_FUNDS = "<400 BAD_REQUEST Bad Request,Unable to add transaction. Ensure sufficient funds are available.,[]>";
    public static final String TRANSACSERVICE_PERFORM_TRANSACTION_FAILED_CREATING_TRANSACTION_RECORD = "<500 INTERNAL_SERVER_ERROR Internal Server Error,Error adding transaction to database. Funds returned to user.,[]>";
    public static final String TRANSACSERVICE_PERFORM_TRANSACTION_FAILED_ADDING_RECIPIENT_FUNDS = "<500 INTERNAL_SERVER_ERROR Internal Server Error,Error adding funds to recipient. Funds returned to user.,[]>";
    public static final String TRANSACSERVICE_PERFORM_TRANSACTION_FAILED_MARKING_TRANSACTION_PROCESSED = "<500 INTERNAL_SERVER_ERROR Internal Server Error,Transaction processed successfully. Transaction Record failed to be marked as Processed. TransactionID [3],[]>";

    public static final String TRANSACSERVICE_MARKPAID_RESPONSE = "<200 OK OK,Transaction 0 successfully marked as processed,[]>";
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

    public static final String TRANSACSERVICE_GETSENTDETAILS_RESPONSE = "<200 OK OK,[\n" +
            "  \"{testjson1}\",\n" +
            "  \"{testjson2}\",\n" +
            "  \"{testjson3}\"\n" +
            "],[]>";
    public static final String TRANSACSERVICE_GETSENTDETAILS_FAILRESPONSE = "<404 NOT_FOUND Not Found,[]>";

    public static final String TRANSACSERVICE_GETALLREC_RESPONSE = "<200 OK OK,[\n" +
            "  3,\n" +
            "  4,\n" +
            "  7\n" +
            "],[]>";
    public static final String TRANSACSERVIVCE_GETALLREC_FAILRESPONSE = "<404 NOT_FOUND Not Found,[]>";

    public static final String TRANSACSERVICE_GETRECEIVEDDETAILS_RESPONSE = "<200 OK OK,[\n" +
            "  \"{testjson1}\",\n" +
            "  \"{testjson2}\",\n" +
            "  \"{testjson3}\"\n" +
            "],[]>";
    public static final String TRANSACSERVICE_GETRECEIVEDDETAILS_FAILRESPONSE = "<404 NOT_FOUND Not Found,[]>";

    public static final String TRANSACSERVICE_GETUNPROCESSED_RESPONSE = "<200 OK OK,[\n" +
            "  1,\n" +
            "  2\n" +
            "],[]>";
    public static final String TRANSACSERVICE_GETUNPROCESSED_FAILRESPONSE = "<404 NOT_FOUND Not Found,[]>";

    //Constants for BankTransactionServiceTest
    public static final String BANKTSERVICE_ADDFUNDS_RESPONSE = "<200 OK OK,{\n" +
            "  \"transactionID\": 5,\n" +
            "  \"acctID\": 1,\n" +
            "  \"amount\": 10,\n" +
            "  \"processed\": false,\n" +
            "  \"cancelled\": false,\n" +
            "  \"transactionDate\": \"12:Dec:2021 14:39:34\"\n" +
            "},[]>";
    public static final String BANKTSERVICE_REMOVEFUNDS_RESPONSE = "<200 OK OK,{\n" +
            "  \"transactionID\": 3,\n" +
            "  \"acctID\": 1,\n" +
            "  \"amount\": -10,\n" +
            "  \"processed\": false,\n" +
            "  \"cancelled\": false,\n" +
            "  \"transactionDate\": \"12:Dec:2021 14:39:34\"\n" +
            "},[]>";
    public static final String BANKTSERVICE_REMOVEFUNDS_FAILRESPONSE = "<400 BAD_REQUEST Bad Request,Unable to add bank transaction. Failed to remove funds from user account.,[]>";
    public static final String BANKTSERVICE_REMOVEFUNDS_ERROR_CREATING_RECORD_RESPONSE = "<500 INTERNAL_SERVER_ERROR Internal Server Error,Unable to add bank transaction.,[]>";
    public static final String BANKTSERVICE_REMOVEFUNDS_ERROR_RETURNING_FUNDS_RESPONSE = "<500 INTERNAL_SERVER_ERROR Internal Server Error,Unable to add bank transaction. Failed to restore removed funds,[]>";
    public static final String BANKTSERVICE_GETALLDETAILS_RESPONSE = "<200 OK OK,[\n  \"{testjson1}\",\n  \"{testjson2}\",\n  \"{testjson3}\"\n],[]>";
    public static final String BANKTSERVICE_GETTRANS_RESPONSE = "<200 OK OK,{\n" +
            "  \"transactionID\": 0,\n" +
            "  \"acctID\": 1,\n" +
            "  \"amount\": 100,\n" +
            "  \"processed\": false,\n" +
            "  \"cancelled\": false,\n" +
            "  \"transactionDate\": \"12:Dec:2021 14:39:34\"\n" +
            "},[]>";
    public static final String BANKTSERVICE_GETTRANS_FAILRESPONSE = "<404 NOT_FOUND Not Found,[]>";

}
