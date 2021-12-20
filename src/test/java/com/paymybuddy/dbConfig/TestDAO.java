package com.paymybuddy.dbConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static com.paymybuddy.dbConfig.TestDBConstants.*;


public class TestDAO {


    public DatabaseTestConnection databaseConnection = new DatabaseTestConnection();

    public int setUpTestDB() {
        Connection con = null;
        databaseConnection.databaseUrl = "jdbc:mysql://localhost:3306/test";

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
            //Set up relationships
            for (String line : SETUP_TEST_RELS) {
                PreparedStatement ps = con.prepareStatement(line);
                affectedRows += ps.executeUpdate();
                databaseConnection.closePreparedStatement(ps);
            }
            //Set up transactions
            for (String line : SETUP_TEST_TRANSACTIONS) {
                PreparedStatement ps = con.prepareStatement(line);
                affectedRows += ps.executeUpdate();
                databaseConnection.closePreparedStatement(ps);
            }
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
        databaseConnection.databaseUrl = "jdbc:mysql://localhost:3306/test";

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
        databaseConnection.databaseUrl = "jdbc:mysql://localhost:3306/test";

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
        databaseConnection.databaseUrl = "jdbc:mysql://localhost:3306/test";

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

    public void clearDB() {
        clearRelationshipTable();
        clearTransactionsTable();
        clearUsersTable();
    }

}
