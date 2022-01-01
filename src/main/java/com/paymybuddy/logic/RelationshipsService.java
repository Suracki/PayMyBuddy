package com.paymybuddy.logic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.paymybuddy.data.dao.RelationshipsDAO;
import com.paymybuddy.presentation.model.Relationship;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class RelationshipsService {

    private RelationshipsDAO relationshipsDAO;
    private static final Logger logger = LogManager.getLogger("RelationshipsService");

    @Autowired
    public RelationshipsService(RelationshipsDAO relationshipsDAO) {
        this.relationshipsDAO = relationshipsDAO;
    }

    public ResponseEntity<String> addRelationship(Relationship newRelationship) {
        //Add relationship to database
        newRelationship.setListID(relationshipsDAO.addRelationship(newRelationship));

        if (newRelationship.getListID() == -1) {
            //Relationship already exists between these users
            ResponseEntity<String> response = ResponseEntity.status(HttpStatus.CONFLICT).build();
            logger.error("Failed to add relationship. Relationship may already exist between these users",response);
            return response;
        }

        //Build response
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();
        String responseString = gson.toJson(newRelationship);
        HttpHeaders responseHeaders = new HttpHeaders();

        return new ResponseEntity<>(responseString, responseHeaders, HttpStatus.CREATED);
    }

    public ResponseEntity deleteRelationship(Relationship deleteRelationship) {

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

    public ResponseEntity getRelationships(Relationship relationship){
        //Get list of all relationships for userID
        ArrayList<Integer> relationships = relationshipsDAO.getList(relationship);

        //Build response
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();
        String responseString = gson.toJson(relationships);
        HttpHeaders responseHeaders = new HttpHeaders();

        return new ResponseEntity<>(responseString, responseHeaders, HttpStatus.OK);

    }


}
