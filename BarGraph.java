package com.example.harvest;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class BarGraph extends AppCompatActivity {

    ArrayList barArraylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_graph);

        BarChart barChart = findViewById(R.id.barChart);

        //call the statement to initialise before passing it to the dataset
        getData();
        BarDataSet barDataSet = new BarDataSet(barArraylist, "Harvest");

        //pass the data sets into here
        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);

        //setting colors and text size
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);

        //small description
        barChart.getDescription().setEnabled(true);


    }

    //method for the dummy data
    private void getData(){
        barArraylist = new ArrayList();

        //adding the new entries
        barArraylist.add(new BarEntry(2f,5));
        barArraylist.add(new BarEntry(3f,10));
        barArraylist.add(new BarEntry(4f,15));
        barArraylist.add(new BarEntry(5f,20));
        barArraylist.add(new BarEntry(6f,25));

    }
}