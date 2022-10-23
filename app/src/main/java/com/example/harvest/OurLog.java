package com.example.harvest;

import com.google.firebase.firestore.Exclude;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class OurLog {
    private String documentID;
    private String userID;
    private String timeCreated;
    private String logName;
  //  private String friends;
    private ArrayList<String> friendsArray= new ArrayList<>();
  //  private String logID;

    public OurLog(){
        //firestore always needs a public empty constructor
    }

    public OurLog(String userID, String logName, String timeCreated){

        this.userID = userID;
        this.timeCreated = timeCreated;
        this.logName = logName;

    }
  //  @Exclude //prevents the document ID from being stored as a field in the log
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



    //getter and setter for the friends String, which will be the user IDS of the friends that have been added
    //to a log separated by a "/". Use the / as a delimiter to separate the user ids and then]
    //add the entry to the log entries for all of the users and not just the current one so the logs match
    //don't forget that when a friend is first added to a log you need to copy over all the entries.
//
//    public String getFriends() {
//        return friends;
//    }
//
//    public void setFriends(String friends) {
//        this.friends = friends;
//    }


//    public String getLogID() {
//        return logID;
//    }
//
//    public void setLogID(String logID) {
//        this.logID = logID;
//    }

   // public String getLogID(){
 //       return documentID;
   // }

//an array to store all the people who have been added to this log
    public ArrayList<String> getFriendsArray() {
        return friendsArray;
    }
//in your activity, fetch the friends array, then add to it
    public void setFriendsArray(ArrayList<String> friendsArray) {
        this.friendsArray = friendsArray;
    }
}

