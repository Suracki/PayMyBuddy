package com.paymybuddy.unit.dao;

import com.paymybuddy.data.dao.UsersDAO;
import com.paymybuddy.data.dao.dbConfig.*;

import com.paymybuddy.data.dao.dbConfig.DatabaseTestConnection;
import com.paymybuddy.presentation.apimodels.UserDTO;
import com.paymybuddy.presentation.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UsersDAOTest {

    private static UsersDAO usersDAO;
    private static TestDAO testDAO;

    @BeforeAll
    private static void setUp() {
        usersDAO = new UsersDAO();
        usersDAO.databaseConnection = new DatabaseTestConnection();
        testDAO = new TestDAO();
        testDAO.clearDB();
    }

    @BeforeEach
    private void setUpDb() {
        testDAO.setUpTestDB();
    }

    @AfterEach
    private void cleanUp() {
        testDAO.clearDB();
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
    public void usersDAOCanAddNewUniqueUser() {
        //Prepare
        int userID = -1;
        User newUser = new User("firstnametest", "lastnametest", "addresstest", "citytest",
                "ziptest", "phonetest", "email@test", "password");

        //Method
        userID = usersDAO.addUniqueUser(newUser);

        //Verification
        System.out.println(userID);
        assertNotEquals(-1, userID);
    }

    @Test
    public void usersDAOReturnsErrorWhenAttemptingToAddDuplicateUniqueUser() {
        //Prepare
        int userID = -1;
        User newUser = new User("firstnametest", "lastnametest", "addresstest", "citytest",
                "ziptest", "phonetest", "email@test", "password");
        usersDAO.addUniqueUser(newUser);

        //Method
        userID = usersDAO.addUniqueUser(newUser);

        //Verification
        System.out.println(userID);
        assertEquals(-1, userID);
    }

    @Test
    public void usersDAOCanAddNewUserFromModel() {
        //Prepare
        int userID = -1;

        //Method
        User newUser = new User("firstnametest", "lastnametest", "addresstest", "citytest",
                "ziptest", "phonetest", "email@test", "password");
        userID = usersDAO.addUser(newUser);

        //Verification
        assertEquals(6, userID);
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

    @Test
    public void usersDAOCanUpdateExistingUserAndCheckPassword() {
        //Prepare
        User testUser = new User("firstnametest", "lastnametest", "addresstest", "citytest",
                "ziptest", "phonetest", "email@test", "password");
        User updatedUser = new User("firstnametestupdated", "lastnametestupdated", "addresstestupdated",
                "citytestupdated", "ziptestupdated", "phonetestupdated", "email@test", "password");
        int acctID = usersDAO.addUser(testUser);
        updatedUser.setAcctID(acctID);
        int affectedRows = -1;

        //Method
        affectedRows = usersDAO.updateUserAuthed(updatedUser);

        //Verification
        assertEquals(1, affectedRows);
    }

    @Test
    public void usersDAOFailsToUpdateUserWithInvalidPassword() {
        //Prepare
        User testUser = new User("firstnametest", "lastnametest", "addresstest", "citytest",
                "ziptest", "phonetest", "email@test", "password");
        User updatedUser = new User("firstnametestupdated", "lastnametestupdated", "addresstestupdated",
                "citytestupdated", "ziptestupdated", "phonetestupdated", "email@test", "wrongpassword");
        int acctID = usersDAO.addUser(testUser);
        updatedUser.setAcctID(acctID);
        int affectedRows = -1;

        //Method
        affectedRows = usersDAO.updateUserAuthed(updatedUser);

        //Verification
        assertEquals(0, affectedRows);
    }

    @Test
    public void usersDAOCanUpdatePasswordWhenOldPasswordIsValid(){
        //Prepare
        int AcctID = usersDAO.addUser("firstnametest", "lastnametest", "addresstest", "citytest",
                "ziptest", "phonetest", "email@test", "password");
        int affectedRows = -1;

        //Method
        affectedRows = usersDAO.updatePassword(AcctID, "password", "newpassword");

        //Verification
        assertEquals(1, affectedRows);

    }

    @Test
    public void usersDAOFailsToUpdatePasswordWhenOldPasswordIsInvalid(){
        //Prepare
        int AcctID = usersDAO.addUser("firstnametest", "lastnametest", "addresstest", "citytest",
                "ziptest", "phonetest", "email@test", "password");
        int affectedRows = -1;

        //Method
        affectedRows = usersDAO.updatePassword(AcctID, "wrongpassword", "newpassword");

        //Verification
        assertEquals(0, affectedRows);

    }

    @Test
    public void usersDAOCanUpdateExistingUserFromModel(){
        //Prepare
        int AcctID = usersDAO.addUser("firstnametest", "lastnametest", "addresstest", "citytest",
                "ziptest", "phonetest", "email@test", "password");
        User updatedUser = new User(AcctID, "firstnametest", "lastnametest", "addresstest", "citytest",
                "ziptest", "phonetest", "email@test", "password", new BigDecimal(0));
        int affectedRows = -1;

        //Method
        affectedRows = usersDAO.updateUser(updatedUser);

        //Verification
        assertEquals(1, affectedRows);
    }

    @Test
    public void usersDAOCanGetUserDetailsByAcctID() {
        //Prepare
        int UserID = addUser("first","last","test","test", "test", "test", "test@email.com", "password");
        int foundUserID = 0;

        //Method
        User foundUser = usersDAO.getUser(UserID);

        //Verification
        assertEquals("first", foundUser.getFirstName());
        assertEquals("test@email.com", foundUser.getEmail());
    }

    @Test
    public void usersDAOCanDeleteUserAcctFromModel() {
        //Prepare
        int AcctID = usersDAO.addUser("firstnametest", "lastnametest", "addresstest", "citytest",
                "ziptest", "phonetest", "email@test", "password");
        User deleteUser = new User(AcctID, "firstnametest", "lastnametest", "addresstest", "citytest",
                "ziptest", "phonetest", "email@test", "password", new BigDecimal(0));
        int affectedRows = -1;

        //Method
        affectedRows = usersDAO.deleteUser(deleteUser);

        //Verification
        assertEquals(1, affectedRows);
    }

    @Test
    public void usersDAOFailsToDeleteUserIfPasswordInvalid() {
        //Prepare
        int AcctID = usersDAO.addUser("firstnametest", "lastnametest", "addresstest", "citytest",
                "ziptest", "phonetest", "email@test", "password");
        User deleteUser = new User(AcctID, "firstnametest", "lastnametest", "addresstest", "citytest",
                "ziptest", "phonetest", "email@test", "notpassword", new BigDecimal(0));
        int affectedRows = -1;

        //Method
        affectedRows = usersDAO.deleteUser(deleteUser);

        //Verification
        assertEquals(0, affectedRows);
    }

    private int addUser(String firstName, String lastName, String address, String city, String zip, String phone,
                        String email, String password) {
        return usersDAO.addUser(firstName, lastName, address, city, zip, phone, email, password);
    }

}
