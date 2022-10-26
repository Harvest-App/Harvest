package com.example.harvest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class LogEntryHome extends AppCompatActivity {

    //UI elements
    private ImageView returnHome;
    private ImageView addEntry;
    private TextView logDisplayTextView;
    private TextView heading;
    private ImageView analytics;
    private ImageView addFriend;
    ProgressDialog progressDialog;

    //Firestore database, documents, and collections
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference usersRef = db.collection("users");//what we wanna add nodes to
    private DocumentReference logRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
    private CollectionReference allLogsRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Logs");
    private String ID;
    private String logName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_entry_home);

        //fetch intent that has log information
        Intent intent = getIntent();
        if(intent!=null){
            ID=getIntent().getStringExtra("logID");
            logName = getIntent().getStringExtra("logName");
        }

        //loading image that shows while log entries are still loading
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        //heading that has log name
        heading = findViewById(R.id.logHeading);
        heading.setText(logName);

        //initialise UI elements and OnClickListeners

        //home button
        returnHome = findViewById(R.id.returnHome);

        returnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LogEntryHome.this, ProfileActivity.class));
            }
        });

        //button to add a new log entry
        addEntry =  findViewById(R.id.addLogEntry);

        addEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // startActivity(new Intent(LogEntryHome.this, CreateLogEntry.class));
               Intent i = new Intent(LogEntryHome.this, CreateLogEntry.class);
                i.putExtra("logID",ID);
                i.putExtra("logName",logName);
                startActivity(i);
            }
        });

        //button to go to analytics activity
        analytics = findViewById(R.id.logAnalytics);

        analytics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  startActivity(new Intent(LogEntryHome.this,LogAnalytics.class));
                Intent i = new Intent(LogEntryHome.this, LogAnalytics.class);
                i.putExtra("logID",ID);
                i.putExtra("logName",logName);
                startActivity(i);
            }
        });

        //button to go to AddFriends activity
        addFriend = findViewById(R.id.addFriend);

        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  startActivity(new Intent(LogEntryHome.this,LogAnalytics.class));
                Intent i = new Intent(LogEntryHome.this, AddFriends.class);
                i.putExtra("logID",ID);
                i.putExtra("logName",logName);
                startActivity(i);
            }
        });

        //textView to list logs
        logDisplayTextView = findViewById(R.id.logDisplay);

        //function that fetches log entries from Firestore
        loadLogEntries(ID);
    }

    //fetch and display log entries from Firestore, filter latest on top
    public void loadLogEntries(String logID){//fetches log entries from Firestore
        allLogsRef.document(logID).collection("Log Entries").orderBy("timeCreated", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String entryInfo="";
                        for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){

                            //QuerySnapshot is our whole collection, which has multiple document snapshots
                            //each document snapshot represents a log entry
                            LogEntry log = documentSnapshot.toObject(LogEntry.class);

                            //fetches entry info and add it to a string to add to the textview
                            String foodType = log.getProduceFood();
                            String weight = log.getWeight();
                            String timeCreated=log.getTimeCreated();
                            entryInfo+="Produce Type: "+foodType+"\n"+"Weight: "+weight+"g\n"+"Log created: "+timeCreated+"\n\n";

                        }
                        logDisplayTextView.setText(entryInfo);
                        //closes loading image
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LogEntryHome.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}