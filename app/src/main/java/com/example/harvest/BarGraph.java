package com.example.harvest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class BarGraph extends AppCompatActivity{

    private static final String TAG = "BarGraph";

    //UI elements
    private ImageView analytics;
    private ImageView entries;
    private Spinner categorySpinner;
    private Spinner categoryListSpinner;
    private Spinner timeSpinner;
    private TextView logDisplayTextView;

    //variables
    private ArrayList<ProduceItem> mProduceList;
    private ArrayList<String> foodList;
    private ArrayList<String> subtypeList;
    private ArrayList<String> typeList;
    private ArrayList<String> supertypeList;
    private ArrayList<LogEntry> timeList;
    private ArrayList<LogEntry> logEntryList;

    private String foodArray[];
    private String subtypeArray[];
    private String typeArray[];
    String supertypeArray[];
    private String category;
    private String categoryItem;

    //Firestore database, documents, and collections
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference usersRef = db.collection("users");
    private DocumentReference logRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
    private CollectionReference allLogsRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Logs");
    private String ID;
    private String logName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_graph);

        //intent to get log ID and name
        Intent intent = getIntent();
        if(intent!=null){
            ID=getIntent().getStringExtra("logID");
            logName = getIntent().getStringExtra("logName");
        }

        //ID for loading log entries
       // String logID = (usersRef.document(FirebaseAuth.getInstance().getCurrentUser().getUid())).getId();

        //buttons
        entries = findViewById(R.id.returnToEntries);
        entries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BarGraph.this , LogEntryHome.class);
                intent.putExtra("logID",ID);
                intent.putExtra("logName",logName);
                startActivity(intent);
            }
        });

        analytics= findViewById(R.id.returnToAnalytics);
        analytics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BarGraph.this , LogAnalytics.class);
                intent.putExtra("logID",ID);
                intent.putExtra("logName",logName);
                startActivity(intent);
            }
        });


        //for displaying filtered log entries
        logDisplayTextView=findViewById(R.id.logDisplay);

        categorySpinner = findViewById(R.id.categorySpinner);
        categoryListSpinner = findViewById(R.id.listSpinner);
        timeSpinner = findViewById(R.id.timeSpinner);

        //creating everything needed for our drop down lists

        logEntryList = new ArrayList<>();
        timeList =new ArrayList<>();
        createProduceList();
        createFoodList();
        createSubtypeList();
        createTypeList();
        createSupertypeList();

        String [] categories = {"Food","Subtype","Type","Supertype"};

        foodArray= new String[foodList.size()];
        foodArray = foodList.toArray(foodArray);

        subtypeArray = new String[subtypeList.size()];
        subtypeArray = subtypeList.toArray(subtypeArray);

        typeArray = new String[typeList.size()];
        typeArray = typeList.toArray(typeArray);

        supertypeArray= new String[supertypeList.size()];
        supertypeArray = supertypeList.toArray(supertypeArray);

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        ArrayAdapter<String> foodAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, foodArray);
        ArrayAdapter<String> subtypeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, subtypeArray);
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, typeArray);
        ArrayAdapter<String> supertypeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, supertypeArray);

        String [] timePeriods = {"","Past month","Past 6 months","Past year","Past 5 years","All time"};
        ArrayAdapter<String> timeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, timePeriods);
        timeSpinner.setAdapter(timeAdapter);
        timeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(timePeriods[i]!=""){
                    changeTimePeriod(timePeriods[i]);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        categorySpinner.setAdapter(categoryAdapter);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                switch(i){
                    case 0:
                        logDisplayTextView.setText("");
                        category = "produceFood";
                        timeSpinner.setAdapter(timeAdapter);
                        categoryListSpinner.setAdapter(foodAdapter);
                        categoryListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                timeSpinner.setAdapter(timeAdapter);
                                logDisplayTextView.setText("");
                                categoryItem=foodArray[i];
                                if (foodArray[i] != "") {

                                    logEntryList.clear();
                                    loadLogEntries(ID);

                                }

                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                        break;
                    case 1:
                        timeSpinner.setAdapter(timeAdapter);
                        logDisplayTextView.setText("");
                        category = "produceSubtype";
                        categoryListSpinner.setAdapter(subtypeAdapter);
                        categoryListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                timeSpinner.setAdapter(timeAdapter);
                                logDisplayTextView.setText("");;
                                categoryItem=subtypeArray[i];
                                if(subtypeArray[i]!="") {

                                    logEntryList.clear();
                                    loadLogEntries(ID);

                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                        break;
                    case 2:
                        logDisplayTextView.setText("");
                        category= "produceType";
                        timeSpinner.setAdapter(timeAdapter);
                        categoryListSpinner.setAdapter(typeAdapter);
                        categoryListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                timeSpinner.setAdapter(timeAdapter);
                                logDisplayTextView.setText("");
                                categoryItem=typeArray[i];
                                if(typeArray[i]!="") {
                                    logEntryList.clear();
                                    loadLogEntries(ID);
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                        break;
                    case 3:
                        logDisplayTextView.setText("");
                        category = "produceSupertype";
                        timeSpinner.setAdapter(timeAdapter);
                        categoryListSpinner.setAdapter(supertypeAdapter);
                        categoryListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                logDisplayTextView.setText("");
                                timeSpinner.setAdapter(timeAdapter);
                                categoryItem=supertypeArray[i];
                                if(supertypeArray[i]!="") {
                                    logEntryList.clear();
                                    loadLogEntries(ID);
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void createProduceList() {
        mProduceList = new ArrayList<>();
        mProduceList.add(new ProduceItem("Almond", "Almond", "Nut", "Fruit"));
        mProduceList.add(new ProduceItem("Apple (Golden Delicious)", "Apple", "Pome Fruit", "Fruit"));
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
        mProduceList.add(new ProduceItem("Curry Leaf", "Curry Leaf", "Woody Herb", "Herb"));
        mProduceList.add(new ProduceItem("Edible Flower", "Edible Flower", "Flower", "Flower"));
        mProduceList.add(new ProduceItem("Fennel", "Fennel", "Soft Herb", "Herb"));
        mProduceList.add(new ProduceItem("Fig (Green)", "Fig", "False Fruit", "Fruit"));
        mProduceList.add(new ProduceItem("Fig (Purple)", "Fig", "False Fruit", "Fruit"));
        mProduceList.add(new ProduceItem("Gem Squash", "Squash", "Cucurbit", "Vegetable"));
        mProduceList.add(new ProduceItem("Ginger", "Ginger", "Root", "Vegetable"));
        mProduceList.add(new ProduceItem("Gooseberry", "Gooseberry", "Berry", "Fruit"));
        mProduceList.add(new ProduceItem("Granadilla", "Granadilla", "Vine Fruit", "Fruit"));
        mProduceList.add(new ProduceItem("Grape (Catawba)", "Grape", "Vine Fruit", "Fruit"));
        mProduceList.add(new ProduceItem("Grape (Hanepoot)", "Grape", "Vine Fruit", "Fruit"));
        mProduceList.add(new ProduceItem("Grape (Victoria)", "Grape", "Vine Fruit", "Fruit"));
        mProduceList.add(new ProduceItem("Grapefruit (Ruby)", "Grapefruit", "Citrus", "Fruit"));
        mProduceList.add(new ProduceItem("Jerusalem Artichoke", "Jerusalem Artichoke", "Root", "Vegetable"));
        mProduceList.add(new ProduceItem("Kale", "Kale", "Cruciferous", "Vegetable"));
        mProduceList.add(new ProduceItem("Kei Apple", "Kei Apple", "Berry", "Fruit"));
        mProduceList.add(new ProduceItem("Leek", "Leek", "Allium", "Vegetable"));
        mProduceList.add(new ProduceItem("Lemon", "Lemon", "Citrus", "Fruit"));
        mProduceList.add(new ProduceItem("Lemon Balm", "Lemon Balm", "Soft Herb", "Herb"));
        mProduceList.add(new ProduceItem("Lemon Verbena", "Lemon Verbena", "Hard Herb", "Herb"));
        mProduceList.add(new ProduceItem("Lettuce", "Lettuce", "Leaf", "Vegetable"));
        mProduceList.add(new ProduceItem("Lime", "Lime", "Citrus", "Fruit"));
        mProduceList.add(new ProduceItem("Marjoram", "Marjoram", "Hard Herb", "Herb"));
        mProduceList.add(new ProduceItem("Mint", "Mint", "Soft Herb", "Herb"));
        mProduceList.add(new ProduceItem("Mustard Leaf", "Mustard Leaf", "Leaf", "Vegetable"));
        mProduceList.add(new ProduceItem("Naartjie", "Naartjie", "Citrus", "Fruit"));
        mProduceList.add(new ProduceItem("Nasturtium", "Nasturtium", "Flower", "Flower"));
        mProduceList.add(new ProduceItem("Onion (Red)", "Onion", "Allium", "Vegetable"));
        mProduceList.add(new ProduceItem("Onion (White)", "Onion", "Allium", "Vegetable"));
        mProduceList.add(new ProduceItem("Orange (Cara Cara)", "Orange", "Citrus", "Fruit"));
        mProduceList.add(new ProduceItem("Orange (Valencia)", "Orange", "Citrus", "Fruit"));
        mProduceList.add(new ProduceItem("Oregano", "Oregano", "Woody Herb", "Herb"));
        mProduceList.add(new ProduceItem("Parsley", "Parsley", "Soft Herb", "Herb"));
        mProduceList.add(new ProduceItem("Pea", "Pea", "Legume", "Vegetable"));
        mProduceList.add(new ProduceItem("Peach (White)", "Peach", "Stone Fruit", "Fruit"));
        mProduceList.add(new ProduceItem("Peach (Yellow)", "Peach", "Stone Fruit", "Fruit"));
        mProduceList.add(new ProduceItem("Pepper (Green California Wonder)", "Pepper", "Nightshade", "Vegetable"));
        mProduceList.add(new ProduceItem("Pepper (Red Santorini)", "Pepper", "Nightshade", "Vegetable"));
        mProduceList.add(new ProduceItem("Plum (Yellow)", "Plum", "Stone Fruit", "Fruit"));
        mProduceList.add(new ProduceItem("Plum (Red)", "Plum", "Stone Fruit", "Fruit"));
        mProduceList.add(new ProduceItem("Plum (Purple)", "Plum", "Stone Fruit", "Fruit"));
        mProduceList.add(new ProduceItem("Plum (Purple Leaf)", "Plum", "Stone Fruit", "Fruit"));
        mProduceList.add(new ProduceItem("Pumpkin (Boerpampoen)", "Pumpkin", "Cucurbit", "Vegetable"));
        mProduceList.add(new ProduceItem("Pumpkin (Queensland Blue)", "Pumpkin", "Cucurbit", "Vegetable"));
        mProduceList.add(new ProduceItem("Radish", "Radish", "Root", "Vegetable"));
        mProduceList.add(new ProduceItem("Rhubarb", "Rhubarb", "Stalk", "Vegetable"));
        mProduceList.add(new ProduceItem("Rosemary", "Rosemary", "Hard Herb", "Herb"));
        mProduceList.add(new ProduceItem("Sage", "Sage", "Hard Herb", "Herb"));
        mProduceList.add(new ProduceItem("Shallot", "Onion", "Leaf", "Vegetable"));
        mProduceList.add(new ProduceItem("Sorrel", "Sorrel", "Leaf", "Vegetable"));
        mProduceList.add(new ProduceItem("Spinach", "Spinach", "Cucurbit", "Vegetable"));
        mProduceList.add(new ProduceItem("Strawberry", "Strawberry", "Berry", "Fruit"));
        mProduceList.add(new ProduceItem("Sunflower Seed", "Sunflower Seed", "Seed", "Flower"));
        mProduceList.add(new ProduceItem("Sweet Potato (White)", "Sweet Potato", "Root", "Vegetable"));
        mProduceList.add(new ProduceItem("Sweet Potato (Orange)", "Sweet Potato", "Root", "Vegetable"));
        mProduceList.add(new ProduceItem("Thyme", "Thyme", "Hard Herb", "Herb"));
        mProduceList.add(new ProduceItem("Tomato (Cherry)", "Tomato", "Nightshade", "Vegetable"));
        mProduceList.add(new ProduceItem("Tomato (Costoluto)", "Tomato", "Nightshade", "Vegetable"));
        mProduceList.add(new ProduceItem("Tomato (Floradade)", "Tomato", "Nightshade", "Vegetable"));
        mProduceList.add(new ProduceItem("Tomato (Moneymaker)", "Tomato", "Nightshade", "Vegetable"));
        mProduceList.add(new ProduceItem("Tomato (St. Pierre)", "Tomato", "Nightshade", "Vegetable"));
        mProduceList.add(new ProduceItem("Tomato (Yellow Baby)", "Tomato", "Nightshade", "Vegetable"));
        mProduceList.add(new ProduceItem("Turmeric", "Turmeric", "Root", "Vegetable"));
        mProduceList.add(new ProduceItem("Turnip", "Turnip", "Root", "Vegetable"));
        mProduceList.add(new ProduceItem("Zucchini (Green)", "Zucchini", "Cucurbit", "Vegetable"));
    }

    private void createFoodList(){
        foodList = new ArrayList<>();
        foodList.add("");
        boolean test;
        for(ProduceItem item:mProduceList){
            test= foodList.contains(item.getFoodType());
            if(test==false){
                foodList.add(item.getFoodType());
            }

        }
    }
    private void createSubtypeList(){
        subtypeList = new ArrayList<>();
        subtypeList.add("");
        boolean test;
        for(ProduceItem item:mProduceList){
            test= subtypeList.contains(item.getSubType());
            if(test==false){
                subtypeList.add(item.getSubType());
            }

        }
    }
    private void createTypeList(){
        typeList = new ArrayList<>();
        typeList.add("");
        boolean test;
        for(ProduceItem item:mProduceList){
            test= typeList.contains(item.getType());
            if(test==false){
                typeList.add(item.getType());
            }

        }
    }
    private void createSupertypeList(){
        supertypeList = new ArrayList<>();
        supertypeList.add("");
        boolean test;
        for(ProduceItem item:mProduceList){
            test= supertypeList.contains(item.getSuperType());
            if(test==false){
                supertypeList.add(item.getSuperType());
            }

        }
    }

    public void createTimeList(LogEntry entry){
        logEntryList.add(entry);
    }

    public void changeTimePeriod(String period) {
        timeList.clear();
        String entryInfo="";

        //fetch current time
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        String currentTime = formatter.format(date);

        int month, year, newMonth, newYear;
        String day;

        day = currentTime.substring(0, 2);
        month = Integer.parseInt(currentTime.substring(3, 5));
        year = Integer.parseInt(currentTime.substring(6, 10));
        newMonth = month;
        newYear= year;

        switch(period){
            case "Past month":
                newMonth = month-1;
                break;
            case "Past 6 months":
                newMonth = month-6;
                break;
            case "Past year":
                newYear = year-1;
                break;
            case "Past 5 years":
                newYear = year-5;
                break;

        }

        String entryTime="";
        int entryDay=1000;
        int entryMonth=1000;
        int entryYear=1000;
        for(LogEntry entry:logEntryList){

            entryTime= entry.getTimeCreated();
            entryDay = Integer.parseInt(entryTime.substring(0, 2));
            entryMonth = Integer.parseInt(entryTime.substring(3, 5));
            entryYear = Integer.parseInt(entryTime.substring(6, 10));

            String foodType = entry.getProduceFood();
            String weight = entry.getWeight();
            String timeCreated=entry.getTimeCreated();

            if(period.equals("All time")){
                timeList.add(entry);
                entryInfo+="Produce Type: "+foodType+"\n"+"Weight: "+weight+"g\n"+"Log created: "+timeCreated+"\n\n";
                continue;
            }

            if(entryYear>newYear){
                timeList.add(entry);
                entryInfo+="Produce Type: "+foodType+"\n"+"Weight: "+weight+"g\n"+"Log created: "+timeCreated+"\n\n";
            }
            else if(entryYear==newYear&&entryMonth>newMonth){
                timeList.add(entry);
                entryInfo+="Produce Type: "+foodType+"\n"+"Weight: "+weight+"g\n"+"Log created: "+timeCreated+"\n\n";
            }
            else if(entryYear==newYear&&entryMonth==newMonth&&entryDay>Integer.parseInt(day)){
                timeList.add(entry);
                entryInfo+="Produce Type: "+foodType+"\n"+"Weight: "+weight+"g\n"+"Log created: "+timeCreated+"\n\n";
            }

        }
        logDisplayTextView.setText(entryInfo);

    }

    public void loadLogEntries(String logID){//fetches log entries from Firestore
        allLogsRef.document(logID).collection("Log Entries")
                .whereEqualTo(category,categoryItem)
                .orderBy("timeCreated", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){

                            //QuerySnapshot is our whole collection, which has multiple document snapshots
                            //each document snapshot represents a log entry
                            LogEntry log = documentSnapshot.toObject(LogEntry.class);
                            createTimeList(log);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(BarGraph.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString());
                    }
                });
    }
}
