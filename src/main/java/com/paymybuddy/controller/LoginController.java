package com.paymybuddy.controller;

import com.paymybuddy.dao.UsersDAO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;

@RestController
public class LoginController {

    @RequestMapping("/*")
    @RolesAllowed("USER")
    public String getUser() {
        return "Hello user";
    }


    @RequestMapping("/admin")
    @RolesAllowed("ADMIN")
    public String getAdmin() {
        return "Hello admin";
    }

    @GetMapping("/getUserID")
    @RolesAllowed("USER")
    public int getUserIdByEmail(@RequestParam("email") String email) {
        UsersDAO usersDAO = new UsersDAO();
        return usersDAO.getUserID(email);
    }
}
