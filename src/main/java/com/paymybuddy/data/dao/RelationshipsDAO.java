package com.paymybuddy.data.dao;

import com.nimbusds.jose.shaded.json.JSONArray;
import com.nimbusds.jose.shaded.json.JSONObject;
import com.paymybuddy.data.dao.constants.DBConstants;
import com.paymybuddy.data.dao.dbConfig.DatabaseConnection;
import com.paymybuddy.presentation.model.Relationship;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;

/**
 * RelationshipsDAO contains all methods for interacting directly with the UserRelationships table of the database
 */
@Service
public class RelationshipsDAO {

    private static final Logger logger = LogManager.getLogger("UsersDAO");
    @Autowired
    public DatabaseConnection databaseConnection;

    /**
     * Method to get ListID of a relationship between two specific users
     *
     * @param ownerID AcctID for User who's list this is
     * @param friendID AcctID for User recorded as a friend
     * @return ListID integer value
     */
    public int getListID(int ownerID, int friendID) {
        logger.info("Attempting to get ListID for relationship between users " + ownerID + " and " + friendID);
        Connection con = null;

        int ListID = -1;
        try {
            con = databaseConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(DBConstants.GET_RELATIONSHIP_ID);
            ps.setInt(1,ownerID);
            ps.setInt(2,friendID);

            logger.debug("PreparedStatement created: " + ps.toString());

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                ListID = rs.getInt(1);
            }
            databaseConnection.closeResultSet(rs);
            databaseConnection.closePreparedStatement(ps);
            logger.info("ListID obtained from database successfully");
        }
        catch (Exception e) {
            logger.error("Error obtaining List ID",e);
        }
        finally {
            databaseConnection.closeConnection(con);
            return ListID;
        }
    }

    /**
     * Method to add a relationship between two specific users
     * This method requires an AcctID for the friend to be added
     *
     * @param relationship Relationship object containing details to be added
     * @return automatically generated ListID integer value from database
     */
    public int addRelationship(Relationship relationship) {
        logger.info("Attempting to add Relationship to database");
        Connection con = null;

        int ListID = -1;
        try {
            con = databaseConnection.getConnection();

            con.setAutoCommit(false);

            PreparedStatement ps = con.prepareStatement(DBConstants.ADD_UNIQUE_RELATIONSHIP_WITH_ACTIVE, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, relationship.getListOwnerID());
            ps.setInt(2, relationship.getFriendID());
            ps.setInt(3, relationship.getListOwnerID());
            ps.setInt(4, relationship.getFriendID());
            ps.setInt(5, relationship.getListOwnerID());
            logger.debug("PreparedStatement created: " + ps.toString());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                ListID = rs.getInt(1);
            }
            databaseConnection.closeResultSet(rs);
            databaseConnection.closePreparedStatement(ps);

            con.commit();
            logger.info("Relationship (ID " + ListID + ") added to database successfully");
        }
        catch (Exception e) {
            con.rollback();
            logger.error("Error adding user relationship",e);
        }
        finally {
            databaseConnection.closeConnection(con);
            return ListID;
        }
    }

    /**
     * Method to add a relationship between two specific users
     * This method requires an Email address for the friend to be added
     *
     * @param relationship Relationship object containing details to be added
     * @return automatically generated ListID integer value from database
     */
    public int addRelationshipByEmail(Relationship relationship) {
        logger.info("Attempting to add Relationship to database");
        Connection con = null;

        int ListID = -1;
        try {
            con = databaseConnection.getConnection();

            con.setAutoCommit(false);

            PreparedStatement ps = con.prepareStatement(DBConstants.ADD_UNIQUE_RELATIONSHIP_BY_EMAIL_WITH_ACTIVE, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, relationship.getListOwnerID());
            ps.setString(2, relationship.getFriendEmail());
            ps.setInt(3, relationship.getListOwnerID());
            ps.setString(4, relationship.getFriendEmail());
            ps.setInt(5, relationship.getListOwnerID());
            logger.debug("PreparedStatement created: " + ps.toString());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                ListID = rs.getInt(1);
            }
            databaseConnection.closeResultSet(rs);
            databaseConnection.closePreparedStatement(ps);

            con.commit();
            logger.info("Relationship (ID " + ListID + ") added to database successfully");
        }
        catch (Exception e) {
            con.rollback();
            logger.error("Error adding user relationship",e);
        }
        finally {
            databaseConnection.closeConnection(con);
            return ListID;
        }
    }

    /**
     * Method to remove a relationship between two specific users from the database
     * This method requires an Email address for the friend to be added
     *
     * @param relationship Relationship object containing details to be added
     * @return integer number of relationships removed. Can be 0 if no matching relationship found.
     */
    public int deleteRelationship(Relationship relationship){
        logger.info("Attempting to remove Relationship from database");
        Connection con = null;

        int affectedRows = -1;
        try {
            con = databaseConnection.getConnection();

            con.setAutoCommit(false);

            PreparedStatement ps = con.prepareStatement(DBConstants.DELETE_RELATIONSHIP);
            ps.setInt(1, relationship.getListOwnerID());
            ps.setInt(2, relationship.getFriendID());
            logger.debug("PreparedStatement created: " + ps.toString());
            affectedRows = ps.executeUpdate();
            databaseConnection.closePreparedStatement(ps);

            con.commit();
            logger.info(affectedRows + "Relationship(s) removed from database successfully");
        }
        catch (Exception e) {
            con.rollback();
            logger.error("Error deleting user relationship",e);
        }
        finally {
            return affectedRows;
        }
    }

    /**
     * Method to get all relationships for a specific user
     *
     * @param relationship Relationship object containing the AcctID of the user in question
     * @return JSONArray of found relationships for this user. Can be empty if user has no relationships or is not found.
     */
    public JSONArray getRelationships(Relationship relationship) {
        logger.info("Attempting to get details of all Relationships for a single user");
        Connection con = null;

        JSONArray json = new JSONArray();
        try {
            con = databaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.GET_RELATIONSHIP_DETAILS);
            ps.setInt(1,relationship.getListOwnerID());
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
            logger.info("Relationship details retrieved from database successfully");
        }
        catch (Exception e) {
            logger.error("Error obtaining relationship details",e);
        }
        finally {
            databaseConnection.closeConnection(con);
            return json;
        }
    }
}