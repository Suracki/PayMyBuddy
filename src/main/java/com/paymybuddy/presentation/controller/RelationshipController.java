package com.paymybuddy.presentation.controller;

import com.paymybuddy.logic.RelationshipsService;
import com.paymybuddy.presentation.apimodels.RelationshipDTO;
import com.paymybuddy.presentation.apimodels.RelationshipEmailDTO;
import com.paymybuddy.presentation.model.Relationship;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * RestController for /relationship endpoint
 *
 * Endpoint includes:
 *
 * GET/DELETE /relationship
 * POST /relationship/id & /relationship/email
 */
@RestController
public class RelationshipController {

    private static final Logger logger = LogManager.getLogger("UserController");

    private RelationshipsService relationshipsService;

    @Autowired
    public RelationshipController(RelationshipsService relationshipsService){
        this.relationshipsService = relationshipsService;
    }

    /**
     * POST mapping to add a relationship to the database by friend's AcctID
     *
     * Returns:
     * HttpStatus.CONFLICT if relationship exists, or user cannot be found
     * Json string & HttpStatus.CREATED if successful
     *
     * @param relationshipDTO details of relationship to be added
     * @return Json string & HttpStatus.CREATED if successful
     */
    @PostMapping("/relationship/id")
    @Operation(
            summary = "Add new unique user relationship to database",
            description = "Add a relationship to the database.\nListOwnerID must not already have a relationship with this ListFriendID.\nListID will be auto generated" +
                    "\n\nListOwnerID, ListFriendID, and ListOwner Password required\nResponds with JSON of added relationship with generated ListID")
    public ResponseEntity<String> addRelationship(@RequestBody(description = "")@org.springframework.web.bind.annotation.RequestBody RelationshipDTO relationshipDTO){

        return relationshipsService.addRelationship(new Relationship(relationshipDTO));

    }

    /**
     * POST mapping to add a relationship to the database by friend's email
     *
     * Returns:
     * HttpStatus.CONFLICT if relationship exists, or user cannot be found
     * Json string & HttpStatus.CREATED if successful
     *
     * @param relationshipEmailDTO details of relationship to be added
     * @return Json string & HttpStatus.CREATED if successful
     */
    @PostMapping("/relationship/email")
    @Operation(
            summary = "Add new unique user relationship to database by email",
            description = "Add a relationship to the database.\nListOwnerID must not already have a relationship with this email address.\nListID will be auto generated" +
                    "\n\nListOwnerID, ListFriendEmail, and ListOwner Password required\nResponds with JSON of added relationship with generated ListID")
    public ResponseEntity<String> addRelationshipByEMail(@RequestBody(description = "")@org.springframework.web.bind.annotation.RequestBody RelationshipEmailDTO relationshipEmailDTO){

        return relationshipsService.addRelationshipByEmail(new Relationship(relationshipEmailDTO));

    }

    /**
     * DELETE mapping to remove a relationship from the database
     *
     * Returns:
     * HttpStatus.NOT_FOUND if relationship not found
     * HttpStatus.OK if successful
     *
     * @param relationshipDTO details of relationship to be deleted
     * @return HttpStatus.OK if successful
     */
    @DeleteMapping("/relationship")
    @Operation(
            summary = "Delete user relationship from database",
            description = "Delete a relationship from the database." +
                    "\nListOwnerID, ListFriendID, and ListOwner Password required")
    public ResponseEntity<String> deleteRelationship(@RequestBody(description = "")@org.springframework.web.bind.annotation.RequestBody RelationshipDTO relationshipDTO){

        return relationshipsService.deleteRelationship(new Relationship(relationshipDTO));

    }

    /**
     * GET mapping to get all relationships for a specific user
     *
     * Returns Json string & HttpStatus.OK
     * Note Json object can be empty if user has no relationships yet.
     *
     * @param listOwnerID AcctID of User
     * @return Json string & HttpStatus.OK
     */
    @GetMapping("/relationship")
    @Operation(
            summary = "Get all relationships for one user from database",
            description = "Gets a list of all users that provided user currently has a stored relationship with." +
                    "\nListOwnerID and ListOwner Password required")
    public ResponseEntity<String> getRelationships(@RequestParam("ListOwnerID") int listOwnerID){

        return relationshipsService.getRelationships(new Relationship(listOwnerID));

    }

}
