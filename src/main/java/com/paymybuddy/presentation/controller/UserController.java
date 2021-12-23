package com.paymybuddy.presentation.controller;

import com.paymybuddy.logic.UsersService;
import com.paymybuddy.presentation.apimodels.UserDTO;
import com.paymybuddy.presentation.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class UserController {

    private static final Logger logger = LogManager.getLogger("UserController");

    private UsersService userService;

    @Autowired
    public UserController(UsersService userService){
        this.userService = userService;
    }

    @PostMapping("/user")
    @Operation(
            summary = "Add new unique user to database",
            description = "Add a user to the database.\nEmail field must be unique in the database.\nAcctID will be auto generated" +
                    "\n\nResponds with JSON of added user, with generated AcctID, and obscured password")
    public ResponseEntity<String> addUser(@RequestBody(description = "")@org.springframework.web.bind.annotation.RequestBody UserDTO userDTO){

        return userService.createUser(new User(userDTO));

    }

    @PutMapping("/user")
    @Operation(
            summary = "Update existing user in database",
            description = "Update a user in the database.\nEmail field must remain unique in the database.\nPassword is used for verification and is not updated via this method" +
                    "\n\nResponds with JSON of updated user, with obscured password")
    public ResponseEntity<String> updateUser(@RequestBody(description = "")@org.springframework.web.bind.annotation.RequestBody UserDTO userDTO){

        return userService.updateUser(new User(userDTO));
    }

    @DeleteMapping("/user")
    @Operation(
            summary = "Delete existing user in database",
            description = "Delete a user from the database.\nChecks AcctID, Email, Password, and deletes matching user")
    public ResponseEntity<String> deleteUser(@RequestBody(description = "")@org.springframework.web.bind.annotation.RequestBody UserDTO userDTO){

        return userService.deleteUser(new User(userDTO));
    }

    @GetMapping("/user/auth")
    @Operation(
            summary = "Authenticate a user",
            description = "Authenticate user.\nChecks AcctID & Password")
    public ResponseEntity<String> authUser(@RequestBody(description = "")@org.springframework.web.bind.annotation.RequestBody UserDTO userDTO){

        return userService.authUser(new User(userDTO));
    }



}
