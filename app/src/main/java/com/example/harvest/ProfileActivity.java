package com.example.harvest;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {


    //UI elements
    private Button logOut;
    private Button addLog;
    private TextView heading;

    //firestore database, documents and collection
    private FirebaseFirestore db;

    private RecyclerView recyclerView;
    private ArrayList<OurLog> logArrayList;
    private RecyclerViewAdapterLog myAdapter;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //shows while information hasn't appeared yet
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        //recyclerview
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        logArrayList = new ArrayList<>();
        myAdapter = new RecyclerViewAdapterLog(ProfileActivity.this, logArrayList);
        recyclerView.setAdapter(myAdapter);

        //firebase
        db = FirebaseFirestore.getInstance();

        //initialise UI elements and OnClickListeners
        heading = findViewById(R.id.logHeading);
        logOut = findViewById(R.id.logOut);

        logOut.setOnClickListener(new View.OnClickListener() {//logs user out
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();//uses firebase auth to sign user out
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
            }
        });
        addLog = findViewById(R.id.addLog);
        addLog.setOnClickListener(new View.OnClickListener() {//sends to create log
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, CreateLog.class));
            }
        });

        EventChangeListener();//checks for recyclerview changes

        myAdapter.setOnItemClickListener(new RecyclerViewAdapterLog.OnItemClickListener() {
            //takes user to the log entries when they click on a log
            @Override
            public void onItemClick(int position) {
                Intent i = new Intent(ProfileActivity.this, LogEntryHome.class);
                startActivity(i);

            }
        });
    }


    private void EventChangeListener () {
        db.collection("users").document(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).collection("Logs")
                .orderBy("timeCreated", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            Toast.makeText(ProfileActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                OurLog thisLog=dc.getDocument().toObject(OurLog.class);
                                logArrayList.add(thisLog);
                            }
                            myAdapter.notifyDataSetChanged();
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                        }
                    }
                });
    }
}

