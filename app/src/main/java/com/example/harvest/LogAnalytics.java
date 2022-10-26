package com.example.harvest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class LogAnalytics extends AppCompatActivity {

    //UI elements
    private ImageView returnToEntries;
    private ImageView returnHome;
    private TextView sumData;
    private TextView displayHeading;
    private EditText produceET;
    private Button barGraph;
    private Button pieChart;
    private TextView heading;

    //Firestore database and paths
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference usersRef = db.collection("users");
    private CollectionReference allLogsRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Logs");
    String logID = (usersRef.document(FirebaseAuth.getInstance().getCurrentUser().getUid())).getId();
    private  String ID;
    private String logName;

    //recyclerview
    private RecyclerView mRecyclerView;
    private RecycleViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<ProduceItem> mProduceList;
    private ArrayList<ProduceItem> posList;// to get accurate position

    private int sum=0;
    private String produceName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_analytics);


        //intent to fetch the log ID and the log name
        Intent logInfoIntent = getIntent();
        if(logInfoIntent!=null){
            ID=logInfoIntent.getStringExtra("logID");
            logName = logInfoIntent.getStringExtra("logName");

        }
        else{
            Toast.makeText(LogAnalytics.this, "No intent found", Toast.LENGTH_SHORT).show();
        }

        //initialise UI elements and add OnClickListeners

        heading = findViewById(R.id.heading);
        String headingText = "Log Analytics:\n"+logName;
        heading.setText(headingText);
        produceET=findViewById(R.id.produceET);
        produceET.addTextChangedListener(new TextWatcher() {//adding filter functionality
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
        returnToEntries=findViewById(R.id.returnToLogEntries);

        returnToEntries.setOnClickListener(view -> {
            Intent intent = new Intent(LogAnalytics.this , LogEntryHome.class);
            intent.putExtra("logID",ID);
            intent.putExtra("logName",logName);
            startActivity(intent);
        });

        returnHome=findViewById(R.id.returnHome);
        returnHome.setOnClickListener(view -> {
            Intent intent = new Intent(LogAnalytics.this , ProfileActivity.class);
            startActivity(intent);
        });

        displayHeading = findViewById(R.id.sumHeading);
        sumData = findViewById(R.id.sumData);

        pieChart = findViewById(R.id.pieChart);
        pieChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //fetching and editing data from the settext
                String sumString="";
                sumString= sumData.getText().toString();
                if(sumString.isEmpty()){
                    produceET.setError("Choose something to analyse");
                    produceET.requestFocus();
                    return;
                }
                sumString = sumString.substring(0,sumString.length()-1);
                sum = Integer.parseInt(sumString);

                Intent intent = new Intent(LogAnalytics.this , PieGraph.class);
               // startActivity(intent);
                intent.putExtra("sum",sum);
                intent.putExtra("foodType",produceName);
                intent.putExtra("logID",ID);
                intent.putExtra("logName",logName);
                startActivity(intent);
            }
        });

        barGraph = findViewById(R.id.barChart);
        barGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(LogAnalytics.this , BarGraph.class);
                intent.putExtra("logID",ID);
                intent.putExtra("logName",logName);
                startActivity(intent);

            }
        });

        createProduceList();
        buildRecyclerView();
    }

    public void setProduceName(String produce){
        produceName=produce;
    }
    public void analyseLog(String logID){
        String produce = produceET.getText().toString().trim();
        setProduceName(produce);
       // String ID = (usersRef.document(FirebaseAuth.getInstance().getCurrentUser().getUid())).getId();

        //goes through entries in log and fetches the relevant information
        allLogsRef.document(logID).collection("Log Entries")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String display="";
                        String displayHead="";
                        int added=0;
                        for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){

                            LogEntry log = documentSnapshot.toObject(LogEntry.class);
                            String foodType = log.getProduceFood();
                            String weight = log.getWeight();

                            if (foodType.equals(produce)||produce.equals("Total")) {
                                added+=Integer.parseInt(weight);
                            }

                        }
                        display=Integer.toString(added)+"g";
                        if(produce.equals("Total")){
                            displayHead = "Total produce harvested:";
                        }
                        else{

                            displayHead = "Total "+produce+" harvested:";
                        }
                        displayHeading.setText(displayHead);
                        sumData.setText(display);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LogAnalytics.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void filter(String text) {//recyclerview filter
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
        mProduceList.add(new ProduceItem("Total","All","All","Analyse all log entries"));
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
                analyseLog(ID);
            }
        });
    }
}