package com.paymybuddy.dao;

import com.paymybuddy.constants.DBConstants;
import com.paymybuddy.dbConfig.DatabaseConnection;
import com.paymybuddy.model.Transaction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;

public class TransactionDAO {

    private static final Logger logger = LogManager.getLogger("UsersDAO");
    public DatabaseConnection databaseConnection = new DatabaseConnection();

    public int addTransaction(Transaction transaction) {
        Connection con = null;

        int transactionID = -1;
        try {
            con = databaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.ADD_TRANSACTION, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, transaction.getFromAcctID()+"");
            ps.setString(2, transaction.getToAcctID()+"");
            ps.setTimestamp(3, Timestamp.valueOf(transaction.getTransactionDate()));
            ps.setString(4, transaction.getDescription());
            ps.setBigDecimal(5, transaction.getAmount());
            ps.setBoolean(6, transaction.isProcessed());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                transactionID = rs.getInt(1);
            }
            databaseConnection.closeResultSet(rs);
            databaseConnection.closePreparedStatement(ps);
        }
        catch (Exception e) {
            logger.error("Error adding user relationship",e);
        }
        finally {
            return transactionID;
        }
    }

    public void markTransactionPaid(Transaction transaction){
        Connection con = null;

        int affectedRows = 0;
        try {
            con = databaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.UPDATE_TRANSACTION);
            ps.setString(1,transaction.getDescription());
            ps.setBoolean(2,transaction.isProcessed());
            ps.setString(3,transaction.getTransactionID()+"");
            ps.executeUpdate();
            databaseConnection.closePreparedStatement(ps);
        }
        catch (Exception e) {
            logger.error("Error obtaining User ID",e);
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
            logger.error("Error obtaining sent transactions",e);
        }
        finally {
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
                    System.out.println(rs.getString(i));
                }
            }
            databaseConnection.closeResultSet(rs);
            databaseConnection.closePreparedStatement(ps);
        }
        catch (Exception e) {
            logger.error("Error obtaining sent transactions",e);
        }
        finally {
            return list;
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
                    System.out.println(rs.getString(i));
                }
            }
            databaseConnection.closeResultSet(rs);
            databaseConnection.closePreparedStatement(ps);
        }
        catch (Exception e) {
            logger.error("Error obtaining received transactions",e);
        }
        finally {
            return list;
        }
    }

}
