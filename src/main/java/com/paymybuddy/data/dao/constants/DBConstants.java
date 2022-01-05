package com.paymybuddy.data.dao.constants;

public class DBConstants {

    //Strings for UsersDAO
    public static final String GET_ACCT_ID_BY_EMAIL = "SELECT u.AcctID FROM users u WHERE u.Email = ?";
    public static final String GET_ID_AND_PASS_BY_EMAIL = "SELECT u.AcctID, u.Password FROM users u WHERE u.Email = ?";
    public static final String ADD_UNIQUE_USER = "INSERT INTO users (FirstName, LastName, Address, City, Zip, Phone, Email, Password, Balance)\n" +
            "SELECT * FROM (SELECT ? AS FirstName, ? AS LastName, ? AS Address, ? AS City, ? AS Zip, ? AS Phone, ? AS Email, ? AS Password, ? AS Balance) AS temp\n" +
            "WHERE NOT EXISTS (\n" +
            "    SELECT Email FROM users WHERE Email = ?\n" +
            ") LIMIT 1;";
    public static final String UPDATE_USER_AUTHED = "UPDATE users SET FirstName=?, LastName=?, Address=?, City=?, Zip=?, Phone=?, Email=? WHERE AcctID=?";
    public static final String UPDATE_PASSWORD = "UPDATE users SET Password=? WHERE AcctID=?";
    public static final String UPDATE_USER_FUNDS = "UPDATE users SET Balance = Balance + ? WHERE AcctID=?";
    public static final String GET_USER_BY_ID = "SELECT * FROM users u WHERE u.AcctID=?";
    public static final String DELETE_USER = "UPDATE users SET FirstName=\"xxxx\", LastName=\"xxxx\", Address=\"xxxx\", City=\"xxxx\", Zip=\"xxxx\", Phone=\"xxxx\", Email=\"xxxx\", Password=\"xxxx\", Active=0 WHERE AcctID=?;";

    //Strings for RelationshipsDAO
    public static final String ADD_UNIQUE_RELATIONSHIP_WITH_ACTIVE = "INSERT INTO userrelationships (ListOwnerID, ListFriendID)\n" +
            "SELECT * FROM (SELECT ? AS ListOwnerID, ? AS ListFriendID) AS temp\n" +
            "WHERE NOT EXISTS (\n" +
            "\tSELECT ListOwnerID FROM userrelationships WHERE ListOwnerID = ? AND ListFriendID = ?\n" +
            ")\n" +
            "AND EXISTS (SELECT AcctID FROM users WHERE AcctID = ? AND Active = true) LIMIT 1;";
    public static final String GET_RELATIONSHIP_ID = "SELECT r.ListID FROM userrelationships r WHERE r.ListOwnerID=? AND r.ListFriendID=?";
    public static final String DELETE_RELATIONSHIP = "DELETE FROM userrelationships r WHERE r.ListOwnerID IN (SELECT AcctID FROM prod.users WHERE AcctID=? AND Active=true) AND r.ListFriendID=?;";
    public static final String GET_LIST = "SELECT r.ListFriendID FROM userrelationships r WHERE r.ListOwnerID=? AND EXISTS (SELECT ListFriendID FROM users WHERE Active=true);";

    //Strings for TransactionDAO
    public static final String GET_SENT_TRANSACTIONS = "SELECT t.TransactionID FROM transactions t WHERE t.FromAcctID=?";
    public static final String GET_SENT_TRANSACTION_DETAILS = "SELECT * FROM transactions t WHERE t.FromAcctID=?";
    public static final String GET_RECEIVED_TRANSACTIONS = "SELECT t.TransactionID FROM transactions t WHERE t.ToAcctID=?";
    public static final String GET_RECEIVED_TRANSACTION_DETAILS = "SELECT * FROM transactions t WHERE t.ToAcctID=?";
    public static final String GET_TRANSACTION_BY_ID = "SELECT * FROM transactions t WHERE t.TransactionID=?";
    public static final String UPDATE_TRANSACTION = "UPDATE transactions SET Processed=? WHERE TransactionID=?";
    public static final String ADD_TRANSACTION = "INSERT INTO transactions (FromAcctID, ToAcctID, Description, Amount, Processed) VALUES (?,?,?,?,?);";
    public static final String GET_UNPROCESSED_TRANSACTIONS = "SELECT TransactionID FROM transactions WHERE Processed=false";

    //Strings for BankTransactionDAO
    public static final String ADD_BANK_TRANSACTION = "INSERT INTO banktransactions (AcctID, Amount) VALUES (?,?);";
    public static final String GET_UNPROCESSED_BANK_TRANSACTIONS = "SELECT TransactionID FROM banktransactions WHERE Processed=false AND Cancelled=false";
    public static final String UPDATE_BANK_TRANSACTION_PROCESSED = "UPDATE banktransactions SET Processed=? WHERE TransactionID=?";
    public static final String UPDATE_BANK_TRANSACTION_CANCELLED = "UPDATE banktransactions SET Cancelled=? WHERE TransactionID=?";
    public static final String GET_BANK_TRANSACTION_BY_ID = "SELECT * FROM banktransactions t WHERE t.TransactionID=?";

}
