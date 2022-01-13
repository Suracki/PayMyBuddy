package com.paymybuddy.logic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.paymybuddy.data.dao.UsersDAO;
import com.paymybuddy.presentation.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsersService {

    private UsersDAO usersDAO;
    private PasswordEncoder passwordEncoder;
    private static final Logger logger = LogManager.getLogger("UsersService");

    @Autowired
    public UsersService(UsersDAO usersDAO, PasswordEncoder passwordEncoder) {
        this.usersDAO = usersDAO;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<String> createUser(User newUser) {
        logger.info("Processing createUser User request to create a new User entry");

        //Encrypt password
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        //Add user to database
        newUser.setAcctID(usersDAO.addUniqueUser(newUser));

        if (newUser.getAcctID() == -1) {
            //Failed to add, duplicate email
            ResponseEntity<String> response = ResponseEntity.status(HttpStatus.CONFLICT).build();
            logger.error("Failed to add user. User may already exist with this email address",response);
            return response;
        }

        //Clear password before responding
        newUser.setPassword("*********");

        //Build response
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();
        String responseString = gson.toJson(newUser);
        HttpHeaders responseHeaders = new HttpHeaders();

        ResponseEntity<String> response = new ResponseEntity<>(responseString, responseHeaders, HttpStatus.CREATED);
        logger.info("User created", response);
        return response;
    }

    public ResponseEntity<String> getUser(int acctID) {
        logger.info("Processing getUser User request to get User details by AcctID");

        //Get user from database
        User loadUser = usersDAO.getUser(acctID);

        if (loadUser == null) {
            //Failed to load, user not found
            ResponseEntity<String> response = ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            logger.error("Failed to load user. User may not exist with this account ID",response);
            return response;
        }

        //Clear password before responding
        loadUser.setPassword("*********");

        //Build response
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();
        String responseString = gson.toJson(loadUser);
        HttpHeaders responseHeaders = new HttpHeaders();

        ResponseEntity<String> response = new ResponseEntity<>(responseString, responseHeaders, HttpStatus.OK);
        logger.info("User details retrieved", response);
        return response;
    }

    public ResponseEntity<String> updateUser(User user) {
        logger.info("Processing updateUser User request to updated User details");

        int affectedRows = usersDAO.updateUserAuthed(user);

        if (affectedRows == 0) {
            //Failed to update, user doesn't exist or password incorrect
            ResponseEntity<String> response = new ResponseEntity<>("Update failed. User does not exist, or password incorrect", new HttpHeaders(), HttpStatus.NOT_FOUND);;
            logger.error("Failed to update user",response);
            return response;
        }

        //Clear password before responding
        user.setPassword("*********");

        //Create response
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();
        String responseString = gson.toJson(user);
        HttpHeaders responseHeaders = new HttpHeaders();
        ResponseEntity<String> response = new ResponseEntity<>(responseString, responseHeaders, HttpStatus.OK);

        logger.info("User "+ user.getAcctID() + " updated", response);
        return response;

    }

    public ResponseEntity<String> deleteUser(User deleteUser) {
        logger.info("Processing deleteUser User request to delete User from database");

        //Update user in database
        int updatedRows = usersDAO.deleteUser(deleteUser);

        if (updatedRows == 0) {
            //Failed to delete, user doesn't exist or password incorrect
            ResponseEntity<String> response = new ResponseEntity<>("Delete failed. User does not exist, or password incorrect", new HttpHeaders(), HttpStatus.NOT_FOUND);
            logger.error("Delete failed. User does not exist, or password incorrect",response);
            return response;
        }

        //respond
        ResponseEntity<String> response = new ResponseEntity<>(updatedRows + " user(s) deleted", new HttpHeaders(), HttpStatus.OK);
        logger.info("User deleted", response);
        return response;

    }

    public ResponseEntity<String> changePassword(User user, String password) {
        logger.info("Processing changePassword User request to update User password in database");
        int affectedRows = usersDAO.updatePassword(user, passwordEncoder.encode(password));

        if (affectedRows == -1) {
            //Failed to update, user doesn't exist or original password incorrect
            ResponseEntity<String> response = new ResponseEntity<>("Update failed. User does not exist, or original password incorrect", new HttpHeaders(), HttpStatus.NOT_FOUND);;
            logger.error("Unable to update password",response);
            return response;
        }

        //Create response
        ResponseEntity<String> response = new ResponseEntity<>("Password updated", new HttpHeaders(), HttpStatus.OK);

        logger.info("User password updated", response);
        return response;
    }

    public ResponseEntity<String> authUser(User user) {
        logger.info("Processing authUser User request to verify provided password and authenticate login");
        //Get stored password hash from database
        String[] dbResult = usersDAO.getPasswordHash(user.getEmail());
        String pwHash = dbResult[1];
        String providedPw = (user.getPassword());

        if (!passwordEncoder.matches(providedPw, pwHash)) {
            //Failed to authenticate, user doesn't exist or password incorrect
            ResponseEntity<String> response = new ResponseEntity<>("Auth failed. User does not exist, or password incorrect", new HttpHeaders(), HttpStatus.NOT_FOUND);;
            logger.error("Auth failed. User does not exist, or password incorrect",response);
            return response;
        }

        //respond
        ResponseEntity<String> response = new ResponseEntity<>("User " + dbResult[0] + " authenticated.", new HttpHeaders(), HttpStatus.OK);
        logger.info("User authenticated", response);
        return response;
    }


}
