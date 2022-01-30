package com.paymybuddy.data.dao.dbConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static com.paymybuddy.data.dao.dbConfig.TestDBConstants.*;


public class TestDAO {


    public DatabaseTestConnection databaseConnection = new DatabaseTestConnection();

    public int setUpTestDB() {
        Connection con = null;

        int affectedRows = 0;
        System.out.println("SETUP1: " + affectedRows);
        try {
            con = databaseConnection.getConnection();
            //Set up users
            for (String line : SETUP_TEST_USERS) {
                PreparedStatement ps = con.prepareStatement(line);
                affectedRows += ps.executeUpdate();
                databaseConnection.closePreparedStatement(ps);
            }
            System.out.println("SETUP USERS: " + affectedRows);
            //Set up relationships
            for (String line : SETUP_TEST_RELS) {
                PreparedStatement ps = con.prepareStatement(line);
                affectedRows += ps.executeUpdate();
                databaseConnection.closePreparedStatement(ps);
            }
            System.out.println("SETUP RELS: " + affectedRows);
            //Set up transactions
            for (String line : SETUP_TEST_TRANSACTIONS) {
                PreparedStatement ps = con.prepareStatement(line);
                affectedRows += ps.executeUpdate();
                databaseConnection.closePreparedStatement(ps);
            }
            System.out.println("SETUP TRAN: " + affectedRows);
            //Set up transactions
            for (String line : SETUP_TEST_BANK_TRANSACTIONS) {
                PreparedStatement ps = con.prepareStatement(line);
                affectedRows += ps.executeUpdate();
                databaseConnection.closePreparedStatement(ps);
            }
            System.out.println("SETUP BANK: " + affectedRows);
            System.out.println("SETUP2: " + affectedRows);
        }
        catch (Exception e) {
            System.out.println("SETUP3: " + e);
        }
        finally {
            return affectedRows;
        }
    }

    public int clearUsersTable() {
        Connection con = null;

        int affectedRows = 0;
        try {
            con = databaseConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(TestDBConstants.CLEAR_USERS);
            affectedRows = ps.executeUpdate();
            databaseConnection.closePreparedStatement(ps);

            ps = con.prepareStatement(RESET_USERS);
            affectedRows = ps.executeUpdate();
            databaseConnection.closePreparedStatement(ps);
        }
        catch (Exception e) {
            System.out.println("ERROR: " + e);
        }
        finally {
            return affectedRows;
        }
    }

    public int clearRelationshipTable() {
        Connection con = null;

        int affectedRows = 0;
        try {
            con = databaseConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(TestDBConstants.CLEAR_RELATIONSHIPS);
            affectedRows = ps.executeUpdate();
            databaseConnection.closePreparedStatement(ps);

            ps = con.prepareStatement(RESET_RELATIONSHIPS);
            affectedRows = ps.executeUpdate();
            databaseConnection.closePreparedStatement(ps);
        }
        catch (Exception e) {
            System.out.println("ERROR: " + e);
        }
        finally {
            return affectedRows;
        }
    }
    public int clearTransactionsTable() {
        Connection con = null;

        int affectedRows = 0;
        try {
            con = databaseConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(TestDBConstants.CLEAR_TRANSACTIONS);
            affectedRows = ps.executeUpdate();
            databaseConnection.closePreparedStatement(ps);

            ps = con.prepareStatement(RESET_TRANSACTIONS);
            affectedRows = ps.executeUpdate();
            databaseConnection.closePreparedStatement(ps);
        }
        catch (Exception e) {
            System.out.println("ERROR: " + e);
        }
        finally {
            return affectedRows;
        }
    }

    public int clearBankTransactionsTable() {
        Connection con = null;

        int affectedRows = 0;
        try {
            con = databaseConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(TestDBConstants.CLEAR_BANK_TRANSACTIONS);
            affectedRows = ps.executeUpdate();
            databaseConnection.closePreparedStatement(ps);

            ps = con.prepareStatement(RESET_BANK_TRANSACTIONS);
            affectedRows = ps.executeUpdate();
            databaseConnection.closePreparedStatement(ps);
        }
        catch (Exception e) {
            System.out.println("ERROR: " + e);
        }
        finally {
            return affectedRows;
        }
    }

    public void clearDB() {
        clearRelationshipTable();
        clearTransactionsTable();
        clearBankTransactionsTable();
        clearUsersTable();

    }

}
