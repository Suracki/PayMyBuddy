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

    public ResponseEntity<String> addReleationship(Relationship newRelationship) {
        if (relationshipsDAO.getListID(newRelationship.getListOwnerID(), newRelationship.getFriendID()) != -1) {
            //Relationship already exists between these users
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        //Add relationship to database
        newRelationship.setListID(relationshipsDAO.addRelationship(newRelationship.getListOwnerID(), newRelationship.getFriendID()));

        //Build response
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();
        String responseString = gson.toJson(newRelationship);
        HttpHeaders responseHeaders = new HttpHeaders();

        return new ResponseEntity<>(responseString, responseHeaders, HttpStatus.CREATED);
    }

    public ResponseEntity deleteRelationship(Relationship deleteRelationship) {
        int listID = relationshipsDAO.getListID(deleteRelationship.getListOwnerID(), deleteRelationship.getFriendID());
        if (listID == -1) {
            //Relationship does not exist between these users
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        //Remove relationship from database
        int updatedRows = relationshipsDAO.deleteRelationship(listID);

        //Build response
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();
        String responseString = gson.toJson(updatedRows);
        HttpHeaders responseHeaders = new HttpHeaders();

        return new ResponseEntity<>(responseString, responseHeaders, HttpStatus.OK);
    }

    public ResponseEntity getRelationships(int userID){
        //Get list of all relationships for userID
        ArrayList<Integer> relationships = relationshipsDAO.getList(userID);

        //Build response
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();
        String responseString = gson.toJson(relationships);
        HttpHeaders responseHeaders = new HttpHeaders();

        return new ResponseEntity<>(responseString, responseHeaders, HttpStatus.OK);

    }


}
