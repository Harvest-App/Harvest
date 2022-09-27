package com.example.harvest;

import com.google.firebase.firestore.Exclude;

public class LogEntry {
    private String documentID;
    private String userID;
    private String timeCreated;
    private String produceFood;
    private String produceSubtype;
    private String produceType;
    private String produceSupertype;
    private String weight;

    public LogEntry(){
        //firestore always needs a public empty constructor
    }

    public LogEntry(String userID, String produceFood, String produceSubtype,String produceType, String produceSupertype, String weight, String timeCreated){

        this.userID = userID;
        this.produceFood=produceFood;
        this.produceSubtype=produceSubtype;
        this.produceType=produceType;
        this.produceSupertype=produceSupertype;
        this.weight=weight;
        this.timeCreated = timeCreated;
    }

    @Exclude //prevents the document ID from being stored as a field in the log
    public String getDocumentID() {
        return documentID;
    }

    //getters and setters
    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    public String getTimeCreated(){
        return timeCreated;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getProduceFood() {
        return produceFood;
    }

    public void setTimeCreated(String timeCreated) {
        this.timeCreated = timeCreated;
    }

    public void setProduceFood(String produceFood) {
        this.produceFood = produceFood;
    }

    public String getProduceSubtype() {
        return produceSubtype;
    }

    public String getProduceType() {
        return produceType;
    }

    public String getProduceSupertype() {
        return produceSupertype;
    }

    public void setProduceSupertype(String produceSupertype) {
        this.produceSupertype = produceSupertype;
    }

    public void setProduceType(String produceType) {
        this.produceType = produceType;
    }

    public void setProduceSubtype(String produceSubtype) {
        this.produceSubtype = produceSubtype;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getWeight() {
        return weight;
    }
}

