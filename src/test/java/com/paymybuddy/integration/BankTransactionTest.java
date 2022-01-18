package com.paymybuddy.integration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.paymybuddy.data.dao.dbConfig.TestDAO;
import com.paymybuddy.presentation.apimodels.BankTransactionDTO;
import com.paymybuddy.presentation.apimodels.UserDTO;
import com.paymybuddy.presentation.controller.BankTransactionController;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;

import static com.paymybuddy.integration.IntegrationTestConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(BankTransactionController.class)
@WebAppConfiguration
@Import(IntegrationTestBeans.class)
public class BankTransactionTest {

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
    public void canMakeAddFundRequest() throws Exception {
        //Preparation
        BankTransactionDTO bankTransactionDTO = new BankTransactionDTO();
        bankTransactionDTO.acctID = 1;
        bankTransactionDTO.amount = new BigDecimal("100");
        bankTransactionDTO.BIC = "BNPAGFGX";
        bankTransactionDTO.IBAN = "FR1420041010050500013M02606";

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();


        //Method
        MvcResult mvcResult = mvc.perform(post("/banktransaction").contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(bankTransactionDTO)).accept(MediaType.ALL)).andReturn();

        //Verification
        int status = mvcResult.getResponse().getStatus();
        String receivedResponse = mvcResult.getResponse().getContentAsString().replaceAll("\n", "").replaceAll(" ", "");

        System.out.println(receivedResponse);

        assertEquals(200, status);
        assertTrue(receivedResponse.matches(BANKTRANSACTION_IT_ADD_FUNDS_SUCCESS_REGEX));
    }

    @Test
    public void canMakeWithdrawFundRequest() throws Exception {
        //Preparation
        BankTransactionDTO bankTransactionDTO = new BankTransactionDTO();
        bankTransactionDTO.acctID = 1;
        bankTransactionDTO.amount = new BigDecimal("-10");
        bankTransactionDTO.BIC = "BNPAGFGX";
        bankTransactionDTO.IBAN = "FR1420041010050500013M02606";

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();


        //Method
        MvcResult mvcResult = mvc.perform(post("/banktransaction").contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(bankTransactionDTO)).accept(MediaType.ALL)).andReturn();

        //Verification
        int status = mvcResult.getResponse().getStatus();
        String receivedResponse = mvcResult.getResponse().getContentAsString().replaceAll("\n", "").replaceAll(" ", "");

        System.out.println(receivedResponse);

        assertEquals(200, status);
        assertTrue(receivedResponse.matches(BANKTRANSACTION_IT_WITHDRAW_FUNDS_SUCCESS_REGEX));
    }

    @Test
    public void cannotMakeWithdrawFundRequestForMoreThanAccountBalance() throws Exception {
        //Preparation
        BankTransactionDTO bankTransactionDTO = new BankTransactionDTO();
        bankTransactionDTO.acctID = 1;
        bankTransactionDTO.amount = new BigDecimal("-10000");
        bankTransactionDTO.BIC = "BNPAGFGX";
        bankTransactionDTO.IBAN = "FR1420041010050500013M02606";

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();


        //Method
        MvcResult mvcResult = mvc.perform(post("/banktransaction").contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(bankTransactionDTO)).accept(MediaType.ALL)).andReturn();

        //Verification
        int status = mvcResult.getResponse().getStatus();
        String receivedResponse = mvcResult.getResponse().getContentAsString();

        System.out.println(receivedResponse);

        assertEquals(400, status);
        assertEquals(BANKTRANSACTION_IT_WITHDRAW_FUNDS_FAIL, receivedResponse);
    }

    @Test
    public void canGetBankTransactionDetails() throws Exception {
        //Preparation

        //Method
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(TEST_GET_BANK_TRANSACTION_DETAILS)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        //Verification
        int status = mvcResult.getResponse().getStatus();
        String receivedResponse = mvcResult.getResponse().getContentAsString();

        assertEquals(200, status);
        assertEquals(TEST_GET_BANK_TRANSACTION_DETAILS_SUCCESS,receivedResponse);
    }

    @Test
    public void canGetBankSingleTransactionDetails() throws Exception {
        //Preparation

        //Method
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(TEST_GET_SINGLE_BANK_TRANSACTION_DETAILS)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        //Verification
        int status = mvcResult.getResponse().getStatus();
        String receivedResponse = mvcResult.getResponse().getContentAsString();

        assertEquals(200, status);
        assertEquals(TEST_GET_SINGLE_BANK_TRANSACTION_DETAILS_SUCCESS,receivedResponse);
    }

}
