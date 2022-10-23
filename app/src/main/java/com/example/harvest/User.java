package com.example.harvest;

public class User {

    public String fullName, username, email;
    private String documentID;
    public User(){

    }

    public User(String fullName, String username, String email){
        this.fullName=fullName;
        this.username = username;
        this.email= email;

    }
    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
