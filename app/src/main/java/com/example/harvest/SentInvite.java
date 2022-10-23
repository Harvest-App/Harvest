package com.example.harvest;

public class SentInvite {

    public String friendName, friendUsername;
    private String documentID;
    private String status;
    private String logID;
    private String logName;
    private String priority;
    //empty constructor for firestore
    public SentInvite(){

    }

    public SentInvite(String friendName, String friendUsername){
        this.friendName=friendName;
        this.friendUsername = friendUsername;
    }

    public String getFriendName() {
        return friendName;
    }

    public String getFriendUsername() {
        return friendUsername;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLogName() {
        return logName;
    }

    public void setLogName(String logName) {
        this.logName = logName;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
