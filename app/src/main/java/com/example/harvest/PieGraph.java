package com.example.harvest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.charts.PieChart;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class PieGraph extends AppCompatActivity {


    //UI elements
    private Button analytics;
    private Button entries;
    PieChart pieChart;

    //variables
    private float sumProduce;
    private String foodType;

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
        setContentView(R.layout.activity_pie_graph);

        Intent intent = getIntent();
        if(intent!=null){
            sumProduce = getIntent().getIntExtra("sum",0) ;
            foodType=getIntent().getStringExtra("foodType");
            ID=getIntent().getStringExtra("logID");
            logName = getIntent().getStringExtra("logName");
        }

        //UI
        pieChart = findViewById(R.id.activity_piechart);

        //buttons
        entries = findViewById(R.id.returnToEntries);
        entries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PieGraph.this , LogEntryHome.class);
                intent.putExtra("logID",ID);
                intent.putExtra("logName",logName);
                startActivity(intent);
            }
        });

        analytics= findViewById(R.id.returnToAnalytics);
        analytics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PieGraph.this , LogAnalytics.class);
                intent.putExtra("logID",ID);
                intent.putExtra("logName",logName);
                startActivity(intent);
            }
        });

        //ID for loading log entries
       // String logID = (usersRef.document(FirebaseAuth.getInstance().getCurrentUser().getUid())).getId();

        setupPieChart();
        loadLogEntries(ID);

    }
    private void setupPieChart() {
        //setting some attributes on the pie chart object
        //pieChart.setDrawHoleEnabled(true); //this shows a donut instead of a pie
        pieChart.setUsePercentValues(true); //tell chart we are using percentage values

        //size and color for yield value and labels
        pieChart.setEntryLabelTextSize(12);
        pieChart.setEntryLabelColor(Color.BLACK);

        //text at the centre of the pie chart
        pieChart.setCenterText("Total Yield:\n"+Float.toString(sumProduce)+"g");
        pieChart.setCenterTextSize(24);
        pieChart.getDescription().setEnabled(false);


        //adds a legend in the top right hand corner
        //provides details of information in the pie chart
        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP); //alignment and orientation of legend
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(true);
    }

    //method to load data into the piechart
    private void loadPieChartData(int item, int total){
        ArrayList<PieEntry> entries = new ArrayList<>();

        //populating the array and adding the data
        if(foodType.equals("Total")){
            entries.add(new PieEntry((float)total,foodType));
        }
        else {
            float thisP = (float) item / total;
            float otherP = (float) 1 - thisP;

            //for one produce item it will say 100%. Foodtype is gotten from Analytics
            entries.add(new PieEntry(thisP, foodType));
            entries.add(new PieEntry(otherP, "Other"));
        }
        //add a set of colors
        ArrayList<Integer> colors = new ArrayList<>();
        for (int color: ColorTemplate.MATERIAL_COLORS) { //get colors from a list of colors in the color template class
            colors.add(color);
        }

        for (int color: ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(color);
        }

        //create pie data set that's made up of the entries
        PieDataSet dataSet = new PieDataSet(entries, "PRODUCE LEGEND");    //use the dataset to create an object
        dataSet.setColors(colors);

        //using constructor from pie data class and passing in the pie data set to create the pie data object
        PieData data = new PieData(dataSet);
        data.setDrawValues(true);   //you want to draw the diff values of the percentages on the pie chart
        data.setValueFormatter(new PercentFormatter(pieChart));//and you want to present the floating variables as percentages
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.BLACK);    //text color of displayed percentages as black


        //provide the data from pie data object into the pie chart using the two steps below
        pieChart.setData(data);
        pieChart.invalidate();  //lets pie chart know that the data has been updated

        //implements some animation on piechart
        //not really necessary
        pieChart.animateY(1400, Easing.EaseInOutQuad);  //easing makes animation look smoother
    }

    public void loadLogEntries(String logID){
        String ID = (usersRef.document(FirebaseAuth.getInstance().getCurrentUser().getUid())).getId();

        //goes through entries in log and fetches the relevant information
        allLogsRef.document(logID).collection("Log Entries")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        int total=0;
                        int item=0;
                        for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){

                            LogEntry log = documentSnapshot.toObject(LogEntry.class);
                            String food = log.getProduceFood();
                            String weight = log.getWeight();

                            if (food.equals(foodType)) {
                                item+=Integer.parseInt(weight);
                            }
                                total+=Integer.parseInt(weight);
                        }

                        loadPieChartData(item,total);


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PieGraph.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}