package com.paymybuddy.data.dao;

import com.paymybuddy.data.dao.constants.DBConstants;
import com.paymybuddy.data.dao.dbConfig.*;
import com.paymybuddy.exceptions.FailToAddUserFundsException;
import com.paymybuddy.exceptions.FailToLoadUserException;
import com.paymybuddy.exceptions.FailToSubtractUserFundsException;
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

    public String[] getPasswordHash(String email) {
        logger.info("Attempting to get password hash for login: " + email);
        Connection con = null;

        String[] result = {"",""};
        try {
            con = databaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.GET_ID_AND_PASS_BY_EMAIL);
            ps.setString(1, email);
            logger.debug("PreparedStatement created: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                result[0] = rs.getString(1);
                result[1] = rs.getString(2);
            }
            databaseConnection.closeResultSet(rs);
            databaseConnection.closePreparedStatement(ps);
            logger.info("Password hash retrieved from database successfully");
        }
        catch (Exception e) {
            logger.error("Error retrieving password hash",e);
        }
        finally {
            databaseConnection.closeConnection(con);
            return result;
        }
    }

    public int getUserID(String email) {
        logger.info("Attempting to get AcctID for login: " + email);
        Connection con = null;

        int UserID = 0;
        try {
            con = databaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.GET_ACCT_ID_BY_EMAIL);
            ps.setString(1,email);
            logger.debug("PreparedStatement created: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                UserID = rs.getInt(1);
            }
            databaseConnection.closeResultSet(rs);
            databaseConnection.closePreparedStatement(ps);
            logger.info("AcctID "+ UserID + " retrieved from database successfully");
        }
        catch (Exception e) {
            logger.error("Error obtaining AcctID",e);
        }
        finally {
            databaseConnection.closeConnection(con);
            return UserID;
        }
    }

    public int addUniqueUser(User newUser) {
        logger.info("Attempting to add new User to database");
        Connection con = null;

        int UserID = -1;
        try {
            con = databaseConnection.getConnection();

            con.setAutoCommit(false);

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
            ps.setString(10,newUser.getIBAN());
            ps.setString(11,newUser.getBIC());
            ps.setString(12, newUser.getEmail());
            logger.debug("PreparedStatement created: " + ps.toString());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                UserID = rs.getInt(1);
            }
            databaseConnection.closeResultSet(rs);
            databaseConnection.closePreparedStatement(ps);

            con.commit();
            logger.info("User added to database successfully");
        }
        catch (Exception e) {
            con.rollback();
            logger.error("Error adding user",e);
        }
        finally {
            databaseConnection.closeConnection(con);
            return UserID;
        }
    }

    public int addFunds(int acctID, BigDecimal amount) throws FailToAddUserFundsException {
        logger.info("Attempting to add funds to User " + acctID);
        Connection con = null;

        int affectedRows = -1;
        try {
            con = databaseConnection.getConnection();

            con.setAutoCommit(false);

            PreparedStatement ps = con.prepareStatement(DBConstants.UPDATE_USER_FUNDS);
            ps.setBigDecimal(1, amount);
            ps.setInt(2, acctID);
            logger.debug("PreparedStatement created: " + ps.toString());
            affectedRows = ps.executeUpdate();
            databaseConnection.closePreparedStatement(ps);

            con.commit();
            logger.info("Funds added to User record in database successfully");
        }
        catch (Exception e) {
            con.rollback();
            logger.error("Error adding funds to user",e);
        }
        finally {
            databaseConnection.closeConnection(con);
            if (affectedRows == 0) {
                logger.error("Failed to add funds to User " + acctID);
                throw new FailToAddUserFundsException(acctID, amount);
            }
            return affectedRows;
        }
    }

    public int subtractFunds(int acctID, BigDecimal amount) throws FailToSubtractUserFundsException {
        logger.info("Attempting to remove funds from User " + acctID);
        Connection con = null;

        int affectedRows = -1;
        try {
            con = databaseConnection.getConnection();

            con.setAutoCommit(false);

            PreparedStatement ps = con.prepareStatement(DBConstants.UPDATE_SUBTRACT_USER_FUNDS);
            ps.setBigDecimal(1, amount);
            ps.setInt(2, acctID);
            ps.setBigDecimal(3, amount);
            logger.debug("PreparedStatement created: " + ps.toString());
            affectedRows = ps.executeUpdate();
            databaseConnection.closePreparedStatement(ps);

            con.commit();
            logger.info("Funds removed from User record in database successfully");
        }
        catch (Exception e) {
            con.rollback();
            logger.error("Error subtracting funds from user",e);
        }
        finally {
            databaseConnection.closeConnection(con);
            if (affectedRows == 0) {
                logger.error("Failed to remove funds from User " + acctID);
                throw new FailToSubtractUserFundsException(acctID, amount);
            }
            return affectedRows;
        }
    }

    public User getUser(int acctID) throws FailToLoadUserException {
        logger.info("Attempting to retrieve details for User " + acctID);
        Connection con = null;

        User user = null;
        try {
            con = databaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.GET_USER_BY_ID);
            ps.setInt(1,acctID);
            logger.debug("PreparedStatement created: " + ps.toString());
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
            logger.info("User details retrieved from database successfully");
        }
        catch (Exception e) {
            logger.error("Error obtaining user details",e);
        }
        finally {
            databaseConnection.closeConnection(con);
            if (user == null) {
                logger.error("Failed to load User details for AcctID " + acctID);
                throw new FailToLoadUserException(acctID);
            }
            return user;
        }
    }

    public int updateUser(User user) {
        logger.info("Attempting to update details for User " + user.getAcctID());
        Connection con = null;

        int affectedRows = -1;
        try {
            con = databaseConnection.getConnection();

            con.setAutoCommit(false);

            PreparedStatement ps = con.prepareStatement(DBConstants.UPDATE_USER_AUTHED, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setString(3, user.getAddress());
            ps.setString(4, user.getCity());
            ps.setString(5, user.getZip());
            ps.setString(6, user.getPhone());
            ps.setString(7, user.getEmail());
            ps.setString(8,user.getIBAN());
            ps.setString(9,user.getBIC());
            ps.setInt(10, user.getAcctID());
            logger.debug("PreparedStatement created: " + ps.toString());
            affectedRows = ps.executeUpdate();
            databaseConnection.closePreparedStatement(ps);

            con.commit();
            logger.info("User details updated in database successfully");
        }
        catch (Exception e) {
            con.rollback();
            logger.error("Error updating user",e);
        }
        finally {
            databaseConnection.closeConnection(con);
            return affectedRows;
        }
    }

    public int updatePassword(User user, String newPassword){
        logger.info("Attempting to update password for User " + user.getAcctID());
        Connection con = null;

        int affectedRows = -1;
        try {
            con = databaseConnection.getConnection();

            con.setAutoCommit(false);

            PreparedStatement ps = con.prepareStatement(DBConstants.UPDATE_PASSWORD, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, newPassword);
            ps.setInt(2, user.getAcctID());
            logger.debug("PreparedStatement created: " + ps.toString());
            affectedRows = ps.executeUpdate();
            databaseConnection.closePreparedStatement(ps);

            con.commit();
            logger.info("User details updated in database successfully");
        }
        catch (Exception e) {
            con.rollback();
            logger.error("Error updating user",e);
        }
        finally {
            databaseConnection.closeConnection(con);
            return affectedRows;
        }
    }

    public int deleteUser(User deleteUser) {
        logger.info("Attempting to delete User " + deleteUser.getAcctID());
        Connection con = null;

        int affectedRows = -1;
        try {
            con = databaseConnection.getConnection();

            con.setAutoCommit(false);

            PreparedStatement ps = con.prepareStatement(DBConstants.DELETE_USER, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, deleteUser.getAcctID());
            logger.debug("PreparedStatement created: " + ps.toString());
            affectedRows = ps.executeUpdate();
            databaseConnection.closePreparedStatement(ps);

            con.commit();
            logger.info("User deleted successfully");
        }
        catch (Exception e) {
            con.rollback();
            logger.error("Error deleting user",e);
        }
        finally {
            databaseConnection.closeConnection(con);
            return affectedRows;
        }
    }


}


