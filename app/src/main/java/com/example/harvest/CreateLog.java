package com.example.harvest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;




public class CreateLog extends AppCompatActivity {

    //ui elements
    private EditText logName;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Button addLog;
    private Button returnHome;

    //firebase information
    private CollectionReference usersRef = db.collection("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_log);

        //initialise UI elements and relevant OnClickListeners

        logName = (EditText) findViewById(R.id.logName);
        returnHome = (Button)findViewById(R.id.returnHome);
        returnHome.setOnClickListener(view -> {
            Intent intent = new Intent(CreateLog.this , ProfileActivity.class);
            startActivity(intent);
        });

        addLog = (Button) findViewById(R.id.addLog1);
        addLog.setOnClickListener(view -> {
            createLog();
        });
    }

    void createLog(){//adds a log to Firestore with user inputted data

        String logNamed = logName.getText().toString().trim();

        //check log name isn't empty
        if (logNamed.isEmpty()){
            logName.setError("Enter log name to proceed");
            logName.requestFocus();
            return;
        }

        //fetching date to add the time the log was created
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        String timeCreated = formatter.format(date);

        //create log instance
        OurLog logs = new OurLog(FirebaseAuth.getInstance().getCurrentUser().getUid(),logName.getText().toString().trim(),timeCreated);

        //add log to firestore, connected to this specific user.
       usersRef.document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Logs").add(logs)
               .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                   @Override
                   public void onSuccess(DocumentReference documentReference) {
                       Toast.makeText(CreateLog.this, "Log added successfully", Toast.LENGTH_LONG).show();
                       logName.getText().clear();
                   }
               })
               .addOnFailureListener(new OnFailureListener() {
                   @Override
                   public void onFailure(@NonNull Exception e) {
                       Toast.makeText(CreateLog.this, "Log creation failed", Toast.LENGTH_LONG).show();
                   }
               });

    }

}