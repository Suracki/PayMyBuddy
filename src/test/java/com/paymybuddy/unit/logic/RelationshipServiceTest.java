package com.paymybuddy.unit.logic;

import com.paymybuddy.data.dao.RelationshipsDAO;
import com.paymybuddy.logic.RelationshipsService;
import com.paymybuddy.presentation.model.Relationship;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;

import static com.paymybuddy.unit.logic.TestServiceConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class RelationshipServiceTest {

    @Mock
    RelationshipsDAO relationshipsDAO;

    @InjectMocks
    RelationshipsService relationshipsService;

    @Test
    public void relationshipsServiceCanAddNewRelationship() {
        //Prepare
        Relationship newRelationship = new Relationship(1,2);
        ResponseEntity<String> response;
        doReturn(-1).when(relationshipsDAO).getListID(anyInt(),anyInt());
        doReturn(3).when(relationshipsDAO).addRelationship(anyInt(),anyInt());

        //Perform
        response = relationshipsService.addReleationship(newRelationship);

        //Verify
        assertEquals(RELSERVICE_CREATED_RESPONSE, response.toString());
    }

    @Test
    public void relationshipServiceWillNotAddDuplicateRelationship() {
        //Prepare
        Relationship newRelationship = new Relationship(1,2);
        ResponseEntity<String> response;
        doReturn(3).when(relationshipsDAO).getListID(anyInt(),anyInt());

        //Perform
        response = relationshipsService.addReleationship(newRelationship);

        //Verify
        assertEquals(RELSERVICE_CREATED_FAILRESPONSE, response.toString());
    }

    @Test
    public void relationshipServiceCanDeleteRelationship() {
        //Prepare
        Relationship deleteRelationship = new Relationship(1,2);
        ResponseEntity<String> response;
        doReturn(3).when(relationshipsDAO).getListID(anyInt(),anyInt());
        doReturn(1).when(relationshipsDAO).deleteRelationship(3);

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
        doReturn(-1).when(relationshipsDAO).getListID(anyInt(),anyInt());

        //Perform
        response = relationshipsService.deleteRelationship(deleteRelationship);

        //Verify
        assertEquals(RELSERVICE_DELETED_FAILRESPONSE, response.toString());
    }

    @Test
    public void relationshipsServiceCanGetListOfAllRelationshipsForAccountID() {
        //Prepare
        ResponseEntity<String> response;
        ArrayList<Integer> result = new ArrayList<>();
        result.add(2);
        result.add(3);
        doReturn(result).when(relationshipsDAO).getList(anyInt());

        //Perform
        response = relationshipsService.getRelationships(1);

        //Verify
        assertEquals(RELSERVICE_GETLIST_RESPONSE, response.toString());
    }

    @Test
    public void relationshipServiceCanGetListOfRelationshipsEvenIfEmpty() {
        //Prepare
        ResponseEntity<String> response;
        ArrayList<Integer> result = new ArrayList<>();
        doReturn(result).when(relationshipsDAO).getList(anyInt());

        //Perform
        response = relationshipsService.getRelationships(1);

        //Verify
        assertEquals(RELSERVICE_GETLIST_EMPTYRESPONSE, response.toString());
    }

}
