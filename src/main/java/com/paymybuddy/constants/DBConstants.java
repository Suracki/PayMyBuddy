package com.paymybuddy.constants;

public class DBConstants {

    //Strings for UsersDAO
    public static final String GET_ACCT_ID_BY_EMAIL = "SELECT u.AcctID FROM users u WHERE u.Email = ?";
    public static final String GET_ACCT_ID_WITH_LOGIN = "SELECT u.AcctID FROM users u WHERE u.Email=? and u.Password=?";
    public static final String ADD_USER = "INSERT INTO users (FirstName, LastName, Address, City, Zip, Phone, Email, Password, Balance) VALUES (?,?,?,?,?,?,?,?,0);";
    public static final String UPDATE_USER = "UPDATE users SET FirstName=?, LastName=?, Address=?, City=?, Zip=?, Phone=?, Email=?, Password=? WHERE AcctID=?";
    public static final String GET_USER_BY_ID = "SELECT * FROM users u WHERE u.AcctID=?";

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
