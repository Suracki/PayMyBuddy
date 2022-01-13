package com.paymybuddy.integration;

public class IntegrationTestConstants {

    //Transaction Integration Test Strings
    public static final String TEST_GET_TRANSACTION = "/transaction?transactionID=1";
    public static final String TEST_GET_UNPROCESSED_TRANSACTION_IDS = "/transaction/unprocessed";
    public static final String TEST_GET_UNPROCESSED_TRANSACTION_IDS_SUCCESS = "[\n  1,\n  2,\n  3,\n  4,\n  5,\n  6,\n  7,\n  8,\n  9,\n  10\n]";
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
    public static final String TRANSAC_IT_ADD_TRANSACTION_REGEX = "\\{\"transactionID\":11,\"fromAcctID\":1,\"toAcctID\":2,\"transactionDate\":\".*\",\"description\":\"test\",\"amount\":1.4,\"processed\":true\\}";
    public static final String TRANSAC_IT_ADD_TRANSACTION_FAIL_RESPONSE = "Unable to add transaction. Failed to load user [22].";
    public static final String TRANSAC_IT_MARK_PAID_SUCCESS = "Transaction 1 successfully marked as processed";

    //Relationships Integration Test Strings
    public static final String REL_IT_ADD_RELATIONSHIP_SUCCESS = "{\"listID\":11,\"listOwnerID\":1,\"friendID\":5}";
    public static final String REL_IT_ADD_RELATIONSHIP_EMAIL_SUCCESS = "{\"listID\":11,\"listOwnerID\":1,\"friendID\":0,\"friendEmail\":\"tash@email.com\"}";
    public static final String REL_IT_DEL_RELATIONSHIP_SUCCESS = "Relationship between users 1 and 2 deleted";
    public static final String REL_IT_GET_REL_IDS = "/relationship?ListOwnerID=1";
    public static final String REL_IT_GET_REL_IDS_SUCCESS = "[\n" +
            "  {\n    \"ListID\": 1,\n    \"ListOwnerID\": 1,\n    \"ListFriendID\": 2\n  },\n" +
            "  {\n    \"ListID\": 6,\n    \"ListOwnerID\": 1,\n    \"ListFriendID\": 3\n  }\n]";

    //Users Integration Test Strings
    public static final String USER_IT_ADD_USER_SUCCESS = "{\"acctID\":7,\"firstName\":\"first\",\"lastName\":\"last\",\"address\":\"add\",\"city\":\"city\",\"zip\":\"zip\",\"phone\":\"123\",\"email\":\"email\",\"password\":\"*********\",\"balance\":0,\"IBAN\":\"FR1420041010050500013M02606\",\"BIC\":\"BNPAGFGX\"}";
    public static final String USER_IT_GET_USER = "/user?acctID=1";
    public static final String USER_IT_GET_USER_SUCCESS = "{\"acctID\":1,\"firstName\":\"Allison\",\"lastName\":\"Boyd\",\"address\":\"112SteppesPl\",\"city\":\"Culver\",\"zip\":\"97451\",\"phone\":\"841-874-9888\",\"email\":\"aly@imail.com\",\"password\":\"*********\",\"balance\":10.0000}";
    public static final String USER_IT_UPDATE_USER_SUCCESS = "{\"acctID\":2,\"firstName\":\"first\",\"lastName\":\"last\",\"address\":\"add\",\"city\":\"city\",\"zip\":\"zip\",\"phone\":\"123\",\"email\":\"email\",\"password\":\"*********\",\"balance\":0}";
    public static final String USER_IT_DEL_USER = "/user?acctID=2";
    public static final String USER_IT_DEL_USER_SUCCESS = "1 user(s) deleted";
    public static final String USER_IT_UPDATE_PW_SUCCESS = "Password updated";
    public static final String USER_IT_AUTH_USER = "/user/auth?email=jaboyd@email.com&password=password";
    public static final String USER_IT_AUTH_USER_SUCCESS = "User 3 authenticated.";
    public static final String USER_IT_AUTH_BAD_USER = "/user/auth?email=jaboyd@email.com&password=badpassword";
    public static final String USER_IT_AUTH_USER_FAIL = "Auth failed. User does not exist, or password incorrect";

}
