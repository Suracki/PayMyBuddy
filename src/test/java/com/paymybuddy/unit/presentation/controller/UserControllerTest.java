package com.paymybuddy.unit.presentation.controller;

import com.paymybuddy.integration.IntegrationTestBeans;
import com.paymybuddy.logic.UsersService;
import com.paymybuddy.presentation.controller.UserController;
import com.paymybuddy.presentation.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.paymybuddy.unit.presentation.controller.ControllerTestConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(UserController.class)
@WebAppConfiguration
public class UserControllerTest {

    @MockBean
    private UsersService usersService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

//    Saving as example for sending JSON object input
//    @Test
//    public void userControllerCanAuthenticateAUser() throws Exception {
//        //Preparation
//        doReturn(new ResponseEntity<>("User 1 authenticated.", new HttpHeaders(), HttpStatus.OK)).when(usersService).authUser(notNull());
//
//        //Method
//        MvcResult mvcResult = mvc.perform(get("/user/auth").contentType(MediaType.APPLICATION_JSON)
//                        .content(TEST_USER_AUTH_JSON).accept(MediaType.ALL)).andReturn();
//
//        //Verification
//        int status = mvcResult.getResponse().getStatus();
//        String receivedResponse = mvcResult.getResponse().getContentAsString();
//
//        assertEquals(200, status);
//        assertEquals("User 1 authenticated.",receivedResponse);
//    }

    @Test
    public void userControllerCanAuthenticateAUser() throws Exception {
        //Preparation
        doReturn(new ResponseEntity<>("User 1 authenticated.", new HttpHeaders(), HttpStatus.OK)).when(usersService).authUser(notNull());

        //Method
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(TEST_AUTH_URI)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        //Verification
        int status = mvcResult.getResponse().getStatus();
        String receivedResponse = mvcResult.getResponse().getContentAsString();

        assertEquals(200, status);
        assertEquals("User 1 authenticated.",receivedResponse);
    }


}
