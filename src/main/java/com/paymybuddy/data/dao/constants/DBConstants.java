package com.paymybuddy.data.dao.constants;

public class DBConstants {

    //Strings for UsersDAO
    public static final String GET_ACCT_ID_BY_EMAIL = "SELECT u.AcctID FROM users u WHERE u.Email = ?";
    public static final String GET_ACCT_ID_WITH_LOGIN = "SELECT u.AcctID FROM users u WHERE u.Email=? and u.Password=?";
    public static final String ADD_USER = "INSERT INTO users (FirstName, LastName, Address, City, Zip, Phone, Email, Password, Balance) VALUES (?,?,?,?,?,?,?,?,0);";
    public static final String ADD_UNIQUE_USER = "INSERT INTO users (FirstName, LastName, Address, City, Zip, Phone, Email, Password, Balance)\n" +
            "SELECT * FROM (SELECT ? AS FirstName, ? AS LastName, ? AS Address, ? AS City, ? AS Zip, ? AS Phone, ? AS Email, ? AS Password, ? AS Balance) AS temp\n" +
            "WHERE NOT EXISTS (\n" +
            "    SELECT Email FROM users WHERE Email = ?\n" +
            ") LIMIT 1;";
    public static final String UPDATE_USER = "UPDATE users SET FirstName=?, LastName=?, Address=?, City=?, Zip=?, Phone=?, Email=?, Password=? WHERE AcctID=?";
    public static final String UPDATE_USER_AUTHED = "UPDATE users SET FirstName=?, LastName=?, Address=?, City=?, Zip=?, Phone=?, Email=?, Password=? WHERE AcctID=? AND Password=?";
    public static final String UPDATE_PASSWORD = "UPDATE users SET Password=? WHERE AcctID=? AND Password=?";

    public static final String GET_USER_BY_ID = "SELECT * FROM users u WHERE u.AcctID=?";
    public static final String DELETE_USER = "UPDATE users SET FirstName=\"xxxx\", LastName=\"xxxx\", Address=\"xxxx\", City=\"xxxx\", Zip=\"xxxx\", Phone=\"xxxx\", Email=\"xxxx\", Password=\"xxxx\", Active=0 WHERE AcctID=? AND Password=?;";

    //Strings for RelationshipsDAO
    public static final String ADD_RELATIONSHIP = "INSERT INTO userrelationships (ListOwnerID, ListFriendID) VALUES (?,?);";
    public static final String GET_RELATIONSHIP_ID = "SELECT r.ListID FROM userrelationships r WHERE r.ListOwnerID=? AND r.ListFriendID=?";
    public static final String DELETE_RELATIONSHIP = "DELETE FROM userrelationships r where r.ListID=?";
    public static final String GET_LIST = "SELECT r.ListFriendID FROM userrelationships r where r.ListOwnerID=?";

    //Strings for TransactionDAO
    public static final String GET_SENT_TRANSACTIONS = "SELECT t.TransactionID FROM transactions t WHERE t.FromAcctID=?";
    public static final String GET_RECEIVED_TRANSACTIONS = "SELECT t.TransactionID FROM transactions t WHERE t.ToAcctID=?";
    public static final String GET_TRANSACTION_BY_ID = "SELECT * FROM transactions t WHERE t.TransactionID=?";
    public static final String UPDATE_TRANSACTION = "UPDATE transactions SET Description=?, Processed=? WHERE TransactionID=?";
    public static final String ADD_TRANSACTION = "INSERT INTO transactions (FromAcctID, ToAcctID, TransactionDate, Description, Amount, Processed) VALUES (?,?,?,?,?,?);";

}
