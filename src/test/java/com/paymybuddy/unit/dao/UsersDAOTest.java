package com.paymybuddy.unit.dao;

import com.paymybuddy.data.dao.UsersDAO;
import com.paymybuddy.data.dao.dbConfig.*;

import com.paymybuddy.data.dao.dbConfig.DatabaseTestConnection;
import com.paymybuddy.exceptions.FailToAddUserFundsException;
import com.paymybuddy.exceptions.FailToSubtractUserFundsException;
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
    public void usersDAOCanGetUserIDByEmail() {
        //Prepare
        int expectedUserID = addUser("test","test","test","test", "test", "test", "test@email.com", "password", "FR1420041010050500013M02606","BNPAGFGX");
        int foundUserID = 0;

        //Method
        foundUserID = usersDAO.getUserID("test@email.com");

        //Verification
        assertEquals(expectedUserID, foundUserID);


    }

    @Test
    public void usersDAOCanAddNewUniqueUser() {
        //Prepare
        int userID = -1;
        User newUser = new User("firstnametest", "lastnametest", "addresstest", "citytest",
                "ziptest", "phonetest", "email@test", "password", "FR1420041010050500013M02606","BNPAGFGX");

        //Method
        userID = usersDAO.addUniqueUser(newUser);

        //Verification
        assertNotEquals(-1, userID);
    }

    @Test
    public void usersDAOReturnsErrorWhenAttemptingToAddDuplicateUniqueUser() {
        //Prepare
        int userID = -1;
        User newUser = new User("firstnametest", "lastnametest", "addresstest", "citytest",
                "ziptest", "phonetest", "email@test", "password", "FR1420041010050500013M02606","BNPAGFGX");
        usersDAO.addUniqueUser(newUser);

        //Method
        userID = usersDAO.addUniqueUser(newUser);

        //Verification
        assertEquals(-1, userID);
    }

    @Test
    public void usersDAOCanUpdateExistingUser() {
        //Prepare
        User testUser = new User("firstnametest", "lastnametest", "addresstest", "citytest",
                "ziptest", "phonetest", "email@test", "password", "FR1420041010050500013M02606","BNPAGFGX");
        User updatedUser = new User("firstnametestupdated", "lastnametestupdated", "addresstestupdated",
                "citytestupdated", "ziptestupdated", "phonetestupdated", "email@test", "password", "FR1420041010050500013M02606","BNPAGFGX");
        int acctID = usersDAO.addUniqueUser(testUser);
        updatedUser.setAcctID(acctID);
        int affectedRows = -1;

        //Method
        affectedRows = usersDAO.updateUser(updatedUser);

        //Verification
        assertEquals(1, affectedRows);
    }

    @Test
    public void usersDAOCanUpdatePassword(){
        //Prepare
        User testUser = new User("firstnametest", "lastnametest", "addresstest", "citytest",
                "ziptest", "phonetest", "email@test", "password", "FR1420041010050500013M02606","BNPAGFGX");
        testUser.setAcctID(usersDAO.addUniqueUser(testUser));
        int affectedRows = -1;

        //Method
        affectedRows = usersDAO.updatePassword(testUser, "newpassword");

        //Verification
        assertEquals(1, affectedRows);

    }

    @Test
    public void usersDAOCanGetUserDetailsByAcctID() throws Exception {
        //Prepare
        int UserID = addUser("first","last","test","test", "test", "test", "test@email.com", "password", "FR1420041010050500013M02606","BNPAGFGX");
        int foundUserID = 0;

        //Method
        User foundUser = usersDAO.getUser(UserID);

        //Verification
        assertEquals("first", foundUser.getFirstName());
        assertEquals("test@email.com", foundUser.getEmail());
    }

    @Test
    public void usersDAOCanDeleteUserAcctFromDatabase() {
        //Prepare
        int AcctID = addUser("firstnametest", "lastnametest", "addresstest", "citytest",
                "ziptest", "phonetest", "email@test", "password", "FR1420041010050500013M02606","BNPAGFGX");
        User deleteUser = new User(AcctID, "firstnametest", "lastnametest", "addresstest", "citytest",
                "ziptest", "phonetest", "email@test", "password", new BigDecimal(0));
        int affectedRows = -1;

        //Method
        affectedRows = usersDAO.deleteUser(deleteUser);

        //Verification
        assertEquals(1, affectedRows);
    }

    @Test
    public void usersDAOCanGetAcctIDAndPasswordHashByEmail() {
        //Prepare
        int expectedUserID = addUser("test","test","test","test", "test", "test", "test@email.com", "password", "FR1420041010050500013M02606","BNPAGFGX");
        String foundPwHash[];

        //Method
        foundPwHash = usersDAO.getPasswordHash("test@email.com");
        int foundUserID = Integer.valueOf(foundPwHash[0]);

        //Verification
        assertEquals("password", foundPwHash[1]);
        assertEquals(expectedUserID, foundUserID);


    }

    @Test
    public void usersDAOCanAddFundsToAUser() throws Exception {
        //Prepare
        int affectedRows = -1;

        //Method
        affectedRows = usersDAO.addFunds(1, new BigDecimal("50"));

        //Verification
        assertEquals(1, affectedRows);
    }

    @Test
    public void usersDAOCanSubtractFundsFromAUser() throws Exception{
        //Prepare
        int affectedRows = -1;

        //Method
        affectedRows = usersDAO.subtractFunds(2, new BigDecimal("5"));

        //Verification
        assertEquals(1, affectedRows);
    }

    @Test
    public void usersDAOCanSubtractAllFundsFromAUser() throws Exception{
        //Prepare
        int affectedRows = -1;

        //Method
        affectedRows = usersDAO.subtractFunds(2, new BigDecimal("10"));

        //Verification
        assertEquals(1, affectedRows);
    }

    @Test
    public void usersDAOCannotSubtractUnavailableFundsFromAUser() {
        //Prepare

        //Method
        FailToSubtractUserFundsException exception = assertThrows(FailToSubtractUserFundsException.class, () -> {
            usersDAO.subtractFunds(2, new BigDecimal("11"));
        });

        //Verification
        assertEquals(exception.getAcctID(), 2);
    }

    private int addUser(String firstName, String lastName, String address, String city, String zip, String phone,
                        String email, String password, String IBAN, String BIC) {
        return usersDAO.addUniqueUser(new User(firstName, lastName, address, city, zip, phone, email, password, IBAN, BIC));
    }

}
