package com.paymybuddy.integration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.paymybuddy.data.dao.TransactionDAO;
import com.paymybuddy.data.dao.UsersDAO;
import com.paymybuddy.data.dao.dbConfig.DatabaseTestConnection;
import com.paymybuddy.data.dao.dbConfig.TestDAO;
import com.paymybuddy.logic.TransactionService;
import com.paymybuddy.logic.UsersService;
import com.paymybuddy.logic.gson.LocalDateTimeDeserializer;
import com.paymybuddy.logic.gson.LocalDateTimeSerializer;
import com.paymybuddy.presentation.apimodels.TransactionDTO;
import com.paymybuddy.presentation.apimodels.UserDTO;
import com.paymybuddy.presentation.controller.TransactionController;
import com.paymybuddy.presentation.controller.UserController;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import static com.paymybuddy.integration.IntegrationTestConstants.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;

import com.paymybuddy.logic.UsersService;
import com.paymybuddy.presentation.controller.UserController;
import com.paymybuddy.presentation.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.paymybuddy.unit.presentation.controller.ControllerTestConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(TransactionController.class)
@WebAppConfiguration
@Import(IntegrationTestBeans.class)
public class TransactionIT {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private static TransactionDAO transactionDAO;

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
    public void canGetTransactionFromSystem() throws Exception {
        //Preparation

        //Method
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(TEST_GET_TRANSACTION)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        //Verification
        int status = mvcResult.getResponse().getStatus();
        String receivedResponse = mvcResult.getResponse().getContentAsString();

        assertEquals(200, status);
        assertEquals(TRANSAC_IT_GET_TRANSACTION,receivedResponse);
    }

    @Test
    public void canAddNewTransactionToTheSystem() throws Exception {
        //Preparation
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.fromAcctID = 1;
        transactionDTO.toAcctID = 2;
        transactionDTO.description = "test";
        transactionDTO.amount = new BigDecimal("1.4");
        transactionDTO.processed = false;

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
        builder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
        Gson gson = builder.create();


        //Method
        MvcResult mvcResult = mvc.perform(post("/transaction").contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(transactionDTO)).accept(MediaType.ALL)).andReturn();

        //Verification
        int status = mvcResult.getResponse().getStatus();
        String receivedResponse = mvcResult.getResponse().getContentAsString().replaceAll("\n", "").replaceAll(" ", "");
        //receivedResponse = receivedResponse.replaceAll("\n", "").replaceAll(" ", "");

        assertEquals(201, status);
        assertTrue(receivedResponse.matches(TRANSAC_IT_ADD_TRANSACTION_REGEX));
    }

}
