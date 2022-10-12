package com.example.harvest;

import java.util.ArrayList;

public class BarGraphValidation {

    ArrayList<String> cat = new ArrayList<String>(4) {
        {
            add("Food");
            add("Type");
            add("Subtype");
            add("Supertype");
        }
    };

    ArrayList<String> time = new ArrayList<String>(5) {
        {
            add("Past month");
            add("Past 6 months");
            add("Past year");
            add("Past 5 years");
            add("All time");
        }
    };

    /**
     * checks to see if category selected is valid
     *
     * @param category
     * @return
     */
    public boolean isValidCategory(String category){
        for (int i = 0; i < cat.size(); ++i){
            if (cat.get(i).equals(category)){
                return true;
            }
        }
        return false;
    }

    /**
     * checks to see if time period selected is valid
     *
     * @param timePeriod
     * @return
     */
    public boolean isValidTimePeriod(String timePeriod){
        for (int i = 0; i < time.size(); ++i){
            if (time.get(i).equals(timePeriod)){
                return true;
            }
        }
        return false;
    }
}
