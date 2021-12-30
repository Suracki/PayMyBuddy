package com.paymybuddy.integration;

public class IntegrationTestConstants {
    public static final String TEST_TRANSACTION_POST = "{ \"amount\": 99, \"description\": \"Test Payment\", \"fromAcctID\": 3, \"processed\": true, \"toAcctID\": 1}";

    public static final String TEST_GET_TRANSACTION = "/transaction?transactionID=1";


    public static final String TRANSAC_IT_ADD_TRANSACTION = "{\n" +
            "  \"transactionID\": 11,\n" +
            "  \"fromAcctID\": 1,\n" +
            "  \"toAcctID\": 2,\n" +
            "  \"transactionDate\": \"27:Dec:2021 18:36:59\",\n" +
            "  \"description\": \"test\",\n" +
            "  \"amount\": 1.4,\n" +
            "  \"processed\": false\n" +
            "}";

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
}
