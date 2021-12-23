package com.paymybuddy.data.dao;

import com.paymybuddy.data.dao.constants.DBConstants;
import com.paymybuddy.data.dao.dbConfig.DatabaseConnection;
import com.paymybuddy.presentation.model.Relationship;
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
public class RelationshipsDAO {

    private static final Logger logger = LogManager.getLogger("UsersDAO");
    @Autowired
    public DatabaseConnection databaseConnection;

    public int getListID(int ownerID, int friendID) {
        Connection con = null;

        int ListID = -1;
        try {
            con = databaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.GET_RELATIONSHIP_ID);
            ps.setString(1,ownerID+"");
            ps.setString(2,friendID+"");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ListID = rs.getInt(1);
            }
            databaseConnection.closeResultSet(rs);
            databaseConnection.closePreparedStatement(ps);
        }
        catch (Exception e) {
            logger.error("Error obtaining List ID",e);
        }
        finally {
            databaseConnection.closeConnection(con);
            return ListID;
        }
    }

    public int addRelationship(Relationship relationship, String password) {
        Connection con = null;

        int ListID = -1;
        try {
            con = databaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.ADD_UNIQUE_RELATIONSHIP_WITH_AUTH, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, relationship.getListOwnerID());
            ps.setInt(2, relationship.getFriendID());
            ps.setInt(3, relationship.getListOwnerID());
            ps.setInt(4, relationship.getFriendID());
            ps.setInt(5, relationship.getListOwnerID());
            ps.setString(6, password);
            System.out.println(ps.toString());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                ListID = rs.getInt(1);
            }
            databaseConnection.closeResultSet(rs);
            databaseConnection.closePreparedStatement(ps);
        }
        catch (Exception e) {
            logger.error("Error adding user relationship",e);
        }
        finally {
            databaseConnection.closeConnection(con);
            return ListID;
        }
    }

    public int deleteRelationship(Relationship relationship, String password){
        Connection con = null;

        int affectedRows = -1;
        try {
            con = databaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.DELETE_RELATIONSHIP_SECURE);
            ps.setInt(1, relationship.getListOwnerID());
            ps.setString(2, password);
            ps.setInt(3, relationship.getFriendID());
            affectedRows = ps.executeUpdate();
            databaseConnection.closePreparedStatement(ps);
        }
        catch (Exception e) {
            logger.error("Error deleting user relationship",e);
        }
        finally {
            return affectedRows;
        }
    }

    public ArrayList<Integer> getList(Relationship relationship, String password) {
        Connection con = null;

        ArrayList<Integer> list = new ArrayList<>();
        try {
            con = databaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.GET_LIST);
            ps.setInt(1,relationship.getListOwnerID());
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            int columnCount = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                //list.add(rs.getInt(1));
                for (int i = 1; i <= columnCount; i++) {
                    list.add(Integer.parseInt(rs.getString(i)));
                    System.out.println(rs.getString(i));
                }
            }
            databaseConnection.closeResultSet(rs);
            databaseConnection.closePreparedStatement(ps);
        }
        catch (Exception e) {
            logger.error("Error obtaining List",e);
        }
        finally {
            databaseConnection.closeConnection(con);
            return list;
        }
    }
}