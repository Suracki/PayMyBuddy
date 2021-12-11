package com.paymybuddy.constants;

public class DBConstants {

    //Strings for UsersDAO
    public static final String GET_USER_ID_BY_EMAIL = "SELECT u.UserID FROM users u WHERE u.Email = ?";
    public static final String GET_USER_ID_WITH_LOGIN = "SELECT u.UserID FROM users u WHERE u.Email=? and u.Password=?";
    public static final String ADD_USER = "INSERT INTO users (FirstName, LastName, Address, City, Zip, Phone, Email, Password, Balance) VALUES (?,?,?,?,?,?,?,?,0);";
    public static final String UPDATE_USER = "UPDATE users SET FirstName=?, LastName=?, Address=?, City=?, Zip=?, Phone=?, Email=?, Password=? WHERE AcctID=?";


    //Strings for RelationshipsDAO
    public static final String ADD_RELATIONSHIP = "INSERT INTO userrelationships (ListOwnerID, ListFriendID) VALUES (?,?);";
    public static final String GET_RELATIONSHIP_ID = "SELECT r.ListID FROM userrelationships r WHERE r.ListOwnerID=? AND r.ListFriendID=?";
    public static final String DELETE_RELATIONSHIP = "DELETE FROM userrelationships r where r.ListID=?";
    public static final String GET_LIST = "SELECT r.ListFriendID FROM userrelationships r where r.ListOwnerID=?";


}
