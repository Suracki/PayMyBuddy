package com.paymybuddy.logic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nimbusds.jose.shaded.json.JSONArray;
import com.paymybuddy.data.dao.RelationshipsDAO;
import com.paymybuddy.data.dao.UsersDAO;
import com.paymybuddy.presentation.model.Relationship;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


/**
 * RelationshipsService performs operations and generates ResponseEntities for the RelationshipsController endpoints
 */
@Service
public class RelationshipsService extends BaseService{

    private RelationshipsDAO relationshipsDAO;
    private UsersDAO usersDAO;
    private static final Logger logger = LogManager.getLogger("RelationshipsService");

    @Autowired
    public RelationshipsService(RelationshipsDAO relationshipsDAO, UsersDAO usersDAO) {
        this.relationshipsDAO = relationshipsDAO;
        this.usersDAO = usersDAO;
    }

    /**
     * Add a new relationship to the database, using AcctID as identifier of friend.
     *
     * @param newRelationship Relationship object containing details of relationship to be added
     * @return ResponseEntity containing the output
     */
    public ResponseEntity<String> addRelationship(Relationship newRelationship) {
        logger.info("Processing addRelationship Relationship request");
        //Add relationship to database
        newRelationship.setListID(relationshipsDAO.addRelationship(newRelationship));

        if (newRelationship.getListID() == -1) {
            //Relationship already exists between these users
            ResponseEntity<String> response = ResponseEntity.status(HttpStatus.CONFLICT).build();
            logger.error("Failed to add relationship. User not found, or relationship may already exist between these users",response);
            return response;
        }

        //Build response
        ResponseEntity<String> response = createdResponse(newRelationship);
        logger.info("Relationship added", response);

        return response;
    }

    /**
     * Add a new relationship to the database, using email address as identifier of friend.
     *
     * @param newRelationship Relationship object containing details of relationship to be added
     * @return ResponseEntity containing the output
     */
    public ResponseEntity<String> addRelationshipByEmail(Relationship newRelationship) {
        logger.info("Processing addRelationshipByEmail Relationship request");
        //Add relationship to database
        newRelationship.setListID(relationshipsDAO.addRelationshipByEmail(newRelationship));

        if (newRelationship.getListID() == -1) {
            //Relationship already exists between these users, or user not found/inactive
            ResponseEntity<String> response = ResponseEntity.status(HttpStatus.CONFLICT).build();
            logger.error("Failed to add relationship. User not found, or relationship may already exist between these users",response);
            return response;
        }

        //Build response
        ResponseEntity<String> response = createdResponse(newRelationship);
        logger.info("Relationship added", response);

        return response;
    }

    /**
     * Remove a relationship from the database
     *
     * @param deleteRelationship Relationship object containing details of relationship to be added
     * @return ResponseEntity containing the output
     */
    public ResponseEntity deleteRelationship(Relationship deleteRelationship) {
        logger.info("Processing deleteRelationship Relationship request");

        //Delete relationship from database
        int updatedRows = relationshipsDAO.deleteRelationship(deleteRelationship);

        if (updatedRows == -1) {
            //Relationship could not be deleted
            ResponseEntity<String> response = new ResponseEntity<>("Delete failed. Relationship does not exist, or password incorrect", new HttpHeaders(), HttpStatus.NOT_FOUND);
            logger.error("Failed to delete relationship",response);
            return response;
        }

        //Build response
        ResponseEntity<String> response = new ResponseEntity<>("Relationship between users " + deleteRelationship.getListOwnerID() + " and " +
                deleteRelationship.getFriendID() + " deleted", new HttpHeaders(), HttpStatus.OK);
        logger.info(updatedRows + " relationships(s) deleted", response);
        return response;
    }

    /**
     * Get details a relationship from the database
     *
     * @param relationship Relationship object containing ID of relationship
     * @return ResponseEntity containing the output
     */
    public ResponseEntity getRelationships(Relationship relationship){
        logger.info("Processing getRelationships Relationship request to get all Relationships for a User");
        //Get list of all relationships for userID
        JSONArray json = relationshipsDAO.getRelationships(relationship);

        //Build response
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();
        String responseString = gson.toJson(json);
        HttpHeaders responseHeaders = new HttpHeaders();

        return new ResponseEntity<>(responseString, responseHeaders, HttpStatus.OK);

    }


}
