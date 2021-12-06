package com.paymybuddy.unit.dao;

import com.paymybuddy.dao.UsersDAO;
import com.paymybuddy.dbConfig.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UsersDAOTest {

    private static DatabaseTestConnection databaseTestConfig = new DatabaseTestConnection();

    @Test
    public void usersDAOCanGetUserIDByEmail() {
        //Prepare
        UsersDAO usersDAO = new UsersDAO();
        //usersDAO.dataBaseConfig = databaseTestConfig;
        int userID = 0;

        //Method
        userID = usersDAO.getUserID("tenz@email.com");

        //Verification
        System.out.println(userID);
        assertEquals(4, userID);


    }

}
