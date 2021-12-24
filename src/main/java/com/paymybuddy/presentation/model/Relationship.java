package com.paymybuddy.presentation.model;

import com.paymybuddy.presentation.apimodels.RelationshipDTO;

public class Relationship {
    private int listID;
    private int listOwnerID;
    private int friendID;

    public Relationship() {
    }

    public Relationship(RelationshipDTO relationshipDTO) {
        this.listID = 0;
        this.listOwnerID = relationshipDTO.listOwnerID;
        this.friendID = relationshipDTO.friendID;
    }

    public Relationship(int listOwnerID, int friendID) {
        this.listOwnerID = listOwnerID;
        this.friendID = friendID;
    }

    public Relationship(int listOwnerID) {
        this.listOwnerID = listOwnerID;
    }

    public Relationship(int listID, int listOwnerID, int friendID) {
        this.listID = listID;
        this.listOwnerID = listOwnerID;
        this.friendID = friendID;
    }


    public int getListID() {
        return listID;
    }

    public void setListID(int listID) {
        this.listID = listID;
    }

    public int getListOwnerID() {
        return listOwnerID;
    }

    public void setListOwnerID(int listOwnerID) {
        this.listOwnerID = listOwnerID;
    }

    public int getFriendID() {
        return friendID;
    }

    public void setFriendID(int friendID) {
        this.friendID = friendID;
    }

}
