package com.paymybuddy.data.dao.dbConfig;

public class TestDBConstants {
    //Strings used for testing
    public static final String CLEAR_USERS = "DELETE FROM users;";
    public static final String RESET_USERS = "ALTER TABLE users AUTO_INCREMENT=0;";

    public static final String CLEAR_RELATIONSHIPS = "DELETE FROM userrelationships;";
    public static final String RESET_RELATIONSHIPS = "ALTER TABLE userrelationships AUTO_INCREMENT=0;";

    public static final String CLEAR_TRANSACTIONS = "DELETE FROM transactions;";
    public static final String RESET_TRANSACTIONS = "ALTER TABLE transactions AUTO_INCREMENT=0;";

    //Strings used for test db setup
    public static final String SETUP_TEST_USERS[] = {"INSERT INTO test.users (FirstName, LastName, Address, City, Zip, Phone, Email, Password, Balance) VALUES (\"Allison\",\"Boyd\",\"112 Steppes Pl\",\"Culver\",\"97451\",\"841-874-9888\",\"aly@imail.com\",\"password\",0);",
        "INSERT INTO test.users (FirstName, LastName, Address, City, Zip, Phone, Email, Password, Balance) VALUES (\"Jacob\",\"Boyd\",\"1509 Culver St\",\"Culver\",\"97451\",\"841-874-6513\",\"drk@email.com\",\"password\",10);\n",
        "INSERT INTO test.users (FirstName, LastName, Address, City, Zip, Phone, Email, Password, Balance) VALUES (\"John\",\"Boyd\",\"1509 Culver St\",\"Culver\",\"97451\",\"841-874-6512\",\"jaboyd@email.com\",\"password\",20);",
        "INSERT INTO test.users (FirstName, LastName, Address, City, Zip, Phone, Email, Password, Balance) VALUES (\"Tenley\",\"Boyd\",\"1509 Culver St\",\"Culver\",\"97451\",\"841-874-6512\",\"tenz@email.com\",\"password\",30);",
        "INSERT INTO test.users (FirstName, LastName, Address, City, Zip, Phone, Email, Password, Balance) VALUES (\"Eric\",\"Cadigan\",\"951 LoneTree Rd\",\"Culver\",\"97451\",\"841-874-7458\",\"gramps@email.com\",\"password\",40);"};

    public static final String SETUP_TEST_RELS[] = {"INSERT INTO test.userrelationships (ListOwnerID, ListFriendID) VALUES (1,2); ",
            "INSERT INTO test.userrelationships (ListOwnerID, ListFriendID) VALUES (2,1);",
            "INSERT INTO test.userrelationships (ListOwnerID, ListFriendID) VALUES (3,1);",
            "INSERT INTO test.userrelationships (ListOwnerID, ListFriendID) VALUES (4,1);",
            "INSERT INTO test.userrelationships (ListOwnerID, ListFriendID) VALUES (5,1);",
            "INSERT INTO test.userrelationships (ListOwnerID, ListFriendID) VALUES (1,3);",
            "INSERT INTO test.userrelationships (ListOwnerID, ListFriendID) VALUES (2,3);",
            "INSERT INTO test.userrelationships (ListOwnerID, ListFriendID) VALUES (3,2);",
            "INSERT INTO test.userrelationships (ListOwnerID, ListFriendID) VALUES (4,3);",
            "INSERT INTO test.userrelationships (ListOwnerID, ListFriendID) VALUES (5,3);"};

    public static final String SETUP_TEST_TRANSACTIONS[] = {"INSERT INTO test.transactions (FromAcctID, ToAcctID, TransactionDate, Description, Amount, Processed) VALUES (1,2,'2021-09-07 10:01:00',\"Test Payment\",100.00,false);",
    "INSERT INTO test.transactions (FromAcctID, ToAcctID, TransactionDate, Description, Amount, Processed) VALUES (1,2,'2021-10-13 11:09:10',\"Test Payment\",50.00,false);",
    "INSERT INTO test.transactions (FromAcctID, ToAcctID, TransactionDate, Description, Amount, Processed) VALUES (1,3,'2021-11-20 14:20:30',\"Test Payment\",10.50,false);",
    "INSERT INTO test.transactions (FromAcctID, ToAcctID, TransactionDate, Description, Amount, Processed) VALUES (1,4,'2021-12-25 16:40:05',\"Test Payment\",22.33,false);",
    "INSERT INTO test.transactions (FromAcctID, ToAcctID, TransactionDate, Description, Amount, Processed) VALUES (1,5,'2021-12-26 20:55:22',\"Test Payment\",100.01,false);",
    "INSERT INTO test.transactions (FromAcctID, ToAcctID, TransactionDate, Description, Amount, Processed) VALUES (2,1,'2021-09-07 10:01:00',\"Test Payment\",100.00,false);",
    "INSERT INTO test.transactions (FromAcctID, ToAcctID, TransactionDate, Description, Amount, Processed) VALUES (2,1,'2021-10-13 11:09:10',\"Test Payment\",50.00,false);",
    "INSERT INTO test.transactions (FromAcctID, ToAcctID, TransactionDate, Description, Amount, Processed) VALUES (2,3,'2021-11-20 14:20:30',\"Test Payment\",10.50,false);",
    "INSERT INTO test.transactions (FromAcctID, ToAcctID, TransactionDate, Description, Amount, Processed) VALUES (3,4,'2021-12-25 16:40:05',\"Test Payment\",22.33,false);",
    "INSERT INTO test.transactions (FromAcctID, ToAcctID, TransactionDate, Description, Amount, Processed) VALUES (3,5,'2021-12-26 20:55:22',\"Test Payment\",100.01,false);"};





}
