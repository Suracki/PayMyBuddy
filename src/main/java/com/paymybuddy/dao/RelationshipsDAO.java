package com.paymybuddy.dao;

import com.paymybuddy.constants.DBConstants;
import com.paymybuddy.dbConfig.DatabaseConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

@Service
public class RelationshipsDAO {

    private static final Logger logger = LogManager.getLogger("UsersDAO");
    public DatabaseConnection databaseConnection = new DatabaseConnection();

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
            return ListID;
        }
    }

    public int addRelationship(int ownerID, int friendID) {
        Connection con = null;

        int ListID = -1;
        try {
            con = databaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.ADD_RELATIONSHIP, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, ownerID+"");
            ps.setString(2, friendID+"");
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
            return ListID;
        }
    }

    public int deleteRelationship(int listID){
        Connection con = null;

        int affectedRows = -1;
        try {
            con = databaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.DELETE_RELATIONSHIP);
            ps.setString(1, listID+"");
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

    public ArrayList<Integer> getList(int ownerID) {
        Connection con = null;

        ArrayList<Integer> list = new ArrayList<>();
        try {
            con = databaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.GET_LIST);
            ps.setString(1,ownerID+"");
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
            return list;
        }
    }
}