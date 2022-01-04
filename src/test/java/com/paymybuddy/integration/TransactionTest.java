package com.paymybuddy.integration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.paymybuddy.data.dao.TransactionDAO;
import com.paymybuddy.data.dao.dbConfig.TestDAO;
import com.paymybuddy.logic.gson.LocalDateTimeDeserializer;
import com.paymybuddy.logic.gson.LocalDateTimeSerializer;
import com.paymybuddy.presentation.apimodels.TransactionDTO;
import com.paymybuddy.presentation.apimodels.TransactionIDDTO;
import com.paymybuddy.presentation.controller.TransactionController;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;
import static com.paymybuddy.integration.IntegrationTestConstants.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(TransactionController.class)
@WebAppConfiguration
@Import(IntegrationTestBeans.class)
public class TransactionTest {

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

    @Test
    public void cannotAddNewTransactionToTheSystemWithInvalidSender() throws Exception {
        //Preparation
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.fromAcctID = 11;
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
        String receivedResponse = mvcResult.getResponse().getContentAsString();

        assertEquals(400, status);
        assertEquals(TRANSAC_IT_ADD_TRANSACTION_FAIL_RESPONSE, receivedResponse);
    }

    @Test
    public void cannotAddNewTransactionToTheSystemWithInvalidReceiver() throws Exception {
        //Preparation
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.fromAcctID = 1;
        transactionDTO.toAcctID = 22;
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
        String receivedResponse = mvcResult.getResponse().getContentAsString();

        assertEquals(400, status);
        assertEquals(TRANSAC_IT_ADD_TRANSACTION_FAIL_RESPONSE, receivedResponse);
    }

    @Test
    public void canMarkATransactionAsPaid() throws Exception {
        //Preparation
        TransactionIDDTO transactionIDDTO = new TransactionIDDTO();
        transactionIDDTO.transactionID = 1;

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        //Method
        MvcResult mvcResult = mvc.perform(put("/transaction").contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(transactionIDDTO)).accept(MediaType.ALL)).andReturn();

        //Verification
        int status = mvcResult.getResponse().getStatus();
        String receivedResponse = mvcResult.getResponse().getContentAsString();

        assertEquals(200, status);
        assertEquals(TRANSAC_IT_MARK_PAID_SUCCESS,receivedResponse);
    }

    @Test
    public void cannotMarkAnInvalidTransactionAsPaid() throws Exception {
        //Preparation
        TransactionIDDTO transactionIDDTO = new TransactionIDDTO();
        transactionIDDTO.transactionID = 200;

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        //Method
        MvcResult mvcResult = mvc.perform(put("/transaction").contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(transactionIDDTO)).accept(MediaType.ALL)).andReturn();

        //Verification
        int status = mvcResult.getResponse().getStatus();
        String receivedResponse = mvcResult.getResponse().getContentAsString();

        assertEquals(404, status);
    }

    @Test
    public void canGetTransactionIDsForAllUnprocessedTransactions() throws Exception {
        //Preparation

        //Method
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(TEST_GET_UNPROCESSED_TRANSACTION_IDS)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        //Verification
        int status = mvcResult.getResponse().getStatus();
        String receivedResponse = mvcResult.getResponse().getContentAsString();

        assertEquals(200, status);
        assertEquals(TEST_GET_UNPROCESSED_TRANSACTION_IDS_SUCCESS,receivedResponse);
    }

    @Test
    public void canGetSentTransactionIDs() throws Exception {
        //Preparation

        //Method
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(TEST_GET_TRANSACTION_SENT_IDS)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        //Verification
        int status = mvcResult.getResponse().getStatus();
        String receivedResponse = mvcResult.getResponse().getContentAsString();

        assertEquals(200, status);
        assertEquals(TEST_GET_TRANSACTION_SENT_IDS_SUCCESS,receivedResponse);
    }

    @Test
    public void canGetSentTransactionDetails() throws Exception {
        //Preparation

        //Method
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(TEST_GET_TRANSACTION_SENT_DETAILS)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        //Verification
        int status = mvcResult.getResponse().getStatus();
        String receivedResponse = mvcResult.getResponse().getContentAsString();

        assertEquals(200, status);
        assertEquals(TEST_GET_TRANSACTION_SENT_DETAILS_SUCCESS,receivedResponse);
    }

    @Test
    public void canGetReceivedTransactionIDs() throws Exception {
        //Preparation

        //Method
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(TEST_GET_TRANSACTION_RECEIVED_IDS)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        //Verification
        int status = mvcResult.getResponse().getStatus();
        String receivedResponse = mvcResult.getResponse().getContentAsString();

        assertEquals(200, status);
        assertEquals(TEST_GET_TRANSACTION_RECEIVED_IDS_SUCCESS,receivedResponse);
    }

    @Test
    public void canGetReceivedTransactionDetails() throws Exception {
        //Preparation

        //Method
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(TEST_GET_TRANSACTION_RECEIVED_DETAILS)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        //Verification
        int status = mvcResult.getResponse().getStatus();
        String receivedResponse = mvcResult.getResponse().getContentAsString();

        assertEquals(200, status);
        assertEquals(TEST_GET_TRANSACTION_RECEIVED_DETAILS_SUCCESS,receivedResponse);
    }

}
