package com.example.harvest;

public class OurLog {
    private String documentID;
    private String userID;
    private String timeCreated;
    private String logName;
    private String friends="";


    public OurLog(){
        //Firestore always needs a public empty constructor
    }

    public OurLog(String userID, String logName, String timeCreated){

        this.userID = userID;
        this.timeCreated = timeCreated;
        this.logName = logName;

    }

    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    public String getLogName(){
        return logName;
    }
    public String getTimeCreated(){
        return timeCreated;
    }


    public String getFriends() {
        return friends;
    }

    public void setFriends(String friends) {
        this.friends = friends;
    }


}

