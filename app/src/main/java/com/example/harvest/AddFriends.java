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
import android.widget.ImageView;
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
    private ImageView returnToEntries;
    private ImageView returnHome;
    private Button addFriend;
    private TextView heading;
    private EditText searchBar;
    ProgressDialog progressDialog;

    //firebase information
    private String ID;
    private String logName;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private User friend;
    private User thisUser;
    private CollectionReference usersRef = db.collection("users");

    //recyclerview (list of users)
    private FriendsAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);

        //fetch the intent that has our log information
        Intent intent = getIntent();
        if(intent!=null){
            ID=getIntent().getStringExtra("logID");
            logName = getIntent().getStringExtra("logName");
        }

        //shows while information hasn't appeared yet
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Searching...");


        //initialise our UI variables so that we can alter them dynamically (viewbinding)

        heading = findViewById(R.id.heading);
        String headingText = "Add friends to "+logName;
        heading.setText(headingText);

        searchBar = findViewById(R.id.searchBar);

        //go to activity that displays log entries
        returnToEntries=findViewById(R.id.returnToLogEntries);
        returnToEntries.setOnClickListener(view -> {

            Intent i = new Intent(AddFriends.this , LogEntryHome.class);

            //pass along log information
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
     private void addFriend(){//checks if friend chosen is valid and sends them an invitation

        String newFriend = searchBar.getText().toString().trim();

         //check that a friend has been selected by checking that the search bar edit text isn't empty
         if(newFriend.isEmpty()){
             searchBar.setError("Please select a user");
             searchBar.requestFocus();
             return;
         }
         if(newFriend.equals(thisUser.getUsername())){
             searchBar.setError("Cannot add yourself to a log");
             searchBar.requestFocus();
             return;
         }


        //create an invitation to add to inviter's outbox (custom SentInvite class)
        SentInvite invite = new SentInvite(friend.getFullName(),friend.getUsername());
        invite.setStatus("Pending");
        invite.setLogID(ID);
        invite.setLogName(logName);
        invite.setPriority("2"); //the priority will later be used to order outbox items

         //create an invitation to add to person being invited's inbox (custom GotInvite class)
        GotInvite getInvite = new GotInvite(thisUser.getFullName(), thisUser.getUsername(), thisUser.getDocumentID(),FirebaseAuth.getInstance().getCurrentUser().getUid());
        getInvite.setLogID(ID);
        getInvite.setLogName(logName);
        
         //add invitation to current user's outbox
         usersRef.document(thisUser.getDocumentID()).collection("Outbox").add(invite)
                 .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                     @Override
                     public void onSuccess(DocumentReference documentReference) {
                         searchBar.getText().clear();

                         //add the invitation to other user's inbox if the invitation was successful
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

     private void fetchUID(){//method to fetch the ID of this user in Firebase - not the Firebase Auth UID.
         usersRef.whereEqualTo("id",FirebaseAuth.getInstance().getCurrentUser().getUid())
                 .get()
                 .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                     @Override
                     public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                         for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){

                             /*QuerySnapshot is our whole collection, which has multiple document snapshots
                             each document snapshot represents a log entry */
                             User user = documentSnapshot.toObject(User.class);
                             user.setDocumentID(documentSnapshot.getId());

                             //sets our thisUser variable to this user so we can use it anywhere
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

        //Firestore query that fetches the full names of the users in the users collections and orders them alphabetically
        Query query = usersRef.orderBy("fullName", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>().setQuery(query, User.class).build();

        //setting the adapter to our custom FriendsAdapter and binding it to the activity view
        myAdapter = new FriendsAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapter);

        //defines what will happen when an item in the recyclerview is clicked
        myAdapter.setOnItemClickListener(new FriendsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

                //converting from a FireStore documentSnapshot to an object of our custom User class
                friend = documentSnapshot.toObject(User.class);

                //fetching the Firestore ID of the snapshot and saving it into the object instance
                //this allows us to use the ID to find this document in Firestore
                friend.setDocumentID(documentSnapshot.getId());

                //search just adds the text of the selected friend's username to the "search bar"
                search(friend);
            }
        });
    }


    @Override
    protected void onStart() {//tells our adapter to start listening for changes
        super.onStart();
        myAdapter.startListening();

    }

    @Override
    protected void onStop() {//tells our adapter to stop listening for changes
        super.onStop();
        myAdapter.stopListening();
    }

}