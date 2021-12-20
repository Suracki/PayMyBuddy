package com.paymybuddy.data.dao.dbConfig;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.sql.*;

@Service
public class DatabaseTestConnection extends DatabaseConnection {
    private static final Logger logger = LogManager.getLogger("DatabaseConnection");

    public String uservar = "SQLUser";

    public String userpass = "SQLPass";

    public String databaseUrl = "jdbc:mysql://localhost:3306/test";


    public String getUser() {
        return System.getenv(uservar);
    }

    public String getPassword() {
        return System.getenv(userpass);
    }




    public Connection getConnection() throws ClassNotFoundException, SQLException {
        logger.info("Create DB connection");
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(
                databaseUrl,getUser(),getPassword());
    }

    public void closeConnection(Connection con){
        if(con!=null){
            try {
                con.close();
                logger.info("Closing DB connection");
            } catch (SQLException e) {
                logger.error("Error while closing connection",e);
            }
        }
    }

    public void closePreparedStatement(PreparedStatement ps) {
        if(ps!=null){
            try {
                ps.close();
                logger.info("Closing Prepared Statement");
            } catch (SQLException e) {
                logger.error("Error while closing prepared statement",e);
            }
        }
    }

    public void closeResultSet(ResultSet rs) {
        if(rs!=null){
            try {
                rs.close();
                logger.info("Closing Result Set");
            } catch (SQLException e) {
                logger.error("Error while closing result set",e);
            }
        }
    }

}
