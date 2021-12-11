package com.paymybuddy.dbConfig;

import com.paymybuddy.dao.UsersDAO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import com.paymybuddy.dao.UsersDAO;
import com.paymybuddy.dbConfig.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class DatabaseConnectionTest {

    private static DatabaseTestConnection databaseTestConfig = new DatabaseTestConnection();

    @Test
    public void databaseConnectionCanGetDatabaseVarsFromSystem() {
        //Prepare
        DatabaseConnection databaseConnection = new DatabaseConnection();

        //Method
        //databaseConnection.auth();

        //Verification
    }


}
