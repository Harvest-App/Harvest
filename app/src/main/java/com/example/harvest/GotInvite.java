package com.example.harvest;

public class GotInvite {


    public String senderName, senderUsername;
    private String senderUID;
    private String documentID;
    private String logID;
    private String logName;
    private String senderFirebaseUID;
    //empty constructor for firestore
    public GotInvite(){

    }

    public GotInvite(String senderName, String senderUsername, String SenderUid, String senderFirebaseUID){
        this.senderName=senderName;
        this.senderUsername = senderUsername;
        this.senderUID=SenderUid;
        this.senderFirebaseUID=senderFirebaseUID;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public String getSenderUID() {
        return senderUID;
    }

    public void setSenderUID(String senderUID) {
        this.senderUID = senderUID;
    }

    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    public String getLogID() {
        return logID;
    }

    public void setLogID(String logID) {
        this.logID = logID;
    }

    public String getLogName() {
        return logName;
    }

    public void setLogName(String logName) {
        this.logName = logName;
    }

    public String getSenderFirebaseUID() {
        return senderFirebaseUID;
    }

    public void setSenderFirebaseUID(String senderFirebaseUID) {
        this.senderFirebaseUID = senderFirebaseUID;
    }
}
