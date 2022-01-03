package com.paymybuddy.unit.logic;

import com.paymybuddy.data.dao.UsersDAO;
import com.paymybuddy.logic.UsersService;
import com.paymybuddy.presentation.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;

import static com.paymybuddy.unit.logic.TestServiceConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class UsersServiceTest {

    @Mock
    UsersDAO usersDAO;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UsersService usersService;

    @Test
    public void userServiceCanAddNewUserAndReturnsJsonResponse() {
        //Prepare
        User testUser = new User("First", "Last", "address", "city", "zip",
                "phone", "email", "password", new BigDecimal(0));
        ResponseEntity<String> response;
        doReturn(1).when(usersDAO).addUniqueUser(notNull());

        //Perform
        response = usersService.createUser(testUser);

        //Verify
        assertEquals(USERSSERVICE_CREATED_RESPONSE, response.toString());
    }

    @Test
    public void userServiceWillNotAddDuplicateUsers() {
        //Prepare
        User testUser = new User();
        testUser.setEmail("Test");
        ResponseEntity<String> response;
        doReturn(-1).when(usersDAO).addUniqueUser(notNull());

        //Perform
        response = usersService.createUser(testUser);

        //Verify
        assertEquals(USERSERVICE_CREATED_FAILRESPONSE, response.toString());
    }

    @Test
    public void userServiceCanUpdateExistingUser() {
        //Prepare
        User testUser = new User(3, "First", "Last", "address", "city", "zip",
                "phone", "email", "password", new BigDecimal(0));
        ResponseEntity<String> response;
        doReturn(1).when(usersDAO).updateUserAuthed(notNull());

        //Perform
        response = usersService.updateUser(testUser);

        //Verify
        assertEquals(USERSERVICE_UPDATED_RESPONSE, response.toString());
    }

    @Test
    public void userServiceReturnsErrorAttemptingToUpdateNonExistingUser() {
        //Prepare
        User testUser = new User(3, "First", "Last", "address", "city", "zip",
                "phone", "email", "password", new BigDecimal(0));
        ResponseEntity<String> response;
        doReturn(0).when(usersDAO).updateUserAuthed(notNull());

        //Perform
        response = usersService.updateUser(testUser);

        //Verify
        assertEquals(USERSERVICE_UPDATED_FAILRESPONSE, response.toString());
    }

    @Test
    public void userServiceCanRetrieveUserAndReturnsJsonResponse() {
        //Prepare
        User testUser = new User(1,"First", "Last", "address", "city", "zip",
                "phone", "email", "password", new BigDecimal(0));
        ResponseEntity<String> response;
        doReturn(testUser).when(usersDAO).getUser(1);

        //Perform
        response = usersService.getUser(1);

        //Verify
        assertEquals(USERSERVICE_GET_RESPONSE, response.toString());
    }

    @Test
    public void userServiceCanChangeUserPassword() {
        //Prepare
        User testUser = new User("First", "Last", "address", "city", "zip",
                "phone", "email", "password", new BigDecimal(0));
        ResponseEntity<String> response;
        String hashedPw = "password";
        doReturn(hashedPw).when(passwordEncoder).encode("newpassword");
        doReturn(1).when(usersDAO).updatePassword(testUser,hashedPw);

        //Perform
        response = usersService.changePassword(testUser,"newpassword");

        //Verify
        assertEquals(USERSERVICE_CHANGEPASS_RESPONSE, response.toString());
    }

    @Test
    public void userServiceReturnsErrorAttemptingToChangePasswordWithIncorrectPassword() {
        //Prepare
        User testUser = new User("First", "Last", "address", "city", "zip",
                "phone", "email", "wrongpassword", new BigDecimal(0));
        ResponseEntity<String> response;
        String hashedPw = "password";
        doReturn(hashedPw).when(passwordEncoder).encode("newpassword");
        doReturn(-1).when(usersDAO).updatePassword(testUser,hashedPw);

        //Perform
        response = usersService.changePassword(testUser,"newpassword");

        //Verify
        assertEquals(USERSERVICE_CHANGEPASS_FAILRESPONSE, response.toString());
    }

    @Test
    public void userServiceCanDeleteExistingUser() {
        //Prepare
        User testUser = new User("First", "Last", "address", "city", "zip",
                "phone", "email", "password", new BigDecimal(0));
        ResponseEntity<String> response;
        doReturn(1).when(usersDAO).deleteUser(notNull());

        //Perform
        response = usersService.deleteUser(testUser);

        //Verify
        assertEquals(USERSERVICE_DELETED_RESPONSE, response.toString());
    }

    @Test
    public void userServiceReturnsErrorAttemptingToDeleteNonExistingUser() {
        //Prepare
        User testUser = new User("First", "Last", "address", "city", "zip",
                "phone", "email", "password", new BigDecimal(0));
        ResponseEntity<String> response;
        doReturn(0).when(usersDAO).deleteUser(notNull());

        //Perform
        response = usersService.deleteUser(testUser);

        //Verify
        assertEquals(USERSERVICE_DELETED_FAILRESPONSE, response.toString());
    }

}
