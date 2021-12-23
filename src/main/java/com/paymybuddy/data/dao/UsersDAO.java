package com.paymybuddy.data.dao;

import com.paymybuddy.data.dao.constants.DBConstants;
import com.paymybuddy.data.dao.dbConfig.*;
import com.paymybuddy.presentation.apimodels.UserDTO;
import com.paymybuddy.presentation.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

@Service
public class UsersDAO {

    private static final Logger logger = LogManager.getLogger("UsersDAO");

    @Autowired
    public DatabaseConnection databaseConnection;

    public int verifyUser(String email, String password) {
        Connection con = null;

        int UserID = -1;
        try {
            con = databaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.GET_ACCT_ID_WITH_LOGIN);
            ps.setString(1,email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                UserID = rs.getInt(1);
            }
            databaseConnection.closeResultSet(rs);
            databaseConnection.closePreparedStatement(ps);
        }
        catch (Exception e) {
            logger.error("Error verifying user login",e);
        }
        finally {
            databaseConnection.closeConnection(con);
            return UserID;
        }
    }

    public int getUserID(String email) {
        Connection con = null;

        int UserID = 0;
        try {
            con = databaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.GET_ACCT_ID_BY_EMAIL);
            ps.setString(1,email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                UserID = rs.getInt(1);
            }
            databaseConnection.closeResultSet(rs);
            databaseConnection.closePreparedStatement(ps);
        }
        catch (Exception e) {
            logger.error("Error obtaining User ID",e);
        }
        finally {
            databaseConnection.closeConnection(con);
            return UserID;
        }
    }

    public int addUser(User newUser) {
        Connection con = null;

        int UserID = -1;
        try {
            con = databaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.ADD_USER, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,newUser.getFirstName());
            ps.setString(2, newUser.getLastName());
            ps.setString(3, newUser.getAddress());
            ps.setString(4, newUser.getCity());
            ps.setString(5, newUser.getZip());
            ps.setString(6, newUser.getPhone());
            ps.setString(7, newUser.getEmail());
            ps.setString(8, newUser.getPassword());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                UserID = rs.getInt(1);
            }
            databaseConnection.closeResultSet(rs);
            databaseConnection.closePreparedStatement(ps);
        }
        catch (Exception e) {
            logger.error("Error adding user",e);
        }
        finally {
            databaseConnection.closeConnection(con);
            return UserID;
        }
    }

    public int addUniqueUser(User newUser) {
        Connection con = null;

        int UserID = -1;
        try {
            con = databaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.ADD_UNIQUE_USER, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,newUser.getFirstName());
            ps.setString(2, newUser.getLastName());
            ps.setString(3, newUser.getAddress());
            ps.setString(4, newUser.getCity());
            ps.setString(5, newUser.getZip());
            ps.setString(6, newUser.getPhone());
            ps.setString(7, newUser.getEmail());
            ps.setString(8, newUser.getPassword());
            ps.setBigDecimal(9, new BigDecimal("0"));
            ps.setString(10, newUser.getEmail());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                UserID = rs.getInt(1);
            }
            databaseConnection.closeResultSet(rs);
            databaseConnection.closePreparedStatement(ps);
        }
        catch (Exception e) {
            logger.error("Error adding user",e);
        }
        finally {
            databaseConnection.closeConnection(con);
            return UserID;
        }
    }

    public User getUser(int acctID) {
        Connection con = null;

        User user = null;
        try {
            con = databaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.GET_USER_BY_ID);
            ps.setInt(1,acctID);
            ResultSet rs = ps.executeQuery();
            int columnCount = rs.getMetaData().getColumnCount();
            if (rs.next()) {
                user = new User();
                user.setAcctID(rs.getInt("AcctID"));
                user.setFirstName(rs.getString("FirstName"));
                user.setLastName(rs.getString("LastName"));
                user.setAddress(rs.getString("Address"));
                user.setCity(rs.getString("City"));
                user.setZip(rs.getString("Zip"));
                user.setPhone(rs.getString("Phone"));
                user.setEmail(rs.getString("Email"));
                user.setPassword(rs.getString("Password"));
                user.setBalance(rs.getBigDecimal("Balance"));
            }
            databaseConnection.closeResultSet(rs);
            databaseConnection.closePreparedStatement(ps);
        }
        catch (Exception e) {
            logger.error("Error obtaining user details",e);
        }
        finally {
            databaseConnection.closeConnection(con);
            return user;
        }
    }

    public int addUser(String firstName, String lastName, String address, String city, String zip, String phone,
                       String email, String password) {
        Connection con = null;

        int UserID = -1;
        try {
            con = databaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.ADD_USER, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,firstName);
            ps.setString(2, lastName);
            ps.setString(3, address);
            ps.setString(4, city);
            ps.setString(5, zip);
            ps.setString(6, phone);
            ps.setString(7, email);
            ps.setString(8, password);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                UserID = rs.getInt(1);
            }
            databaseConnection.closeResultSet(rs);
            databaseConnection.closePreparedStatement(ps);
        }
        catch (Exception e) {
            logger.error("Error adding user",e);
        }
        finally {
            databaseConnection.closeConnection(con);
            return UserID;
        }
    }

    public int updateUser(String firstName, String lastName, String address, String city, String zip, String phone,
                       String email, String password, int acctid) {
        Connection con = null;

        int affectedRows = -1;
        try {
            con = databaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.UPDATE_USER, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,firstName);
            ps.setString(2, lastName);
            ps.setString(3, address);
            ps.setString(4, city);
            ps.setString(5, zip);
            ps.setString(6, phone);
            ps.setString(7, email);
            ps.setString(8, password);
            ps.setString(9, acctid+"");
            affectedRows = ps.executeUpdate();
            databaseConnection.closePreparedStatement(ps);
        }
        catch (Exception e) {
            logger.error("Error updating user",e);
        }
        finally {
            databaseConnection.closeConnection(con);
            return affectedRows;
        }
    }

    public int updateUserAuthed(User user) {
        Connection con = null;

        int affectedRows = -1;
        try {
            con = databaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.UPDATE_USER_AUTHED, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setString(3, user.getAddress());
            ps.setString(4, user.getCity());
            ps.setString(5, user.getZip());
            ps.setString(6, user.getPhone());
            ps.setString(7, user.getEmail());
            ps.setString(8, user.getPassword());
            ps.setInt(9, user.getAcctID());
            ps.setString(10, user.getPassword());
            affectedRows = ps.executeUpdate();
            databaseConnection.closePreparedStatement(ps);
        }
        catch (Exception e) {
            logger.error("Error updating user",e);
        }
        finally {
            databaseConnection.closeConnection(con);
            return affectedRows;
        }
    }

    public int updateUser(User updatedUser) {
        Connection con = null;

        int affectedRows = -1;
        try {
            con = databaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.UPDATE_USER, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, updatedUser.getFirstName());
            ps.setString(2, updatedUser.getLastName());
            ps.setString(3, updatedUser.getAddress());
            ps.setString(4, updatedUser.getCity());
            ps.setString(5, updatedUser.getZip());
            ps.setString(6, updatedUser.getPhone());
            ps.setString(7, updatedUser.getEmail());
            ps.setString(8, updatedUser.getPassword());
            ps.setInt(9, updatedUser.getAcctID());
            affectedRows = ps.executeUpdate();
            databaseConnection.closePreparedStatement(ps);
        }
        catch (Exception e) {
            logger.error("Error updating user",e);
        }
        finally {
            databaseConnection.closeConnection(con);
            return affectedRows;
        }
    }

    public int updatePassword(int acctID, String oldPassword, String newPassword){
        Connection con = null;

        int affectedRows = -1;
        try {
            con = databaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.UPDATE_PASSWORD, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, newPassword);
            ps.setInt(2, acctID);
            ps.setString(3, oldPassword);
            affectedRows = ps.executeUpdate();
            databaseConnection.closePreparedStatement(ps);
        }
        catch (Exception e) {
            logger.error("Error updating user",e);
        }
        finally {
            databaseConnection.closeConnection(con);
            return affectedRows;
        }
    }

    public int deleteUser(User deleteUser) {
        Connection con = null;

        int affectedRows = -1;
        try {
            con = databaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.DELETE_USER, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, deleteUser.getAcctID());
            ps.setString(2, deleteUser.getPassword());
            affectedRows = ps.executeUpdate();
            databaseConnection.closePreparedStatement(ps);
        }
        catch (Exception e) {
            logger.error("Error deleting user",e);
        }
        finally {
            databaseConnection.closeConnection(con);
            return affectedRows;
        }
    }


}


