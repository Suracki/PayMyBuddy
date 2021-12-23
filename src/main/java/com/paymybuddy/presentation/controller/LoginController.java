package com.paymybuddy.presentation.controller;

import com.paymybuddy.data.dao.UsersDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;

@RestController
public class LoginController {

//    @RequestMapping("/*")
//    @RolesAllowed("USER")
//    public String getUser() {
//        return "Hello user";
//    }

    @Autowired
    private UsersDAO usersDAO;

    @RequestMapping("/admin")
    //@RolesAllowed("ADMIN")
    public String getAdmin() {
        return "Hello admin";
    }

    @GetMapping("/getUserID")
    //@RolesAllowed("ADMIN")
    public int getUserIdByEmail(@RequestParam("email") String email) {

        return usersDAO.getUserID(email);
    }

}
