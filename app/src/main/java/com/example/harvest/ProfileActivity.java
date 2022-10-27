package com.example.harvest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {


    //UI elements
    private ImageView addLog;
    private ImageView friends;
    private ImageView logOut;
    private TextView welcome;
//    ProgressDialog progressDialog;

    //firestore database, documents and collection
    private FirebaseFirestore db  = FirebaseFirestore.getInstance();;
    private CollectionReference usersRef = db.collection("users");

    private RecyclerView recyclerView;
    private ArrayList<OurLog> logArrayList;
    private RecyclerViewAdapterLog myAdapter;
    private User thisUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        //loading image that shows while user's name hasn't appeared yet
//        progressDialog = new ProgressDialog(this);
//        progressDialog.setCancelable(false);
//        progressDialog.setMessage("Loading...");
//        progressDialog.show();

        //recyclerview
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        logArrayList = new ArrayList<>();
        myAdapter = new RecyclerViewAdapterLog(ProfileActivity.this, logArrayList);
        recyclerView.setAdapter(myAdapter);

        //initialise UI elements and OnClickListeners

        //welcome textview that contains user's name
        welcome = findViewById(R.id.Welcome);

        //button to add a new log
        addLog = findViewById(R.id.addLog);
        addLog.setOnClickListener(new View.OnClickListener() {//sends to create log
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, CreateLog.class));
            }
        });

        //button to go to the invitation tracker
        friends = findViewById(R.id.friends);

        friends.setOnClickListener(new View.OnClickListener() {//logs user out
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProfileActivity.this, FriendsManager.class);
                i.putExtra("userID",thisUser.getDocumentID());
                i.putExtra("userUsername",thisUser.getUsername());
                startActivity(i);
            }
        });

        //button to log out
        logOut = findViewById(R.id.logOut);

        logOut.setOnClickListener(new View.OnClickListener() {//logs user out
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();//uses firebase auth to sign user out
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
            }
        });

        EventChangeListener();//checks for recyclerview changes

        //defines what happens when an item in the log recyclerview is clicked
        myAdapter.setOnItemClickListener(new RecyclerViewAdapterLog.OnItemClickListener() {
            //takes user to the log entries when they click on a log
            @Override
            public void onItemClick(int position) {
                String logID = logArrayList.get(position).getDocumentID();
                String logName = logArrayList.get(position).getLogName();
                Intent i = new Intent(ProfileActivity.this, LogEntryHome.class);
                i.putExtra("logID",logID);
                i.putExtra("logName",logName);
                i.putExtra("userID",thisUser.getDocumentID());
                startActivity(i);
            }
        });

        fetchUID();
    }


    private void EventChangeListener () {
        db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Logs")
                .orderBy("timeCreated", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {

                            Toast.makeText(ProfileActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                OurLog thisLog=dc.getDocument().toObject(OurLog.class);
                                thisLog.setDocumentID(dc.getDocument().getId());
                                logArrayList.add(thisLog);
                            }
                            myAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    private void getUID(User user){
        thisUser=user;
        welcome.setText(thisUser.getFullName());
//        if (progressDialog.isShowing()) {
//            progressDialog.dismiss();
//        }
    }
    private void fetchUID(){
        usersRef.whereEqualTo("id", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){

                            //QuerySnapshot is our whole collection, which has multiple document snapshots
                            //each document snapshot represents a log entry
                            User user = documentSnapshot.toObject(User.class);
                            user.setDocumentID(documentSnapshot.getId());
                            getUID(user);
                        }
                      //  Toast.makeText(ProfileActivity.this, thisUser.getUsername(), Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProfileActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

