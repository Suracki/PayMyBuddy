package com.paymybuddy.unit.dao;

import com.paymybuddy.dao.UsersDAO;
import com.paymybuddy.dbConfig.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UsersDAOTest {

    private static UsersDAO usersDAO;

    @BeforeAll
    private static void setUp() {
        usersDAO = new UsersDAO();
        usersDAO.databaseConnection.databaseUrl = "jdbc:mysql://localhost:3306/test";
    }

    @AfterEach
    private void resetDatabase() {
        resetTable();
    }

    @Test
    public void verifyUserReturnsUserIDForSuccesfulLogin() {
        //Prepare
        int userID = addUser("test","test","test","test", "test", "test", "test@email.com", "password");
        int validLogin;

        //Method
        validLogin = usersDAO.verifyUser("test@email.com", "password");

        //Verification
        assertEquals(userID, validLogin);
    }

    @Test
    public void verifyUserReturnsMinusOneForUnsuccesfulLogin() {
        //Prepare
        int userID = addUser("test","test","test","test", "test", "test", "test@email.com", "password");
        int invalidLogin;

        //Method
        invalidLogin = usersDAO.verifyUser("test@email.com", "notpassword");

        //Verification
        assertEquals(-1, invalidLogin);
    }

    @Test
    public void usersDAOCanGetUserIDByEmail() {
        //Prepare
        int expectedUserID = addUser("test","test","test","test", "test", "test", "test@email.com", "password");
        int foundUserID = 0;

        //Method
        foundUserID = usersDAO.getUserID("test@email.com");

        //Verification
        assertEquals(expectedUserID, foundUserID);


    }

    @Test
    public void usersDAOCanAddNewUser() {
        //Prepare
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
        int AcctID = usersDAO.addUser("firstnametest", "lastnametest", "addresstest", "citytest",
                "ziptest", "phonetest", "email@test", "password");
        int affectedRows = -1;

        //Method
        affectedRows = usersDAO.updateUser("newfirstnametest", "newlastnametest", "newaddresstest", "citytest",
                "ziptest", "phonetest", "email@test", "password", AcctID);

        //Verification
        assertEquals(1, affectedRows);
    }

    private int addUser(String firstName, String lastName, String address, String city, String zip, String phone,
                        String email, String password) {
        return usersDAO.addUser(firstName, lastName, address, city, zip, phone, email, password);
    }

    private void resetTable() {
        TestDAO testDAO = new TestDAO();
        testDAO.clearUsersTable();
    }
}
