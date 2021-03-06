package com.paymybuddy.unit.dao;

import com.nimbusds.jose.shaded.json.JSONArray;
import com.paymybuddy.data.dao.RelationshipsDAO;
import com.paymybuddy.data.dao.dbConfig.DatabaseTestConnection;
import com.paymybuddy.data.dao.dbConfig.TestDAO;
import com.paymybuddy.presentation.model.Relationship;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ExtendWith(MockitoExtension.class)
public class RelationshipsDAOTest {

    private static RelationshipsDAO relationshipsDAO;
    private static TestDAO testDAO;

    @BeforeAll
    private static void setUp() {
        relationshipsDAO = new RelationshipsDAO();
        relationshipsDAO.databaseConnection = new DatabaseTestConnection();
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
    public void relationshipsDAOCanGetARelationshipID() {
        //Prepare
        testDAO.clearRelationshipTable();
        addRelationship(2,3);
        int listID = addRelationship(1,2);
        int relID = -1;

        //Method
        relID = relationshipsDAO.getListID(1,2);

        //Verify
        assertEquals(listID, relID);
    }

    @Test
    public void relationshipsDAOCanAddNewRelationship() {
        //Prepare
        int relID = -1;
        Relationship relationship = new Relationship();
        relationship.setListOwnerID(1);
        relationship.setFriendID(5);

        //Method
        relID = relationshipsDAO.addRelationship(relationship);

        //Verification
        assertEquals(11, relID);

    }

    @Test
    public void relationshipsDAOCanAddNewRelationshipByEmail() {
        //Prepare
        int relID = -1;
        Relationship relationship = new Relationship();
        relationship.setListOwnerID(1);
        relationship.setFriendEmail("tenz@email.com");

        //Method
        relID = relationshipsDAO.addRelationshipByEmail(relationship);

        //Verification
        assertEquals(11, relID);

    }

    @Test
    public void relationshipsDAOCanDeleteExistingRelationship() {
        //Prepare
        int listID = addRelationship(1,5);
        int affectedRows = -1;

        //Method
        affectedRows = deleteRelationship(1, 5);

        //Verification
        assertEquals(1, affectedRows);

    }

    @Test
    public void relationshipsDAOCanGetAllRelationshipsForAUserAndIgnoresInactiveUsers() {
        //Prepare
        JSONArray json;

        //Method
        json = relationshipsDAO.getRelationships(new Relationship(1,2));

        //Verification
        assertEquals(2, json.size());

    }

    private int addRelationship(int owner, int friend) {
        return relationshipsDAO.addRelationship(new Relationship(owner, friend));
    }

    private int deleteRelationship(int owner, int friend) {
        return relationshipsDAO.deleteRelationship(new Relationship(owner, friend));
    }


}

