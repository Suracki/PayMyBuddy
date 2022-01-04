package com.paymybuddy.integration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.paymybuddy.data.dao.dbConfig.TestDAO;
import com.paymybuddy.presentation.apimodels.UserDTO;
import com.paymybuddy.presentation.apimodels.UserPassDTO;
import com.paymybuddy.presentation.controller.UserController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;
import java.math.BigDecimal;
import static com.paymybuddy.integration.IntegrationTestConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(UserController.class)
@WebAppConfiguration
@Import(IntegrationTestBeans.class)
public class UsersTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private static TestDAO testDAO;

    @BeforeAll
    private static void setUp() {

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
    public void canAddNewUser() throws Exception {
        //Preparation
        UserDTO userDTO = new UserDTO();
        userDTO.firstName = "first";
        userDTO.lastName = "last";
        userDTO.address = "add";
        userDTO.city = "city";
        userDTO.zip = "zip";
        userDTO.phone = "123";
        userDTO.email = "email";
        userDTO.password = "password";
        userDTO.balance = new BigDecimal("0");

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();


        //Method
        MvcResult mvcResult = mvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(userDTO)).accept(MediaType.ALL)).andReturn();

        //Verification
        int status = mvcResult.getResponse().getStatus();
        String receivedResponse = mvcResult.getResponse().getContentAsString().replaceAll("\n", "").replaceAll(" ", "");

        assertEquals(201, status);
        assertEquals(USER_IT_ADD_USER_SUCCESS, receivedResponse);
    }

    @Test
    public void canGetUser() throws Exception {
        //Preparation
        int acctID = 1;

        //Method
        MvcResult mvcResult = mvc.perform(get(USER_IT_GET_USER)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        //Verification
        int status = mvcResult.getResponse().getStatus();
        String receivedResponse = mvcResult.getResponse().getContentAsString().replaceAll("\n", "").replaceAll(" ", "");

        assertEquals(200, status);
        assertEquals(USER_IT_GET_USER_SUCCESS, receivedResponse);
    }

    @Test
    public void canUpdateUser() throws Exception {
        //Preparation
        UserDTO userDTO = new UserDTO();
        userDTO.acctID = 2;
        userDTO.firstName = "first";
        userDTO.lastName = "last";
        userDTO.address = "add";
        userDTO.city = "city";
        userDTO.zip = "zip";
        userDTO.phone = "123";
        userDTO.email = "email";
        userDTO.password = "password";
        userDTO.balance = new BigDecimal("0");

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();


        //Method
        MvcResult mvcResult = mvc.perform(put("/user").contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(userDTO)).accept(MediaType.ALL)).andReturn();

        //Verification
        int status = mvcResult.getResponse().getStatus();
        String receivedResponse = mvcResult.getResponse().getContentAsString().replaceAll("\n", "").replaceAll(" ", "");

        assertEquals(200, status);
        assertEquals(USER_IT_UPDATE_USER_SUCCESS, receivedResponse);
    }

    @Test
    public void canDeleteUser() throws Exception {
        //Preparation

        //Method
        MvcResult mvcResult = mvc.perform(delete(USER_IT_DEL_USER)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        //Verification
        int status = mvcResult.getResponse().getStatus();
        String receivedResponse = mvcResult.getResponse().getContentAsString();

        assertEquals(200, status);
        assertEquals(USER_IT_DEL_USER_SUCCESS, receivedResponse);
    }

    @Test
    public void canUpdateUserPassword() throws Exception {
        //Preparation
        UserPassDTO userPassDTO = new UserPassDTO();
        userPassDTO.acctID = 1;
        userPassDTO.newPassword = "newpassword";

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();


        //Method
        MvcResult mvcResult = mvc.perform(put("/user/auth").contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(userPassDTO)).accept(MediaType.ALL)).andReturn();

        //Verification
        int status = mvcResult.getResponse().getStatus();
        String receivedResponse = mvcResult.getResponse().getContentAsString();

        assertEquals(200, status);
        assertEquals(USER_IT_UPDATE_PW_SUCCESS, receivedResponse);
    }

    @Test
    public void canAuthUserByGettingPwHashFromDB() throws Exception {
        //Preparation

        //Method
        MvcResult mvcResult = mvc.perform(get(USER_IT_AUTH_USER)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        //Verification
        int status = mvcResult.getResponse().getStatus();
        String receivedResponse = mvcResult.getResponse().getContentAsString();

        assertEquals(200, status);
        assertEquals(USER_IT_AUTH_USER_SUCCESS,receivedResponse);
    }

    @Test
    public void canDenyInvalidUserPwByGettingPwHashFromDB() throws Exception {
        //Preparation

        //Method
        MvcResult mvcResult = mvc.perform(get(USER_IT_AUTH_BAD_USER)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        //Verification
        int status = mvcResult.getResponse().getStatus();
        String receivedResponse = mvcResult.getResponse().getContentAsString();

        assertEquals(404, status);
        assertEquals(USER_IT_AUTH_USER_FAIL,receivedResponse);
    }


}
