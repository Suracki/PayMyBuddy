package com.paymybuddy.data.dao;

import com.nimbusds.jose.shaded.json.JSONArray;
import com.nimbusds.jose.shaded.json.JSONObject;
import com.paymybuddy.data.dao.constants.DBConstants;
import com.paymybuddy.data.dao.dbConfig.DatabaseConnection;
import com.paymybuddy.presentation.model.Transaction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;

@Service
public class TransactionDAO {

    private static final Logger logger = LogManager.getLogger("UsersDAO");
    @Autowired
    public DatabaseConnection databaseConnection;

    public int addTransaction(Transaction transaction) {
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
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                transactionID = rs.getInt(1);
            }
            databaseConnection.closeResultSet(rs);
            databaseConnection.closePreparedStatement(ps);

            con.commit();
        }
        catch (Exception e) {
            con.rollback();
            logger.error("Error adding transaction",e);
        }
        finally {
            databaseConnection.closeConnection(con);
            return transactionID;
        }
    }

    public int markTransactionPaid(Transaction transaction){
        Connection con = null;

        int affectedRows = 0;
        try {
            con = databaseConnection.getConnection();

            con.setAutoCommit(false);

            PreparedStatement ps = con.prepareStatement(DBConstants.UPDATE_TRANSACTION);
            ps.setBoolean(1,true);
            ps.setInt(2,transaction.getTransactionID());
            ps.executeUpdate();
            databaseConnection.closePreparedStatement(ps);

            con.commit();
        }
        catch (Exception e) {
            con.rollback();
            logger.error("Error marking transaction paid",e);
        }
        finally {
            databaseConnection.closeConnection(con);
            return affectedRows;
        }
    }

    public Transaction getTransactionByID(int TransactionID){
        Connection con = null;

        Transaction transaction = null;
        try {
            con = databaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.GET_TRANSACTION_BY_ID);
            ps.setString(1,TransactionID+"");
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
        }
        catch (Exception e) {
            logger.error("Error obtaining transaction info",e);
        }
        finally {
            databaseConnection.closeConnection(con);
            return transaction;
        }
    }

    public ArrayList<Integer> getAllSentTransactions(int fromAcctID) {
        Connection con = null;

        ArrayList<Integer> list = new ArrayList<>();
        try {
            con = databaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.GET_SENT_TRANSACTIONS);
            ps.setString(1,fromAcctID+"");
            ResultSet rs = ps.executeQuery();
            int columnCount = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    list.add(Integer.parseInt(rs.getString(i)));
                }
            }
            databaseConnection.closeResultSet(rs);
            databaseConnection.closePreparedStatement(ps);
        }
        catch (Exception e) {
            logger.error("Error obtaining sent transactions",e);
        }
        finally {
            databaseConnection.closeConnection(con);
            return list;
        }
    }

    public JSONArray getAllSentTransactionDetails(int fromAcctID) {
        Connection con = null;

        JSONArray json = new JSONArray();
        try {
            con = databaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.GET_SENT_TRANSACTION_DETAILS);
            ps.setInt(1,fromAcctID);
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
        }
        catch (Exception e) {
            logger.error("Error obtaining sent transactions",e);
        }
        finally {
            databaseConnection.closeConnection(con);
            return json;
        }
    }

    public ArrayList<Integer> getAllReceivedTransactions(int fromAcctID) {
        Connection con = null;

        ArrayList<Integer> list = new ArrayList<>();
        try {
            con = databaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.GET_RECEIVED_TRANSACTIONS);
            ps.setString(1,fromAcctID+"");
            ResultSet rs = ps.executeQuery();
            int columnCount = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    list.add(Integer.parseInt(rs.getString(i)));
                }
            }
            databaseConnection.closeResultSet(rs);
            databaseConnection.closePreparedStatement(ps);
        }
        catch (Exception e) {
            logger.error("Error obtaining received transactions",e);
        }
        finally {
            databaseConnection.closeConnection(con);
            return list;
        }
    }


    public JSONArray getAllReceivedTransactionDetails(int fromAcctID) {
        Connection con = null;

        JSONArray json = new JSONArray();
        try {
            con = databaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.GET_RECEIVED_TRANSACTION_DETAILS);
            ps.setInt(1,fromAcctID);
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
        }
        catch (Exception e) {
            logger.error("Error obtaining sent transactions",e);
        }
        finally {
            databaseConnection.closeConnection(con);
            return json;
        }
    }

    public ArrayList<Integer> getAllUnprocessedTransactionIDs() {
        Connection con = null;

        ArrayList<Integer> list = new ArrayList<>();
        try {
            con = databaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.GET_UNPROCESSED_TRANSACTIONS);
            ResultSet rs = ps.executeQuery();
            int columnCount = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    list.add(Integer.parseInt(rs.getString(i)));
                }
            }
            databaseConnection.closeResultSet(rs);
            databaseConnection.closePreparedStatement(ps);
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
