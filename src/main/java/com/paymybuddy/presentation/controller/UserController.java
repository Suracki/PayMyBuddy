package com.paymybuddy.presentation.controller;

import com.paymybuddy.logic.UsersService;
import com.paymybuddy.presentation.apimodels.UserDTO;
import com.paymybuddy.presentation.apimodels.UserPassDTO;
import com.paymybuddy.presentation.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
public class UserController {

    private static PasswordEncoder passwordEncoder;

    private static final Logger logger = LogManager.getLogger("UserController");

    private UsersService userService;

    @Autowired
    public UserController(UsersService userService, PasswordEncoder passwordEncoder){
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/user")
    @Operation(
            summary = "Add new unique user to database",
            description = "Add a user to the database.\nEmail field must be unique in the database.\nAcctID will be auto generated" +
                    "\n\nResponds with JSON of added user, with generated AcctID, and obscured password")
    public ResponseEntity<String> addUser(@RequestBody(description = "")@org.springframework.web.bind.annotation.RequestBody UserDTO userDTO){

        userDTO.password = passwordEncoder.encode(userDTO.password);
        System.out.println(userDTO.password);
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

    @PutMapping("/user/auth")
    @Operation(
            summary = "Update user password",
            description = "Updates a user's password.\nChecks AcctID &  old Password")
    public ResponseEntity<String> updateUserPassword(@RequestBody(description = "")@org.springframework.web.bind.annotation.RequestBody UserPassDTO userPassDTO){

        return userService.changePassword(new User(userPassDTO), userPassDTO.newPassword);
    }

    @GetMapping("/user/auth")
    @Operation(
            summary = "Authenticate a user",
            description = "Authenticate user.\nChecks Email(login) & Password")
    public ResponseEntity<String> authUser(@RequestParam("Email")String email, @RequestParam("Password") String password){

        String encodedPassword = passwordEncoder.encode(password);
        System.out.println(passwordEncoder.encode("password"));
        System.out.println(passwordEncoder.encode("password"));
        System.out.println(passwordEncoder.encode("password"));
        System.out.println(passwordEncoder.encode("password"));
        System.out.println(passwordEncoder.encode("password"));
        String hashone = passwordEncoder.encode("password");
        String hashtwo = passwordEncoder.encode("password");

        System.out.println(passwordEncoder.matches("password", hashone));
        System.out.println(passwordEncoder.matches("password", hashtwo));
        System.out.println(passwordEncoder.matches("password", passwordEncoder.encode("password")));


        return userService.authUser(new User(email, password));
    }



}
