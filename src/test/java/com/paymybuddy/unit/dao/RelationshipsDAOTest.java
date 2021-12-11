package com.paymybuddy.unit.dao;

import com.paymybuddy.dao.RelationshipsDAO;
import com.paymybuddy.dao.UsersDAO;
import com.paymybuddy.dbConfig.DatabaseTestConnection;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ExtendWith(MockitoExtension.class)
public class RelationshipsDAOTest {

    private static DatabaseTestConnection databaseTestConfig = new DatabaseTestConnection();
    private static RelationshipsDAO relationshipsDAO;
    private static ArrayList<Integer> listIDs;

    @BeforeAll
    private static void setUp() {
        relationshipsDAO = new RelationshipsDAO();
        listIDs = new ArrayList<>();
    }

    @AfterEach
    private void cleanUp() {
        for (int id : listIDs) {
            deleteRelationship(id);
        }
    }

    @Test
    public void relationshipsDAOCanGetARelationshipID() {
        //Prepare
        int listID = addRelationship(1,2);
        listIDs.add(listID);
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

        //Method
        relID = addRelationship(1,2);
        listIDs.add(relID);

        //Verification
        assertNotEquals(-1, relID);

    }

    @Test
    public void relationshipsDAOCanDeleteExistingRelationship() {
        //Prepare
        int listID = addRelationship(1,2);
        int affectedRows = -1;

        //Method
        affectedRows = deleteRelationship(listID);

        //Verification
        assertEquals(1, affectedRows);

    }

    @Test
    public void relationshipsDAOCanGetAllRelationshipsForAUser() {
        //Prepare
        int listIDOne = addRelationship(1,2);
        int listIDTwo = addRelationship(1,3);
        int listIDThree = addRelationship(1,4);
        listIDs.add(listIDOne);
        listIDs.add(listIDTwo);
        listIDs.add(listIDThree);
        ArrayList<Integer> list = new ArrayList();

        //Method
        list = relationshipsDAO.getList(1);

        //Verification
        assertEquals(3, list.size());

    }

    private int addRelationship(int owner, int friend) {
        return relationshipsDAO.addRelationship(owner,friend);
    }

    private int deleteRelationship(int listID) {
        return relationshipsDAO.deleteRelationship(listID);
    }


}

