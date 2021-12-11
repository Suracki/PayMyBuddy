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
    public void verifyUserReturnsUserIDForSuccesfulLogin() {
        //Prepare
        UsersDAO usersDAO = new UsersDAO();
        int validLogin;

        //Method
        validLogin = usersDAO.verifyUser("tenz@email.com", "password");

        //Verification
        assertEquals(3, validLogin);
    }

    @Test
    public void verifyUserReturnsMinusOneForUnsuccesfulLogin() {
        //Prepare
        UsersDAO usersDAO = new UsersDAO();
        int invalidLogin;

        //Method
        invalidLogin = usersDAO.verifyUser("tenz@email.com", "notpassword");

        //Verification
        assertEquals(-1, invalidLogin);
    }

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
        assertEquals(3, userID);


    }

    @Test
    public void usersDAOCanAddNewUser() {
        //Prepare
        UsersDAO usersDAO = new UsersDAO();
        int userID = -1;

        //Method
        userID = usersDAO.addUser("firstnametest", "lastnametest", "addresstest", "citytest",
                "ziptest", "phonetest", "email@test", "password");

        //Verification
        System.out.println(userID);
        assertNotEquals(-1, userID);

    }

    @Test
    public void usersDAOCanUpdateExistingUser() {
        //Prepare
        UsersDAO usersDAO = new UsersDAO();
        int affectedRows = -1;

        //Method
        affectedRows = usersDAO.updateUser("firstnametest", "lastnametest", "addresstest", "citytest",
                "ziptest", "phonetest", "email@test", "password", 2);

        //Verification
        System.out.println(affectedRows);
        assertEquals(1, affectedRows);
    }
}
