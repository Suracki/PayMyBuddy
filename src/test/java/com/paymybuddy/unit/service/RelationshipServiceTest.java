package com.paymybuddy.unit.service;

import com.nimbusds.jose.shaded.json.JSONArray;
import com.nimbusds.jose.shaded.json.JSONObject;
import com.paymybuddy.data.dao.RelationshipsDAO;
import com.paymybuddy.data.dao.UsersDAO;
import com.paymybuddy.logic.RelationshipsService;
import com.paymybuddy.presentation.model.Relationship;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;

import static com.paymybuddy.unit.service.TestServiceConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class RelationshipServiceTest {

    @Mock
    RelationshipsDAO relationshipsDAO;
    @Mock
    UsersDAO usersDAO;

    @InjectMocks
    RelationshipsService relationshipsService;

    @Test
    public void relationshipsServiceCanAddNewRelationship() {
        //Prepare
        Relationship newRelationship = new Relationship(1,2);
        ResponseEntity<String> response;
        doReturn(3).when(relationshipsDAO).addRelationship(any());

        //Perform
        response = relationshipsService.addRelationship(newRelationship);

        //Verify
        assertEquals(RELSERVICE_CREATED_RESPONSE, response.toString());
    }

    @Test
    public void relationshipServiceCanAddNewRelationshipByEmail() {
        //Prepare
        Relationship newRelationship = new Relationship();
        newRelationship.setListOwnerID(1);
        newRelationship.setFriendEmail("email");
        doReturn(3).when(relationshipsDAO).addRelationshipByEmail(newRelationship);

        //Perform
        ResponseEntity<String> response = relationshipsService.addRelationshipByEmail(newRelationship);

        //Verify
        assertEquals(RELSERVICE_CREATED_EMAIL_RESPONSE, response.toString());
    }

    @Test
    public void relationshipServiceWillReturnErrorIfNoUserExistsForEmail() {
        //Prepare
        Relationship newRelationship = new Relationship();
        newRelationship.setListOwnerID(1);
        newRelationship.setFriendEmail("email");
        doReturn(-1).when(relationshipsDAO).addRelationshipByEmail(newRelationship);

        //Perform
        ResponseEntity<String> response = relationshipsService.addRelationshipByEmail(newRelationship);

        //Verify
        assertEquals(RELSERVICE_CREATED_FAILRESPONSE, response.toString());
    }

    @Test
    public void relationshipServiceWillNotAddDuplicateRelationship() {
        //Prepare
        Relationship newRelationship = new Relationship(1,2);
        ResponseEntity<String> response;
        doReturn(-1).when(relationshipsDAO).addRelationship(any());

        //Perform
        response = relationshipsService.addRelationship(newRelationship);

        //Verify
        assertEquals(RELSERVICE_CREATED_FAILRESPONSE, response.toString());
    }

    @Test
    public void relationshipServiceCanDeleteRelationship() {
        //Prepare
        Relationship deleteRelationship = new Relationship(1,2);
        ResponseEntity<String> response;
        doReturn(1).when(relationshipsDAO).deleteRelationship(any());

        //Perform
        response = relationshipsService.deleteRelationship(deleteRelationship);

        //Verify
        assertEquals(RELSERVICE_DELETED_RESPONSE, response.toString());
    }

    @Test
    public void relationshipServiceReturnsErrorAttemptingToDeleteNonExistingRelationship() {
        //Prepare
        Relationship deleteRelationship = new Relationship(1,2);
        ResponseEntity<String> response;
        doReturn(-1).when(relationshipsDAO).deleteRelationship(any());

        //Perform
        response = relationshipsService.deleteRelationship(deleteRelationship);

        //Verify
        assertEquals(RELSERVICE_DELETED_FAILRESPONSE, response.toString());
    }

    @Test
    public void relationshipsServiceCanGetListOfAllRelationshipsForAccountID() {
        //Prepare
        ResponseEntity<String> response;
        JSONArray json = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ListID",1);
        jsonObject.put("ListOwnerID",1);
        jsonObject.put("ListFriendID",2);
        json.add(jsonObject);
        doReturn(json).when(relationshipsDAO).getRelationships(any());

        //Perform
        response = relationshipsService.getRelationships(new Relationship(1,1));

        //Verify
        assertEquals(RELSERVICE_GETLIST_RESPONSE, response.toString());
    }

    @Test
    public void relationshipServiceCanGetListOfRelationshipsEvenIfEmpty() {
        //Prepare
        ResponseEntity<String> response;
        JSONArray json = new JSONArray();
        doReturn(json).when(relationshipsDAO).getRelationships(any());

        //Perform
        response = relationshipsService.getRelationships(new Relationship(1,2));

        //Verify
        assertEquals(RELSERVICE_GETLIST_EMPTYRESPONSE, response.toString());
    }

}
