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
import com.google.firestore.v1.WriteResult;

import java.util.ArrayList;

public class FriendsManager extends AppCompatActivity {

    //buttons
    private Button returnHome;

    //firebase information

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private GotInvite inviter;
    // private User thisUser;
    private CollectionReference usersRef = db.collection("users");
    private ArrayList<User> thisUser;
    private String ID;
    private String logName;
    private String userID;
    private String userUsername;

    //recyclerview (list of users)
    private InboxAdapter inAdapter;
    private OutboxAdapter outAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_manager);

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

        setUpInbox();
        setUpOutbox();
    }

    private void setUpInbox(){

        Query query = usersRef.document(userID).collection("Inbox");
        FirestoreRecyclerOptions<GotInvite> options = new FirestoreRecyclerOptions.Builder<GotInvite>().setQuery(query, GotInvite.class).build();

        inAdapter = new InboxAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.inboxRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(inAdapter);

        inAdapter.setOnItemClickListener(new InboxAdapter.OnItemClickListener() {
            @Override
            public void onAcceptClick(DocumentSnapshot documentSnapshot, int position) {
                inviter = documentSnapshot.toObject(GotInvite.class);
                inviter.setDocumentID(documentSnapshot.getId());
                String inviterID = inviter.getSenderUID();
                String inviterFirebaseUID = inviter.getSenderFirebaseUID();
                String logID = inviter.getLogID();
                Toast.makeText(FriendsManager.this,
                        "You accepted an invitation from "+ inAdapter.getSnapshots().get(position).getSenderUsername(), Toast.LENGTH_SHORT).show();

               //updating status of invitation from pending to accepted (doesn't work)
               // changeStatus("Accepted",inviterID,logID,userUsername);

                //NOTE: THIS ONLY ALLOWS ONE PERSON TO BE ADDED.
                //TO ADD MORE, YOU NEED TO UPDATE THE FRIENDS ARRAYS OF ALL THE FRIEND'S IN THE INVITER'S FRIENDS LIST

                //add this user's uid to the friend's array for the other user and copy log to this user
                usersRef.document(inviterFirebaseUID).collection("Logs")
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){

                                    //QuerySnapshot is our whole collection, which has multiple document snapshots
                                    //each document snapshot represents a log entry
                                    OurLog log = documentSnapshot.toObject(OurLog.class);
                                    log.setDocumentID(documentSnapshot.getId());
                                    if(log.getDocumentID().equals(logID)){

                                        //create the log that the user is being added to in this user's Logs collection
                                        copyLog(log);

                                        //add user ID to friendsArray
                                        ArrayList<String> friendsArray = log.getFriendsArray();
                                        friendsArray.add(userID);
                                        log.setFriendsArray(friendsArray);
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


                //add the inviters's uid to the friend's array for this user
                usersRef.document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Logs")
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){

                                    //QuerySnapshot is our whole collection, which has multiple document snapshots
                                    //each document snapshot represents a log entry
                                    OurLog log = documentSnapshot.toObject(OurLog.class);
                                    log.setDocumentID(documentSnapshot.getId());
                                    if(log.getDocumentID().equals(logID)){
                                        ArrayList<String> friendsArray = log.getFriendsArray();
                                        friendsArray.add(inviterID);
                                        log.setFriendsArray(friendsArray);
                                    }
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(FriendsManager.this, "Adding inviter's uid to new log failed", Toast.LENGTH_SHORT).show();

                            }
                        });

                //delete the invitation from the recyclerview once

           //     usersRef.document(userID).collection("Inbox").document(inviter.getDocumentID()).
//                usersRef.document(userID).collection("Inbox").document(inviter.getDocumentID())
//                        .delete()
//                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Toast.makeText(FriendsManager.this,
//                                        "Error accepting invitation from "+ inAdapter.getSnapshots().get(position).getSenderUsername(), Toast.LENGTH_SHORT).show();
//                            }
//                        });

            }


            @Override
            public void onRejectClick(DocumentSnapshot documentSnapshot, int position) {
                inviter = documentSnapshot.toObject(GotInvite.class);
                inviter.setDocumentID(documentSnapshot.getId());
                String inviterID = inviter.getSenderUID();
                String logID = inviter.getLogID();
                Toast.makeText(FriendsManager.this,
                        "You rejected an invitation from "+ inAdapter.getSnapshots().get(position).getSenderUsername(),
                        Toast.LENGTH_SHORT).show();

                //changing the status of the invitation to "Rejected" on the sender's end (doesn't work)
               // changeStatus("Rejected",inviterID,logID,userUsername);


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
                                        "Error rejecting invitation from "+ inAdapter.getSnapshots().get(position).getSenderUsername(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }

        });
    }

    private void copyLog(OurLog log){
        usersRef.document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Logs").document(log.getDocumentID()).set(log);
    }

    private void copyLogEntry(LogEntry entry, String logID){
        usersRef.document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Logs").document(logID).collection("Log Entries").add(entry);
    }

//    this method currently doesn't actually update the status :(
//    private void changeStatus(String status, String inviterID,String logID,String friendUsername){
//        String priority = "1";
//        if(status.equals("Accepted")){
//            priority="10";
//        }
//        if(status.equals("Rejected")){
//            priority="3";
//        }
//        String finalPriority = priority;
//        String finalStatus = status;
//        usersRef.document(inviterID).collection("Outbox").whereEqualTo("logID",logID).whereEqualTo("friendUsername",friendUsername)
//                .get()
//                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//
//                        for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
//
//                            //QuerySnapshot is our whole collection, which has multiple document snapshots
//                            //each document snapshot represents a log entry
//                            SentInvite sent = documentSnapshot.toObject(SentInvite.class);
//                            sent.setDocumentID(documentSnapshot.getId());
//                            sent.setPriority(finalPriority);
//                            sent.setPriority(finalStatus);
//                            //ApiFuture<WriteResult> future = usersRef.document(inviterID).collection("Outbox").document(sent.getDocumentID()).update("status", status);
//                            Toast.makeText(FriendsManager.this, "Changed status of "+inviterID, Toast.LENGTH_SHORT).show();
//                        }
//                        //  Toast.makeText(ProfileActivity.this, thisUser.getUsername(), Toast.LENGTH_SHORT).show();
//
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(FriendsManager.this, "Something went wrong", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }

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