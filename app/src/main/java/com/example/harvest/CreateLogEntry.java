package com.example.harvest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.util.Log;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class CreateLogEntry extends AppCompatActivity {

    //UI elements
    private EditText produceET;
    private EditText weightET;
    private Button addLogEntry;
    private Button returnHome;
    private Button seeEntries;
    private ProduceItem produceItem;

    //Firestore database and paths
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference usersRef = db.collection("users");
    private String ID;
    private String logName;

    //recyclerview
    private RecyclerView mRecyclerView;
    private RecycleViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<ProduceItem> mProduceList;
    private ArrayList<ProduceItem> posList;// to get accurate position

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_log_entry);

        Intent logIdIntent = getIntent();
        if(logIdIntent!=null){
            ID=getIntent().getStringExtra("logID");
            logName = getIntent().getStringExtra("logName");
        }

        createProduceList();
        buildRecyclerView();

        //initialise UI elements
        produceET = findViewById(R.id.produceET);
        weightET = findViewById(R.id.weightET);

        produceET.addTextChangedListener(new TextWatcher() {//adding filter functionalities
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());//filters the recyclerview results when something has been typed

            }
        });


        //go to activity that displays log entries
        seeEntries=findViewById(R.id.goToLogEntries);
        seeEntries.setOnClickListener(view -> {

            Intent i = new Intent(CreateLogEntry.this , LogEntryHome.class);
            i.putExtra("logID",ID);
            i.putExtra("logName",logName);
            startActivity(i);
        });

        //go to profile
        returnHome = findViewById(R.id.returnHome);
        returnHome.setOnClickListener(view -> {
            Intent intent = new Intent(CreateLogEntry.this , ProfileActivity.class);
            startActivity(intent);
        });

        //add log entry to Firestore
        addLogEntry = findViewById(R.id.addLogEntry);
        addLogEntry.setOnClickListener(view -> {
            createLogEntry();
        });
    }

    void createLogEntry(){
        String produce = produceET.getText().toString().trim();
        String weight = weightET.getText().toString().trim();

        //make sure fields aren't empty
        if (produce.isEmpty()){
            produceET.setError("Enter produce type");
            produceET.requestFocus();
            return;
        }

        if (weight.isEmpty()){
            weightET.setError("Enter produce weight");
            weightET.requestFocus();
            return;
        }

        //fetch time
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        String timeCreated = formatter.format(date);

        for (ProduceItem item : mProduceList) {//match the info in the edit text to a produce item
            if(item.getFoodType().equals(produce)){
                produceItem=item;
            }
        }

        //create log entry instance
        LogEntry entry = new LogEntry(FirebaseAuth.getInstance().getCurrentUser().getUid(),produceItem.getFoodType(),produceItem.getSubType(),produceItem.getType(),produceItem.getSuperType(),weight,timeCreated);
      //  String ID = (usersRef.document(FirebaseAuth.getInstance().getCurrentUser().getUid())).getId();

        //add log entry to Firestore
        usersRef.document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Logs").document(ID).collection("Log Entries").add(entry)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(CreateLogEntry.this, "Log entry added successfully", Toast.LENGTH_LONG).show();
                        produceET.getText().clear();
                        weightET.getText().clear();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CreateLogEntry.this, "Log entry creation failed", Toast.LENGTH_LONG).show();
                    }
                });

    }

    private void filter(String text) {//changes recyclerview according to search bar
        ArrayList<ProduceItem> filteredList = new ArrayList<>();

        for (ProduceItem item : mProduceList) {
            if (item.getFoodType().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        mAdapter.filterList(filteredList);
        posList=filteredList;
    }

    private void createProduceList() {
        mProduceList = new ArrayList<>();
        mProduceList.add(new ProduceItem("Almond","Almond","Nut","Fruit"));
        mProduceList.add(new ProduceItem("Apple (Golden Delicious)","Apple","Pome Fruit","Fruit"));
        mProduceList.add(new ProduceItem("Apple (Granny Smith)", "Apple", "Pome Fruit", "Fruit"));
        mProduceList.add(new ProduceItem("Artichoke", "Artichoke", "Flower", "Flower"));
        mProduceList.add(new ProduceItem("Aubergine", "Aubergine", "Nightshade", "Vegetable"));
        mProduceList.add(new ProduceItem("Basil", "Basil", "Soft Herb", "Herb"));
        mProduceList.add(new ProduceItem("Bean (Broad)", "Bean", "Legume", "Vegetable"));
        mProduceList.add(new ProduceItem("Bean (Flat)", "Bean", "Legume", "Vegetable"));
        mProduceList.add(new ProduceItem("Bean (Green)", "Bean", "Legume", "Vegetable"));
        mProduceList.add(new ProduceItem("Bean (Yellow)", "Bean", "Legume", "Vegetable"));
        mProduceList.add(new ProduceItem("Bean (Black)", "Bean", "Legume", "Vegetable"));
        mProduceList.add(new ProduceItem("Bean (Black-Eyed)", "Bean", "Legume", "Vegetable"));
        mProduceList.add(new ProduceItem("Bean (Purple)", "Bean", "Legume", "Vegetable"));
        mProduceList.add(new ProduceItem("Beetroot", "Beetroot", "Root", "Vegetable"));
        mProduceList.add(new ProduceItem("Blackberry", "Blackberry", "Berry", "Fruit"));
        mProduceList.add(new ProduceItem("Blueberry", "Blueberry", "Berry", "Fruit"));
        mProduceList.add(new ProduceItem("Broccoli", "Broccoli", "Cruciferous", "Vegetable"));
        mProduceList.add(new ProduceItem("Butternut Squash", "Squash", "Cruciferous", "Vegetable"));
        mProduceList.add(new ProduceItem("Cabbage (Chinese)", "Cabbage", "Cruciferous", "Vegetable"));
        mProduceList.add(new ProduceItem("Cabbage (Purple)", "Cabbage", "Cruciferous", "Vegetable"));
        mProduceList.add(new ProduceItem("Carrot", "Carrot", "Root", "Vegetable"));
        mProduceList.add(new ProduceItem("Cauliflower", "Cauliflower", "Cruciferous", "Vegetable"));
        mProduceList.add(new ProduceItem("Cavolo Nero", "Kale", "Cruciferous", "Vegetable"));
        mProduceList.add(new ProduceItem("Celery", "Celery", "Stalk", "Vegetable"));
        mProduceList.add(new ProduceItem("Chilli (Birdseye)", "Chilli", "Nightshade", "Vegetable"));
        mProduceList.add(new ProduceItem("Chilli (Serrano)", "Chilli", "Nightshade", "Vegetable"));
        mProduceList.add(new ProduceItem("Chive", "Chive", "Allium", "Vegetable"));
        mProduceList.add(new ProduceItem("Coriander", "Coriander", "Soft Herb", "Herb"));
        mProduceList.add(new ProduceItem("Cucumber", "Cucumber", "Cucurbit", "Vegetable"));
        mProduceList.add(new ProduceItem("Curry Leaf", "Curry Leaf", "Woody Herb","Herb"));
        mProduceList.add(new ProduceItem("Edible Flower", "Edible Flower", "Flower", "Flower"));
        mProduceList.add(new ProduceItem("Fennel", "Fennel", "Soft Herb", "Herb"));
        mProduceList.add(new ProduceItem("Fig (Green)", "Fig", "False Fruit", "Fruit"));
        mProduceList.add(new ProduceItem("Fig (Purple)", "Fig", "False Fruit", "Fruit"));
        mProduceList.add(new ProduceItem("Gem Squash", "Squash", "Cucurbit", "Vegetable"));
        mProduceList.add(new ProduceItem("Ginger", "Ginger", "Root", "Vegetable"));
        mProduceList.add(new ProduceItem("Gooseberry", "Gooseberry", "Berry", "Fruit"));
        mProduceList.add(new ProduceItem("Granadilla", "Granadilla","Vine Fruit","Fruit"));
        mProduceList.add(new ProduceItem("Grape (Catawba)","Grape","Vine Fruit","Fruit"));
        mProduceList.add(new ProduceItem("Grape (Hanepoot)","Grape","Vine Fruit","Fruit"));
        mProduceList.add(new ProduceItem("Grape (Victoria)","Grape","Vine Fruit","Fruit"));
        mProduceList.add(new ProduceItem("Grapefruit (Ruby)","Grapefruit","Citrus","Fruit"));
        mProduceList.add(new ProduceItem("Jerusalem Artichoke","Jerusalem Artichoke","Root","Vegetable"));
        mProduceList.add(new ProduceItem("Kale","Kale","Cruciferous","Vegetable"));
        mProduceList.add(new ProduceItem("Kei Apple","Kei Apple","Berry","Fruit"));
        mProduceList.add(new ProduceItem("Leek","Leek","Allium","Vegetable"));
        mProduceList.add(new ProduceItem("Lemon","Lemon","Citrus","Fruit"));
        mProduceList.add(new ProduceItem("Lemon Balm","Lemon Balm","Soft Herb","Herb"));
        mProduceList.add(new ProduceItem("Lemon Verbena","Lemon Verbena","Hard Herb","Herb"));
        mProduceList.add(new ProduceItem("Lettuce","Lettuce","Leaf","Vegetable"));
        mProduceList.add(new ProduceItem("Lime","Lime","Citrus","Fruit"));
        mProduceList.add(new ProduceItem("Marjoram","Marjoram","Hard Herb","Herb"));
        mProduceList.add(new ProduceItem("Mint","Mint","Soft Herb","Herb"));
        mProduceList.add(new ProduceItem("Mustard Leaf","Mustard Leaf","Leaf","Vegetable"));
        mProduceList.add(new ProduceItem("Naartjie","Naartjie","Citrus","Fruit"));
        mProduceList.add(new ProduceItem("Nasturtium","Nasturtium","Flower","Flower"));
        mProduceList.add(new ProduceItem("Onion (Red)","Onion","Allium","Vegetable"));
        mProduceList.add(new ProduceItem("Onion (White)","Onion","Allium","Vegetable"));
        mProduceList.add(new ProduceItem("Orange (Cara Cara)","Orange","Citrus","Fruit"));
        mProduceList.add(new ProduceItem("Orange (Valencia)","Orange","Citrus","Fruit"));
        mProduceList.add(new ProduceItem("Oregano","Oregano","Woody Herb","Herb"));
        mProduceList.add(new ProduceItem("Parsley","Parsley","Soft Herb","Herb"));
        mProduceList.add(new ProduceItem("Pea","Pea","Legume","Vegetable"));
        mProduceList.add(new ProduceItem("Peach (White)","Peach","Stone Fruit","Fruit"));
        mProduceList.add(new ProduceItem("Peach (Yellow)","Peach","Stone Fruit","Fruit"));
        mProduceList.add(new ProduceItem("Pepper (Green California Wonder)","Pepper","Nightshade","Vegetable"));
        mProduceList.add(new ProduceItem("Pepper (Red Santorini)","Pepper","Nightshade","Vegetable"));
        mProduceList.add(new ProduceItem("Plum (Yellow)","Plum","Stone Fruit","Fruit"));
        mProduceList.add(new ProduceItem("Plum (Red)","Plum","Stone Fruit","Fruit"));
        mProduceList.add(new ProduceItem("Plum (Purple)","Plum","Stone Fruit","Fruit"));
        mProduceList.add(new ProduceItem("Plum (Purple Leaf)","Plum","Stone Fruit","Fruit"));
        mProduceList.add(new ProduceItem("Pumpkin (Boerpampoen)","Pumpkin","Cucurbit","Vegetable"));
        mProduceList.add(new ProduceItem("Pumpkin (Queensland Blue)","Pumpkin","Cucurbit","Vegetable"));
        mProduceList.add(new ProduceItem("Radish","Radish","Root","Vegetable"));
        mProduceList.add(new ProduceItem("Rhubarb","Rhubarb","Stalk","Vegetable"));
        mProduceList.add(new ProduceItem("Rosemary","Rosemary","Hard Herb","Herb"));
        mProduceList.add(new ProduceItem("Sage","Sage","Hard Herb","Herb"));
        mProduceList.add(new ProduceItem("Shallot","Onion","Leaf","Vegetable"));
        mProduceList.add(new ProduceItem("Sorrel","Sorrel","Leaf","Vegetable"));
        mProduceList.add(new ProduceItem("Spinach","Spinach","Cucurbit","Vegetable"));
        mProduceList.add(new ProduceItem("Strawberry","Strawberry","Berry","Fruit"));
        mProduceList.add(new ProduceItem("Sunflower Seed","Sunflower Seed","Seed","Flower"));
        mProduceList.add(new ProduceItem("Sweet Potato (White)","Sweet Potato","Root","Vegetable"));
        mProduceList.add(new ProduceItem("Sweet Potato (Orange)","Sweet Potato","Root","Vegetable"));
        mProduceList.add(new ProduceItem("Thyme","Thyme","Hard Herb","Herb"));
        mProduceList.add(new ProduceItem("Tomato (Cherry)","Tomato","Nightshade","Vegetable"));
        mProduceList.add(new ProduceItem("Tomato (Costoluto)","Tomato","Nightshade","Vegetable"));
        mProduceList.add(new ProduceItem("Tomato (Floradade)","Tomato","Nightshade","Vegetable"));
        mProduceList.add(new ProduceItem("Tomato (Moneymaker)","Tomato","Nightshade","Vegetable"));
        mProduceList.add(new ProduceItem("Tomato (St. Pierre)","Tomato","Nightshade","Vegetable"));
        mProduceList.add(new ProduceItem("Tomato (Yellow Baby)","Tomato","Nightshade","Vegetable"));
        mProduceList.add(new ProduceItem("Turmeric","Turmeric","Root","Vegetable"));
        mProduceList.add(new ProduceItem("Turnip","Turnip","Root","Vegetable"));
        mProduceList.add(new ProduceItem("Zucchini (Green)","Zucchini","Cucurbit","Vegetable"));

    }

    private void buildRecyclerView() {//builds the recyclerview
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new RecycleViewAdapter(mProduceList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new RecycleViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //happens when a card in the recyclerview is clicked
                if(posList==null){
                    posList=mProduceList;
                }
                produceET.setText(posList.get(position).getFoodType());

            }
        });
    }

}