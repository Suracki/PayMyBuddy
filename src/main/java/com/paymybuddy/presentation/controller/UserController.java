package com.paymybuddy.presentation.controller;

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
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private static final Logger logger = LogManager.getLogger("UserController");

    private UsersDAO usersDAO;

    @Autowired
    public UserController(UsersDAO usersDAO){
        this.usersDAO = usersDAO;
    }

    //@PostMapping("/adduser")
    @RequestMapping(value="/adduser",produces="application/json",method={RequestMethod.POST})
    public ResponseEntity<String> addUser(@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName,
                                          @RequestParam("address") String address, @RequestParam("city") String city,
                                          @RequestParam("zip") String zip, @RequestParam("phone") String phone,
                                          @RequestParam("email") String email, @RequestParam("password") String password) {

        int userID = usersDAO.getUserID(email);
        System.out.println("TEST");

        if (userID == 0) {
            //User already exists with this email
            ResponseEntity<String> response = ResponseEntity.status(HttpStatus.CONFLICT).build();
            logger.error("User already exists with this email address",response);
            return response;
        }

        //Add user
        User newUser = new User(firstName, lastName, address, city, zip, phone, email, password);
        userID = usersDAO.addUser(newUser);

        //Set ID + clear password before responding
        newUser.setAcctID(userID);
        newUser.setPassword("*********");

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();
        String responseString = gson.toJson(newUser);
        HttpHeaders responseHeaders = new HttpHeaders();
        ResponseEntity<String> response = new ResponseEntity<>(responseString, responseHeaders, HttpStatus.CREATED);

        //Log response
        logger.info("User created", responseString);
        return response;
    }

    @PostMapping("/user")
    public String test(@RequestParam("firstName") String firstName) {


        System.out.println("TESTING");

        return "THIS IS A TEST";
    }

    @RequestMapping(value="/cartoons",produces="application/json",method=RequestMethod.GET)
    public String testtwo(@RequestParam("firstName") String firstName) {

        return "THIS IS A TEST";
    }

    @RequestMapping(value="/movies",produces="application/json",method={RequestMethod.GET,RequestMethod.POST})
    public String testthree(@RequestParam("firstName") String firstName) {


        System.out.println("TESTING");

        return "THIS IS A TEST";
    }



}
