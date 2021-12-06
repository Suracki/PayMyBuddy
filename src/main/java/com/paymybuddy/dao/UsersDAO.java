package com.paymybuddy.dao;

import com.paymybuddy.constants.DBConstants;
import com.paymybuddy.dbConfig.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UsersDAO {

    private static final Logger logger = LogManager.getLogger("UsersDAO");
    public DatabaseConnection dataBaseConfig = new DatabaseConnection();

    public int getUserID(String email) {
        Connection con = null;

        int UserID = 0;
        try {
            con = dataBaseConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.GET_USER_ID_BY_EMAIL);
            ps.setString(1,email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                UserID = rs.getInt(1);
            }
            dataBaseConfig.closeResultSet(rs);
            dataBaseConfig.closePreparedStatement(ps);
        }
        catch (Exception e) {
            logger.error("Error obtaining User ID",e);
        }
        finally {
            return UserID;
        }
    }

}
