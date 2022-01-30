package com.paymybuddy.data.dao;

import com.nimbusds.jose.shaded.json.JSONArray;
import com.nimbusds.jose.shaded.json.JSONObject;
import com.paymybuddy.data.dao.constants.DBConstants;
import com.paymybuddy.data.dao.dbConfig.DatabaseConnection;
import com.paymybuddy.exceptions.FailToCreateTransactionRecordException;
import com.paymybuddy.exceptions.FailToMarkTransactionProcessedException;
import com.paymybuddy.presentation.model.Transaction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;

/**
 * TransactionDAO contains all methods for interacting directly with the Transactions table of the database
 */
@Service
public class TransactionDAO {

    private static final Logger logger = LogManager.getLogger("UsersDAO");
    @Autowired
    public DatabaseConnection databaseConnection;

    /**
     * Method to add a transaction record between two specific users
     *
     *
     * @param transaction Transaction object containing details to be added
     * @return automatically generated TransactionID integer value from database
     * @throws FailToCreateTransactionRecordException if record cannot be created
     */
    public int addTransaction(Transaction transaction) throws FailToCreateTransactionRecordException {
        logger.info("Attempting to add Transaction");
        Connection con = null;

        int transactionID = -1;
        try {
            con = databaseConnection.getConnection();

            con.setAutoCommit(false);

            PreparedStatement ps = con.prepareStatement(DBConstants.ADD_TRANSACTION, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, transaction.getFromAcctID()+"");
            ps.setString(2, transaction.getToAcctID()+"");
            ps.setString(3, transaction.getDescription());
            ps.setBigDecimal(4, transaction.getAmount());
            ps.setBoolean(5, transaction.isProcessed());
            logger.debug("PreparedStatement created: " + ps.toString());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                transactionID = rs.getInt(1);
            }
            databaseConnection.closeResultSet(rs);
            databaseConnection.closePreparedStatement(ps);

            con.commit();
            logger.info("Transaction added to database successfully");
        }
        catch (Exception e) {
            con.rollback();
            logger.error("Error adding transaction",e);
        }
        finally {
            if (transactionID == -1) {
                logger.error("Failed to create Transaction entry in database");
                throw new FailToCreateTransactionRecordException(transaction);
            }
            databaseConnection.closeConnection(con);
            return transactionID;
        }
    }

    /**
     * Method to mark a transaction record as paid/complete
     *
     * @param transaction Transaction object containing details of specific transaction
     * @return number of transactions updated
     * @throws FailToMarkTransactionProcessedException if record cannot be updated
     */
    public int markTransactionPaid(Transaction transaction) throws FailToMarkTransactionProcessedException {
        logger.info("Attempting to mark Bank Transaction as paid");
        Connection con = null;

        int affectedRows = 0;
        try {
            con = databaseConnection.getConnection();

            con.setAutoCommit(false);

            PreparedStatement ps = con.prepareStatement(DBConstants.UPDATE_TRANSACTION);
            ps.setBoolean(1,true);
            ps.setInt(2,transaction.getTransactionID());
            logger.debug("PreparedStatement created: " + ps.toString());
            affectedRows = ps.executeUpdate();
            databaseConnection.closePreparedStatement(ps);

            con.commit();
            logger.info("Transaction marked as paid successfully");
        }
        catch (Exception e) {
            con.rollback();
            logger.error("Error marking transaction paid",e);
        }
        finally {
            databaseConnection.closeConnection(con);
            if (affectedRows == 0) {
                logger.error("Failed to update Transaction entry in database");
                throw new FailToMarkTransactionProcessedException(transaction);
            }
            return affectedRows;
        }
    }

    /**
     * Method to mark a transaction record as cancelled
     * Cancelling a transaction involves updating the amount to 0, and setting it to processed.
     * This ensures a record of attempted transactions is maintained.
     *
     * @param transaction Transaction object containing details of specific transaction
     * @return number of transactions updated. Can be 0 if no transaction found matching provided details.
     */
    public int cancelTransaction(Transaction transaction){
        logger.info("Attempting to mark Bank Transaction as cancelled");
        Connection con = null;

        int affectedRows = 0;
        try {
            con = databaseConnection.getConnection();

            con.setAutoCommit(false);

            PreparedStatement ps = con.prepareStatement(DBConstants.UPDATE_TRANSACTION_CANCELLED);
            ps.setInt(1,transaction.getTransactionID());
            logger.debug("PreparedStatement created: " + ps.toString());
            affectedRows = ps.executeUpdate();
            databaseConnection.closePreparedStatement(ps);

            con.commit();
            logger.info("Transaction marked as cancelled successfully");
        }
        catch (Exception e) {
            con.rollback();
            logger.error("Error marking transaction cancelled",e);
        }
        finally {
            databaseConnection.closeConnection(con);
            return affectedRows;
        }
    }

    /**
     * Method to get details of a specific transaction
     *
     * @param transactionID ID of transaction to be retrieved
     * @return Transaction object containing all transaction details. Can be null if transaction not found with this ID.
     */
    public Transaction getTransactionByID(int transactionID){
        logger.info("Attempting to get Transaction details for ID " + transactionID);
        Connection con = null;

        Transaction transaction = null;
        try {
            con = databaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.GET_TRANSACTION_BY_ID);
            ps.setInt(1,transactionID);
            logger.debug("PreparedStatement created: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            int columnCount = rs.getMetaData().getColumnCount();
            if (rs.next()) {
                transaction = new Transaction();
                transaction.setTransactionID(rs.getInt("TransactionID"));
                transaction.setFromAcctID(rs.getInt("FromAcctID"));
                transaction.setToAcctID(rs.getInt("ToAcctID"));
                transaction.setTransactionDate(rs.getTimestamp("TransactionDate").toLocalDateTime());
                transaction.setDescription(rs.getString("Description"));
                transaction.setAmount(rs.getBigDecimal("Amount"));
                transaction.setProcessed(rs.getBoolean("Processed"));
            }
            databaseConnection.closeResultSet(rs);
            databaseConnection.closePreparedStatement(ps);
            logger.info("Transaction details retrieved from database successfully");
        }
        catch (Exception e) {
            logger.error("Error obtaining transaction details",e);
        }
        finally {
            databaseConnection.closeConnection(con);
            return transaction;
        }
    }

    /**
     * Method to get Transaction IDs of all transactions from a specific user
     *
     * @param fromAcctID ID of User
     * @return ArrayList of TransactionID ints. Can be empty if user has not sent any transactions.
     */
    public ArrayList<Integer> getAllSentTransactions(int fromAcctID) {
        logger.info("Attempting to get all sent Transaction IDs for AcctID " + fromAcctID);
        Connection con = null;

        ArrayList<Integer> list = new ArrayList<>();
        try {
            con = databaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.GET_SENT_TRANSACTIONS);
            ps.setString(1,fromAcctID+"");
            logger.debug("PreparedStatement created: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            int columnCount = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    list.add(Integer.parseInt(rs.getString(i)));
                }
            }
            databaseConnection.closeResultSet(rs);
            databaseConnection.closePreparedStatement(ps);
            logger.info("Transaction IDs retrieved from database successfully");
        }
        catch (Exception e) {
            logger.error("Error obtaining sent transactions",e);
        }
        finally {
            databaseConnection.closeConnection(con);
            return list;
        }
    }

    /**
     * Method to get details of all transactions from a specific user
     *
     * @param fromAcctID ID of User
     * @return JSONArray of details of all found transactions. Can be empty if user has not sent any transactions.
     */
    public JSONArray getAllSentTransactionDetails(int fromAcctID) {
        logger.info("Attempting to get all sent Transaction details for AcctID " + fromAcctID);
        Connection con = null;

        JSONArray json = new JSONArray();
        try {
            con = databaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.GET_SENT_TRANSACTION_DETAILS);
            ps.setInt(1,fromAcctID);
            logger.debug("PreparedStatement created: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            int columnCount = rs.getMetaData().getColumnCount();

            ResultSetMetaData rsmd = rs.getMetaData();
            while (rs.next()) {
                JSONObject obj = new JSONObject();
                for (int i=1; i<=columnCount; i++) {
                    String column_name = rsmd.getColumnName(i);
                    obj.put(column_name, rs.getObject(column_name));
                }
                json.add(obj);
            }
            databaseConnection.closeResultSet(rs);
            databaseConnection.closePreparedStatement(ps);
            logger.info("Transaction details retrieved from database successfully");
        }
        catch (Exception e) {
            logger.error("Error obtaining sent transactions",e);
        }
        finally {
            databaseConnection.closeConnection(con);
            return json;
        }
    }

    /**
     * Method to get Transaction IDs of all transactions to a specific user
     *
     * @param fromAcctID ID of User
     * @return ArrayList of TransactionID ints. Can be empty if user has not sent any transactions.
     */
    public ArrayList<Integer> getAllReceivedTransactions(int fromAcctID) {
        logger.info("Attempting to get all received Transaction IDs for AcctID " + fromAcctID);
        Connection con = null;

        ArrayList<Integer> list = new ArrayList<>();
        try {
            con = databaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.GET_RECEIVED_TRANSACTIONS);
            ps.setInt(1,fromAcctID);
            logger.debug("PreparedStatement created: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            int columnCount = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    list.add(Integer.parseInt(rs.getString(i)));
                }
            }
            databaseConnection.closeResultSet(rs);
            databaseConnection.closePreparedStatement(ps);
            logger.info("Transaction IDs retrieved from database successfully");
        }
        catch (Exception e) {
            logger.error("Error obtaining received transactions",e);
        }
        finally {
            databaseConnection.closeConnection(con);
            return list;
        }
    }

    /**
     * Method to get details of all transactions to a specific user
     *
     * @param fromAcctID ID of User
     * @return JSONArray of details of all found transactions. Can be empty if user has not sent any transactions.
     */
    public JSONArray getAllReceivedTransactionDetails(int fromAcctID) {
        logger.info("Attempting to get all received Transaction details for AcctID " + fromAcctID);
        Connection con = null;

        JSONArray json = new JSONArray();
        try {
            con = databaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.GET_RECEIVED_TRANSACTION_DETAILS);
            ps.setInt(1,fromAcctID);
            logger.debug("PreparedStatement created: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            int columnCount = rs.getMetaData().getColumnCount();

            ResultSetMetaData rsmd = rs.getMetaData();
            while (rs.next()) {
                JSONObject obj = new JSONObject();
                for (int i=1; i<=columnCount; i++) {
                    String column_name = rsmd.getColumnName(i);
                    obj.put(column_name, rs.getObject(column_name));
                }
                json.add(obj);
            }
            databaseConnection.closeResultSet(rs);
            databaseConnection.closePreparedStatement(ps);
            logger.info("Transaction details retrieved from database successfully");
        }
        catch (Exception e) {
            logger.error("Error obtaining sent transactions",e);
        }
        finally {
            databaseConnection.closeConnection(con);
            return json;
        }
    }

    /**
     * Method to get Transaction IDs of all unprocessed transactions
     *
     * @return ArrayList of TransactionID ints. Can be empty if user has not sent any transactions.
     */
    public ArrayList<Integer> getAllUnprocessedTransactionIDs() {
        logger.info("Attempting to get all unprocessed Transaction IDs");
        Connection con = null;

        ArrayList<Integer> list = new ArrayList<>();
        try {
            con = databaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.GET_UNPROCESSED_TRANSACTIONS);
            logger.debug("PreparedStatement created: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            int columnCount = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    list.add(Integer.parseInt(rs.getString(i)));
                }
            }
            databaseConnection.closeResultSet(rs);
            databaseConnection.closePreparedStatement(ps);
            logger.info("Transaction IDs retrieved from database successfully");
        }
        catch (Exception e) {
            logger.error("Error obtaining unprocessed transactions",e);
        }
        finally {
            databaseConnection.closeConnection(con);
            return list;
        }
    }
}
