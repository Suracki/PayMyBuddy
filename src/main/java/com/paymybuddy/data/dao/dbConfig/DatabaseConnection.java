package com.paymybuddy.data.dao.dbConfig;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.*;

@Service
public class DatabaseConnection {
    private static final Logger logger = LogManager.getLogger("DatabaseConnection");

    @Value("${sql.user.varname}")
    private String uservar;

    @Value("${sql.pw.varname}")
    private String userpass;

    @Value("${sql.url}")
    private String databaseUrl;

    public String getUser() {
        return "bdeb5b3c2a5cb9";
    }

    public String getPassword() {
        return "9bef29e3e0bc9c1";
    }

    public String getDatabaseUrl() {
        return System.getenv("DATABASE_URL");
    }




    public Connection getConnection() throws ClassNotFoundException, SQLException {
        logger.info("Create DB connection");
        logger.info("DB URL: " + getDatabaseUrl());
        logger.info("CLEARDB URL: " + System.getenv("CLEARDB_DATABASE_URL"));
        logger.info("JDBC URL: " + System.getenv("JDBC_DATABASE_URL"));
        Class.forName("com.mysql.cj.jdbc.Driver");
        //return DriverManager.getConnection(
        //        getDatabaseUrl(),getUser(),getPassword());
        String dbUrl = System.getenv("JDBC_DATABASE_URL");
        return DriverManager.getConnection(dbUrl);
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
