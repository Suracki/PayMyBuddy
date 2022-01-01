package com.paymybuddy.integration;

public class IntegrationTestConstants {

    //Transaction Integration Test Strings
    public static final String TEST_GET_TRANSACTION = "/transaction?transactionID=1";
    public static final String TEST_GET_TRANSACTION_SENT_IDS = "/transaction/sent/ids?acctID=1";
    public static final String TEST_GET_TRANSACTION_SENT_IDS_SUCCESS = "[\n  1,\n  2,\n  3,\n  4,\n  5\n]";
    public static final String TEST_GET_TRANSACTION_SENT_DETAILS = "/transaction/sent/details?acctID=3";
    public static final String TEST_GET_TRANSACTION_SENT_DETAILS_SUCCESS = "[\n" +
            "  {\n" +
            "    \"Description\": \"Test Payment\",\n" +
            "    \"Amount\": 22.3300,\n" +
            "    \"ToAcctID\": 4,\n" +
            "    \"FromAcctID\": 3,\n" +
            "    \"TransactionID\": 9,\n" +
            "    \"TransactionDate\": \"Dec 25, 2021, 4:40:05 PM\",\n" +
            "    \"Processed\": false\n" +
            "  },\n" +
            "  {\n" +
            "    \"Description\": \"Test Payment\",\n" +
            "    \"Amount\": 100.0100,\n" +
            "    \"ToAcctID\": 5,\n" +
            "    \"FromAcctID\": 3,\n" +
            "    \"TransactionID\": 10,\n" +
            "    \"TransactionDate\": \"Dec 26, 2021, 8:55:22 PM\",\n" +
            "    \"Processed\": false\n" +
            "  }\n" +
            "]";
    public static final String TEST_GET_TRANSACTION_RECEIVED_IDS = "/transaction/received/ids?acctID=1";
    public static final String TEST_GET_TRANSACTION_RECEIVED_DETAILS = "/transaction/received/details?acctID=3";
    public static final String TEST_GET_TRANSACTION_RECEIVED_IDS_SUCCESS = "[\n  6,\n  7\n]";
    public static final String TEST_GET_TRANSACTION_RECEIVED_DETAILS_SUCCESS = "[\n" +
            "  {\n" +
            "    \"Description\": \"Test Payment\",\n" +
            "    \"Amount\": 10.5000,\n" +
            "    \"ToAcctID\": 3,\n" +
            "    \"FromAcctID\": 1,\n" +
            "    \"TransactionID\": 3,\n" +
            "    \"TransactionDate\": \"Nov 20, 2021, 2:20:30 PM\",\n" +
            "    \"Processed\": false\n" +
            "  },\n" +
            "  {\n" +
            "    \"Description\": \"Test Payment\",\n" +
            "    \"Amount\": 10.5000,\n" +
            "    \"ToAcctID\": 3,\n" +
            "    \"FromAcctID\": 2,\n" +
            "    \"TransactionID\": 8,\n" +
            "    \"TransactionDate\": \"Nov 20, 2021, 2:20:30 PM\",\n" +
            "    \"Processed\": false\n" +
            "  }\n" +
            "]";
    public static final String TRANSAC_IT_GET_TRANSACTION = "{\n" +
            "  \"transactionID\": 1,\n" +
            "  \"fromAcctID\": 1,\n" +
            "  \"toAcctID\": 2,\n" +
            "  \"transactionDate\": \"7:Sept:2021 10:01:00\",\n" +
            "  \"description\": \"Test Payment\",\n" +
            "  \"amount\": 100.0000,\n" +
            "  \"processed\": false\n" +
            "}";
    public static final String TRANSAC_IT_ADD_TRANSACTION_REGEX = "\\{\"transactionID\":11,\"fromAcctID\":1,\"toAcctID\":2,\"transactionDate\":\".*\",\"description\":\"test\",\"amount\":1.4,\"processed\":false\\}";
    public static final String TRANSAC_IT_ADD_TRANSACTION_FAIL_RESPONSE = "Unable to add transaction. Ensure sender and receiver exist and are active.";
    public static final String TRANSAC_IT_MARK_PAID_SUCCESS = "Transaction 1 successfully marked as processed";

    //Relationships Integration Test Strings
    public static final String REL_IT_ADD_RELATIONSHIP_SUCCESS = "{\"listID\":11,\"listOwnerID\":1,\"friendID\":5}";
    public static final String REL_IT_DEL_RELATIONSHIP_SUCCESS = "Relationship between users 1 and 2 deleted";
    public static final String REL_IT_GET_REL_IDS = "/relationship?ListOwnerID=1";
    public static final String REL_IT_GET_REL_IDS_SUCCESS = "[\n  2,\n  3\n]";


}
