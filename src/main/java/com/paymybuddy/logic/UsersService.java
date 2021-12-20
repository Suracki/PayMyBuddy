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
import org.springframework.stereotype.Service;

@Service
public class UsersService {

    private UsersDAO usersDAO;
    private static final Logger logger = LogManager.getLogger("UsersService");

    @Autowired
    public UsersService(UsersDAO usersDAO) {
        this.usersDAO = usersDAO;
    }

    public ResponseEntity<String> createUser(User newUser) {

        if (usersDAO.getUserID(newUser.getEmail()) != 0) {
            //User already exists with this email address
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        //Add user to database
        newUser.setAcctID(usersDAO.addUser(newUser));

        //Build response
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();
        String responseString = gson.toJson(newUser);
        HttpHeaders responseHeaders = new HttpHeaders();

        return new ResponseEntity<>(responseString, responseHeaders, HttpStatus.CREATED);
    }

    public ResponseEntity<String> updateUser(User updatedUser) {

        if (usersDAO.getUserID(updatedUser.getEmail()) == 0) {
            //User does not exist with this email address
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        //Update user in database
        int updatedRows = usersDAO.updateUser(updatedUser);

        //Build response
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();
        String responseString = gson.toJson(updatedRows);
        HttpHeaders responseHeaders = new HttpHeaders();

        return new ResponseEntity<>(responseString, responseHeaders, HttpStatus.OK);

    }

    public ResponseEntity<String> deleteUser(User deleteUser) {

        if (usersDAO.getUserID(deleteUser.getEmail()) == 0) {
            //User does not exist with this email address
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        //Update user in database
        int updatedRows = usersDAO.deleteUser(deleteUser);

        //Build response
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();
        String responseString = gson.toJson(updatedRows);
        HttpHeaders responseHeaders = new HttpHeaders();

        return new ResponseEntity<>(responseString, responseHeaders, HttpStatus.OK);

    }


}
