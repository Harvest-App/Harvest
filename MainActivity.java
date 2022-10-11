package com.example.ati;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;


//references
//https://learntodroid.com/how-to-create-a-pie-chart-in-an-android-app-with-mpandroidchart/
//https://medium.com/@clyeung0714/using-mpandroidchart-for-android-application-piechart-123d62d4ddc0
//https://www.youtube.com/watch?v=S3zqxVoIUig

public class MainActivity extends AppCompatActivity {
    //create variable for pie chart
    PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set up piechart variable in on create method
        pieChart = findViewById(R.id.actvity_piechart);

        setupPieChart();
        loadPieChartData();

    }


    //envoke this method before the load piechart method
    private void setupPieChart() {
        //setting some attributes on the pie chart object
        //pieChart.setDrawHoleEnabled(true); //this shows a donut instead of a pie
        pieChart.setUsePercentValues(true); //tell chart we are using percentage values

        //size and color for yield value and labels
        pieChart.setEntryLabelTextSize(12);
        pieChart.setEntryLabelColor(Color.BLACK);

        //text at the centre of the pie chart
        pieChart.setCenterText("Total Yield");
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
    private void loadPieChartData(){
        ArrayList<PieEntry> entries = new ArrayList<>();

        //populating the array and adding the data

        //these are percentage yields though if you want to make it grams remove the "f"
        entries.add(new PieEntry(0.2f,"apples"));
        entries.add(new PieEntry(0.15f,"oranges"));
        entries.add(new PieEntry(0.10f,"pumpkins"));
        entries.add(new PieEntry(0.25f,"grape"));
        entries.add(new PieEntry(0.3f,"tomato"));

        //add a set of colors
        ArrayList<Integer> colors = new ArrayList<>();
        for (int color: ColorTemplate.MATERIAL_COLORS) { //get colors from a list of colors in the color template class
            colors.add(color);
        }

        for (int color: ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(color);
        }

        //create pie data set thats made up of the entries
        //had to sync something here just check
        PieDataSet dataSet = new PieDataSet(entries, "HARVEST");    //use the dataset to create an object
        dataSet.setColors(colors);


        //using constructor from pie data class (check imports) and passing in the pie data set to create the pie data object
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

}