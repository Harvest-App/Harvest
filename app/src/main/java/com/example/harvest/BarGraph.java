package com.example.harvest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class BarGraph extends AppCompatActivity {

    //UI elements
    private Button analytics;
    private Button entries;

    //variables
    ArrayList barArraylist;
    private float sumProduce;
    private String foodType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_graph);

        Intent intent = getIntent();
        if(intent!=null){
            sumProduce = getIntent().getIntExtra("sum",0) ;
            foodType=getIntent().getStringExtra("foodType");
        }

        entries = findViewById(R.id.returnToEntries);

        entries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BarGraph.this , LogEntryHome.class);
                startActivity(intent);
            }
        });
        analytics= findViewById(R.id.returnToAnalytics);
        analytics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BarGraph.this , LogAnalytics.class);
                startActivity(intent);
            }
        });
        BarChart barChart = findViewById(R.id.barChart);

        //call the statement to initialise before passing it to the dataset
        getData();
        BarDataSet barDataSet = new BarDataSet(barArraylist, foodType);

        //pass the data sets into here
        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);

        //setting colours and text size
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);

        //small description
        barChart.getDescription().setEnabled(true);

    }

    //method for bar graph data
    private void getData(){
        barArraylist = new ArrayList();

        //adding the new entries
        barArraylist.add(new BarEntry(2f,sumProduce));

    }
}