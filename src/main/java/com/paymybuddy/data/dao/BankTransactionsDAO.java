package com.paymybuddy.data.dao;

import com.paymybuddy.data.dao.constants.DBConstants;
import com.paymybuddy.data.dao.dbConfig.DatabaseConnection;
import com.paymybuddy.exceptions.FailedToInsertException;
import com.paymybuddy.presentation.model.BankTransaction;
import com.paymybuddy.presentation.model.Transaction;
import com.paymybuddy.presentation.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

@Service
public class BankTransactionsDAO {

    private static final Logger logger = LogManager.getLogger("UsersDAO");
    @Autowired
    public DatabaseConnection databaseConnection;

    public int addTransaction(BankTransaction bankTransaction) throws FailedToInsertException {
        Connection con = null;
        logger.info("Attempting to add Bank Transaction");

        int transactionID = -1;
        try {
            con = databaseConnection.getConnection();

            con.setAutoCommit(false);

            PreparedStatement ps = con.prepareStatement(DBConstants.ADD_BANK_TRANSACTION, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, bankTransaction.getAcctID());
            ps.setBigDecimal(2, bankTransaction.getAmount());
            ps.setString(3, bankTransaction.getIBAN());
            ps.setString(4, bankTransaction.getBIC());
            logger.debug("PreparedStatement created: " + ps.toString());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                transactionID = rs.getInt(1);
            }
            databaseConnection.closeResultSet(rs);
            databaseConnection.closePreparedStatement(ps);

            con.commit();
            logger.info("Bank Transaction added to database successfully");
        }
        catch (Exception e) {
            con.rollback();
            logger.error("Error adding bank transaction",e);
        }
        finally {
            databaseConnection.closeConnection(con);
            if (transactionID == -1) {
                throw new FailedToInsertException("addTransaction");
            }
            return transactionID;
        }
    }

    public ArrayList<Integer> getAllUnprocessedTransactionIDs() {
        logger.info("Attempting to get unprocessed Bank Transaction IDs");
        Connection con = null;
        ArrayList<Integer> list = new ArrayList<>();
        try {
            con = databaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.GET_UNPROCESSED_BANK_TRANSACTIONS);
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
            logger.info("Unprocessed Bank Transaction IDs retrieved from database successfully");
        }
        catch (Exception e) {
            logger.error("Error obtaining unprocessed bank transactions",e);
        }
        finally {
            databaseConnection.closeConnection(con);
            return list;
        }
    }

    public ArrayList<BankTransaction> getUnprocessedTransactionDetails() {
        logger.info("Attempting to get unprocessed Bank Transaction details");
        Connection con = null;
        ArrayList<BankTransaction> list = new ArrayList<>();
        BankTransaction bankTransaction = null;
        try {
            con = databaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.GET_UNPROCESSED_BANK_TRANSACTIONS_DETAILS);
            logger.debug("PreparedStatement created: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            int columnCount = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                list.add(new BankTransaction(rs.getInt("TransactionID"),
                        rs.getInt("AcctID"),
                        rs.getBigDecimal("Amount"),
                        rs.getString("BankAcctIBAN"),
                        rs.getString("BankAcctBIC"),
                        rs.getBoolean("Processed"),
                        rs.getBoolean("Cancelled"),
                        rs.getTimestamp("TransactionDate").toLocalDateTime()));
            }

            databaseConnection.closeResultSet(rs);
            databaseConnection.closePreparedStatement(ps);
            logger.info("Unprocessed Bank Transaction IDs retrieved from database successfully");
        }
        catch (Exception e) {
            logger.error("Error obtaining user details",e);
        }
        finally {
            databaseConnection.closeConnection(con);
            return list;
        }
    }

    public BankTransaction getTransactionDetails(int transactionID) {
        logger.info("Attempting to get Bank Transaction details for TransactionID " + transactionID);
        Connection con = null;

        BankTransaction bankTransaction = null;
        try {
            con = databaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.GET_BANK_TRANSACTION_BY_ID);
            ps.setInt(1,transactionID);
            logger.debug("PreparedStatement created: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            int columnCount = rs.getMetaData().getColumnCount();
            if (rs.next()) {
                bankTransaction = new BankTransaction();
                bankTransaction.setTransactionID(rs.getInt("TransactionID"));
                bankTransaction.setAcctID(rs.getInt("AcctID"));
                bankTransaction.setAmount(rs.getBigDecimal("Amount"));
                bankTransaction.setProcessed(rs.getBoolean("Processed"));
                bankTransaction.setCancelled(rs.getBoolean("Cancelled"));
                bankTransaction.setTransactionDate(rs.getTimestamp("TransactionDate").toLocalDateTime());
                bankTransaction.setBIC("BankAcctBic");
                bankTransaction.setIBAN("BankAcctIBAN");
            }
            databaseConnection.closeResultSet(rs);
            databaseConnection.closePreparedStatement(ps);
            logger.info("Bank Transaction details retrieved from database successfully");
        }
        catch (Exception e) {
            logger.error("Error obtaining Bank Transaction details",e);
        }
        finally {
            databaseConnection.closeConnection(con);
            return bankTransaction;
        }
    }

    public int markTransactionProcessed(int transactionID){
        logger.info("Attempting to mark Bank Transaction (ID " + transactionID + ") as processed");
        Connection con = null;

        int affectedRows = 0;
        try {
            con = databaseConnection.getConnection();

            con.setAutoCommit(false);

            PreparedStatement ps = con.prepareStatement(DBConstants.UPDATE_BANK_TRANSACTION_PROCESSED);
            ps.setBoolean(1,true);
            ps.setInt(2,transactionID);
            logger.debug("PreparedStatement created: " + ps.toString());
            affectedRows = ps.executeUpdate();
            databaseConnection.closePreparedStatement(ps);

            con.commit();
            logger.info("Bank Transaction status updated successfully");
        }
        catch (Exception e) {
            con.rollback();
            logger.error("Error marking transaction processed",e);
        }
        finally {
            databaseConnection.closeConnection(con);
            return affectedRows;
        }
    }

    public int markTransactionCancelled(int transactionID){
        logger.info("Attempting to mark Bank Transaction (ID " + transactionID + ") as cancelled");
        Connection con = null;

        int affectedRows = 0;
        try {
            con = databaseConnection.getConnection();

            con.setAutoCommit(false);

            PreparedStatement ps = con.prepareStatement(DBConstants.UPDATE_BANK_TRANSACTION_CANCELLED);
            ps.setBoolean(1,true);
            ps.setInt(2,transactionID);
            logger.debug("PreparedStatement created: " + ps.toString());
            affectedRows = ps.executeUpdate();
            databaseConnection.closePreparedStatement(ps);

            con.commit();
            logger.info("Bank Transaction status updated successfully");
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
}
