package com.paymybuddy.integration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.paymybuddy.data.dao.TransactionDAO;
import com.paymybuddy.data.dao.dbConfig.TestDAO;
import com.paymybuddy.logic.gson.LocalDateTimeDeserializer;
import com.paymybuddy.logic.gson.LocalDateTimeSerializer;
import com.paymybuddy.presentation.apimodels.RelationshipDTO;
import com.paymybuddy.presentation.apimodels.TransactionDTO;
import com.paymybuddy.presentation.controller.RelationshipController;
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
import static com.paymybuddy.integration.IntegrationTestConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(RelationshipController.class)
@WebAppConfiguration
@Import(IntegrationTestBeans.class)
public class RelationshipsIT {

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
    public void canAddNewRelationshipToTheSystem() throws Exception {
        //Preparation
        RelationshipDTO relationshipDTO = new RelationshipDTO();
        relationshipDTO.listOwnerID = 1;
        relationshipDTO.friendID = 5;

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();


        //Method
        MvcResult mvcResult = mvc.perform(post("/relationship").contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(relationshipDTO)).accept(MediaType.ALL)).andReturn();

        //Verification
        int status = mvcResult.getResponse().getStatus();
        String receivedResponse = mvcResult.getResponse().getContentAsString().replaceAll("\n", "").replaceAll(" ", "");

        assertEquals(201, status);
        assertEquals(REL_IT_ADD_RELATIONSHIP_SUCCESS, receivedResponse);
    }

    @Test
    public void canDeleteRelationshipFromSystem() throws Exception {
        //Preparation
        RelationshipDTO relationshipDTO = new RelationshipDTO();
        relationshipDTO.listOwnerID = 1;
        relationshipDTO.friendID = 2;
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();


        //Method
        MvcResult mvcResult = mvc.perform(delete("/relationship").contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(relationshipDTO)).accept(MediaType.ALL)).andReturn();

        //Verification
        int status = mvcResult.getResponse().getStatus();
        String receivedResponse = mvcResult.getResponse().getContentAsString();

        assertEquals(200, status);
        assertEquals(REL_IT_DEL_RELATIONSHIP_SUCCESS, receivedResponse);
    }

    @Test
    public void canGetAllRelationshipsForAUser() throws Exception {
        //Preparation

        //Method
        MvcResult mvcResult = mvc.perform(get(REL_IT_GET_REL_IDS)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        //Verification
        int status = mvcResult.getResponse().getStatus();
        String receivedResponse = mvcResult.getResponse().getContentAsString();

        assertEquals(200, status);
        assertEquals(REL_IT_GET_REL_IDS_SUCCESS,receivedResponse);
    }


}
