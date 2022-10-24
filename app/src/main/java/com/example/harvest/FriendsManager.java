package com.example.harvest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firestore.v1.WriteResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FriendsManager extends AppCompatActivity {

    //buttons
    private Button returnHome;

    //firebase information

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private GotInvite inviter;
    private CollectionReference usersRef = db.collection("users");
    private String userID;
    private String userUsername;
    private String thisUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

    //recyclerview (list of users)
    private InboxAdapter inAdapter;
    private OutboxAdapter outAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_manager);

        //fetch intent that provides userID and username information
        Intent userInfo = getIntent();
        if(userInfo!=null){
            userID= userInfo.getStringExtra("userID");
            userUsername= userInfo.getStringExtra("userUsername");
        }

        //send user to profile when they click return home button
        returnHome=findViewById(R.id.returnHome);
        returnHome.setOnClickListener(view -> {
            Intent i = new Intent(FriendsManager.this , ProfileActivity.class);
            startActivity(i);
        });

        //set up recyclerviews
        setUpInbox();
        setUpOutbox();
    }

    private void setUpInbox(){//fetches inbox sub-collection and displays in recyclerview

        //Firestore query to fetch all items in Inbox sub-collection
        Query query = usersRef.document(userID).collection("Inbox");
        FirestoreRecyclerOptions<GotInvite> options = new FirestoreRecyclerOptions.Builder<GotInvite>().setQuery(query, GotInvite.class).build();

        //set recyclerview adapter to be custom inAdapter (adapter for inbox)
        inAdapter = new InboxAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.inboxRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(inAdapter);


        //defines what happens when an inbox item is clicked
        inAdapter.setOnItemClickListener(new InboxAdapter.OnItemClickListener() {
            @Override

            //clicking thumbs up to accept invitation
            public void onAcceptClick(DocumentSnapshot documentSnapshot, int position) {

                //fetching the information of the clicked item from Firestore via documentSnapshot
                inviter = documentSnapshot.toObject(GotInvite.class);
                inviter.setDocumentID(documentSnapshot.getId());
                String inviterID = inviter.getSenderUID();
                String inviterFirebaseUID = inviter.getSenderFirebaseUID();
                String logID = inviter.getLogID();

                Toast.makeText(FriendsManager.this,
                        "You accepted an invitation from "+ inAdapter.getSnapshots().get(position).getSenderUsername(), Toast.LENGTH_SHORT).show();

               /*updating status of invitation from "Pending" to "Accepted" in the outbox of the user
               who sent the invitation
                */
               changeStatus("Accepted",inviterID,logID,userUsername);

                /*add this user's uid to the friend's array for the inviter and copy log to this user
                NOTE: THIS ONLY ALLOWS ONE PERSON TO BE ADDED.
                TO ADD MORE, YOU NEED TO UPDATE THE FRIENDS ARRAYS OF ALL THE FRIEND'S IN THE INVITER'S FRIENDS LIST
                */

                usersRef.document(inviterFirebaseUID).collection("Logs")
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){

                                    /*QuerySnapshot is our whole collection, which has multiple document snapshots
                                    each document snapshot represents a log
                                     */

                                    //casting documentSnapshot to custom log object and setting the ID
                                    OurLog log = documentSnapshot.toObject(OurLog.class);
                                    log.setDocumentID(documentSnapshot.getId());

                                    if(log.getDocumentID().equals(logID)){//checks if this is the log we want

                                        //fetching friendsList string from the log and adding the new friend
                                        String friendsList = log.getFriends();

                                        if(friendsList.equals("")){
                                            friendsList+=thisUID;
                                        }
                                        else{
                                            friendsList+="/"+thisUID;
                                        }

                                        //updating the friends field of the log in Firestore
                                        Map<String, Object> data = new HashMap<>();
                                        data.put("friends", friendsList);
                                        usersRef.document(inviterFirebaseUID).collection("Logs").document(logID)
                                                .set(data, SetOptions.merge());

                                        //create the log that the user is being added to in this user's Logs collection
                                        copyLog(log,logID,inviterFirebaseUID);

                                    }
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(FriendsManager.this, "Adding friend to log failed", Toast.LENGTH_SHORT).show();

                            }
                        });

                //copy the log entries to the new log
                usersRef.document(inviterFirebaseUID).collection("Logs").document(logID).collection("Log Entries")
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){

                                    //QuerySnapshot is our whole collection, which has multiple document snapshots
                                    //each document snapshot represents a log entry
                                    LogEntry entry = documentSnapshot.toObject(LogEntry.class);

                                    //copy log entry
                                    copyLogEntry(entry,logID);

                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(FriendsManager.this, "Adding friend to log failed", Toast.LENGTH_SHORT).show();

                            }
                        });



                //delete the invitation from Firestore to remove it from the user's inbox
                usersRef.document(userID).collection("Inbox").document(inviter.getDocumentID())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(FriendsManager.this,
                                        "Error accepting invitation from "+ inAdapter.getSnapshots().get(position).getSenderUsername(), Toast.LENGTH_SHORT).show();
                            }
                        });

            }


            @Override
            public void onRejectClick(DocumentSnapshot documentSnapshot, int position) {

                //fetching information of clicked item from Firestore
                inviter = documentSnapshot.toObject(GotInvite.class);
                inviter.setDocumentID(documentSnapshot.getId());
                String inviterID = inviter.getSenderUID();
                String logID = inviter.getLogID();

                Toast.makeText(FriendsManager.this,
                        "You rejected an invitation from "+ inAdapter.getSnapshots().get(position).getSenderUsername(),
                        Toast.LENGTH_SHORT).show();

                //changing the status of the invitation to "Rejected" in the inviter's inbox
                changeStatus("Rejected",inviterID,logID,userUsername);

                //deleting the invitation from the user's inbox
                usersRef.document(userID).collection("Inbox").document(inviter.getDocumentID())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(FriendsManager.this,
                                        "Error deleting invitation from "+ inAdapter.getSnapshots().get(position).getSenderUsername()+" from inbox", Toast.LENGTH_SHORT).show();
                            }
                        });
            }

        });
    }

    private void copyLog(OurLog log, String logID, String inviterFirebaseUID){
        usersRef.document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Logs").document(log.getDocumentID()).set(log);

        String friendsList = log.getFriends();
        if(friendsList.equals("")){
            friendsList+=inviterFirebaseUID;
        }
        else{
            friendsList+="/"+inviterFirebaseUID;
        }
        Map<String, Object> data = new HashMap<>();
        data.put("friends", friendsList);

        usersRef.document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Logs").document(logID)
                .set(data, SetOptions.merge());
    }

    private void copyLogEntry(LogEntry entry, String logID){
        usersRef.document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Logs").document(logID).collection("Log Entries").add(entry);
    }

   // this method edits the status of an invitation in the inviter's outbox upon being accepted or rejected
    private void changeStatus(String status, String inviterID,String logID,String friendUsername){

        /* This priority code will be implemented later as a way of ordering outbox items.
        String priority = "1";
        if(status.equals("Accepted")){
            priority="10";
        }
        if(status.equals("Rejected")){
            priority="3";
        }
        String finalPriority = priority;
         */

        String finalStatus = status;
        usersRef.document(inviterID).collection("Outbox").whereEqualTo("logID",logID).whereEqualTo("friendUsername",friendUsername)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){

                            //QuerySnapshot is our whole collection, which has multiple document snapshots
                            //each document snapshot represents a log entry
                            SentInvite sent = documentSnapshot.toObject(SentInvite.class);
                            sent.setDocumentID(documentSnapshot.getId());

                            //edit the status field of the outbox item
                            Map<String, Object> data = new HashMap<>();
                            data.put("status", finalStatus);
                            usersRef.document(inviterID).collection("Outbox").document(sent.getDocumentID())
                                    .set(data, SetOptions.merge());
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(FriendsManager.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setUpOutbox(){

        Query query = usersRef.document(userID).collection("Outbox");
        FirestoreRecyclerOptions<SentInvite> options = new FirestoreRecyclerOptions.Builder<SentInvite>().setQuery(query, SentInvite.class).build();

        outAdapter = new OutboxAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.outboxRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(outAdapter);

    }
    @Override
    protected void onStart() {
        super.onStart();
        inAdapter.startListening();
        outAdapter.startListening();

    }

    @Override
    protected void onStop() {
        super.onStop();
        inAdapter.stopListening();
        outAdapter.stopListening();
    }

}