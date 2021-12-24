package com.paymybuddy.presentation.controller;

import com.paymybuddy.logic.RelationshipsService;
import com.paymybuddy.presentation.apimodels.RelationshipDTO;
import com.paymybuddy.presentation.apimodels.RelationshipGetDTO;
import com.paymybuddy.presentation.model.Relationship;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RelationshipController {

    private static final Logger logger = LogManager.getLogger("UserController");

    private RelationshipsService relationshipsService;

    @Autowired
    public RelationshipController(RelationshipsService relationshipsService){
        this.relationshipsService = relationshipsService;
    }

    @PostMapping("/relationship")
    @Operation(
            summary = "Add new unique user relationship to database",
            description = "Add a relationship to the database.\nListOwnerID must not already have a relationship with this ListFriendID.\nListID will be auto generated" +
                    "\n\nListOwnerID, ListFriendID, and ListOwner Password required\nResponds with JSON of added relationship with generated ListID")
    public ResponseEntity<String> addRelationship(@RequestBody(description = "")@org.springframework.web.bind.annotation.RequestBody RelationshipDTO relationshipDTO){

        return relationshipsService.addRelationship(new Relationship(relationshipDTO), relationshipDTO.listOwnerPassword);

    }

    @DeleteMapping("/relationship")
    @Operation(
            summary = "Delete user relationship from database",
            description = "Delete a relationship from the database." +
                    "\nListOwnerID, ListFriendID, and ListOwner Password required")
    public ResponseEntity<String> deleteRelationship(@RequestBody(description = "")@org.springframework.web.bind.annotation.RequestBody RelationshipDTO relationshipDTO){

        return relationshipsService.deleteRelationship(new Relationship(relationshipDTO), relationshipDTO.listOwnerPassword);

    }

    @GetMapping("/relationship")
    @Operation(
            summary = "Get all relationships for one user from database",
            description = "Gets a list of all users that provided user currently has a stored relationship with." +
                    "\nListOwnerID and ListOwner Password required")
    public ResponseEntity<String> getRelationships(@RequestBody(description = "")@org.springframework.web.bind.annotation.RequestBody RelationshipGetDTO relationshipGetDTO){

        return relationshipsService.getRelationships(new Relationship(relationshipGetDTO), relationshipGetDTO.listOwnerPassword);

    }

}
