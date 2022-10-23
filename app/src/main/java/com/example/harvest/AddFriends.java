package com.example.harvest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AddFriends extends AppCompatActivity {

    //UI elements
    private Button returnToEntries;
    private Button returnHome;
    private Button addFriend;
    private TextView heading;
    private EditText searchBar;

    //firebase information
    private String ID;
    private String logName;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    ProgressDialog progressDialog;
    private User friend;
    private User thisUser;
    private CollectionReference usersRef = db.collection("users");
    private CollectionReference inviteRef;


    //recyclerview (list of users)
    private RecyclerView recyclerView;
    private ArrayList<User> userArrayList;
    private FriendsAdapter myAdapter;
    private ArrayList<User> posList;// to get accurate position

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);

        Intent intent = getIntent();
        if(intent!=null){
            ID=getIntent().getStringExtra("logID");
            logName = getIntent().getStringExtra("logName");
        }

//        //shows while information hasn't appeared yet
//        progressDialog = new ProgressDialog(this);
//        progressDialog.setCancelable(false);
//        progressDialog.setMessage("Loading...");
//        progressDialog.show();

        heading = findViewById(R.id.heading);
        String headingText = "Add friends to "+logName;
        heading.setText(headingText);


        searchBar = findViewById(R.id.searchBar);
//
//
//
//        recyclerView = findViewById(R.id.recyclerView);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        userArrayList = new ArrayList<>();
//        myAdapter = new FriendsAdapter(AddFriends.this, userArrayList);
//        recyclerView.setAdapter(myAdapter);
//
//        EventChangeListener();
        //go to activity that displays log entries
        returnToEntries=findViewById(R.id.returnToLogEntries);
        returnToEntries.setOnClickListener(view -> {

            Intent i = new Intent(AddFriends.this , LogEntryHome.class);
            i.putExtra("logID",ID);
            i.putExtra("logName",logName);
            startActivity(i);
        });

        //go to profile
        returnHome = findViewById(R.id.returnHome);
        returnHome.setOnClickListener(view -> {
            Intent i = new Intent(AddFriends.this , ProfileActivity.class);
            startActivity(i);
        });

        //button to send the invitation to a friend
        addFriend = findViewById(R.id.addFriend);

        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFriend();
            }
        });


setUpRecyclerView();
fetchUID();
    }
     private void search(User newFriend){
         searchBar.setText(newFriend.getUsername());

     }
     private void addFriend(){
        String newFriend = searchBar.getText().toString().trim();

        //check that a friend has been selected by checking that the search bar edit text isn't empty
        if(newFriend.isEmpty()){
            searchBar.setError("Please select a user");
            searchBar.requestFocus();
            return;
        }

        if(friend.getUsername().isEmpty()){
            searchBar.setError("Please click on the user you wish to add");
            searchBar.requestFocus();
            return;
        }

        //create an invitation to add to inviter's outbox
        SentInvite invite = new SentInvite(friend.getFullName(),friend.getUsername());
        invite.setStatus("Pending");
        invite.setLogID(ID);
        invite.setLogName(logName);
        invite.setPriority("2");
         //create an invitation to add to person being invited's inbox
        GotInvite getInvite = new GotInvite(thisUser.getFullName(), thisUser.getUsername(), thisUser.getDocumentID(),FirebaseAuth.getInstance().getCurrentUser().getUid());
        getInvite.setLogID(ID);
        getInvite.setLogName(logName);
        
        
        
         //add invitation to current user's outbox
         usersRef.document(thisUser.getDocumentID()).collection("Outbox").add(invite)
                 .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                     @Override
                     public void onSuccess(DocumentReference documentReference) {
                      //   Toast.makeText(AddFriends.this, "Invitation in outbox", Toast.LENGTH_LONG).show();
                         searchBar.getText().clear();

                         //add to other user's inbox if the invitation was successful

                         //add to other user's inbox if the invitation was successful
                         usersRef.document(friend.getDocumentID()).collection("Inbox").add(getInvite)
                                 .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                     @Override
                                     public void onSuccess(DocumentReference documentReference) {
                                         Toast.makeText(AddFriends.this, "Invitation sent successfully", Toast.LENGTH_LONG).show();
                                         searchBar.getText().clear();
                                     }
                                 })
                                 .addOnFailureListener(new OnFailureListener() {
                                     @Override
                                     public void onFailure(@NonNull Exception e) {
                                         Toast.makeText(AddFriends.this, "Invitation failed", Toast.LENGTH_LONG).show();
                                         searchBar.getText().clear();
                                     }
                                 });
                     }
                 })
                 .addOnFailureListener(new OnFailureListener() {
                     @Override
                     public void onFailure(@NonNull Exception e) {
                         Toast.makeText(AddFriends.this, "Invitation failed", Toast.LENGTH_LONG).show();
                         searchBar.getText().clear();
                     }
                 });
     }
    private void getUID(User user){
        thisUser=user;
    }
     private void fetchUID(){
         usersRef.whereEqualTo("id",FirebaseAuth.getInstance().getCurrentUser().getUid())
                 .get()
                 .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                     @Override
                     public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                         String entryInfo="";
                         for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){

                             //QuerySnapshot is our whole collection, which has multiple document snapshots
                             //each document snapshot represents a log entry
                             User user = documentSnapshot.toObject(User.class);
                             user.setDocumentID(documentSnapshot.getId());
                             getUID(user);
                         }

                     }
                 }).addOnFailureListener(new OnFailureListener() {
                     @Override
                     public void onFailure(@NonNull Exception e) {
                         Toast.makeText(AddFriends.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                     }
                 });
     }
    private void setUpRecyclerView(){
        Query query = usersRef.orderBy("fullName", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>().setQuery(query, User.class).build();

        myAdapter = new FriendsAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapter);



        myAdapter.setOnItemClickListener(new FriendsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                friend = documentSnapshot.toObject(User.class);
                friend.setDocumentID(documentSnapshot.getId());
               // String id = documentSnapshot.getId();
               // String path = documentSnapshot.getReference().getPath();
                search(friend);
//
                Toast.makeText(AddFriends.this,
                        "Position: " + position + " ID: " + friend.getDocumentID(), Toast.LENGTH_SHORT).show();
                Toast.makeText(AddFriends.this,
                        "First username is "+myAdapter.getSnapshots().get(0).getUsername(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        myAdapter.startListening();

    }

    @Override
    protected void onStop() {
        super.onStop();
        myAdapter.stopListening();
    }

}