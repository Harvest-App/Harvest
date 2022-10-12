package com.example.harvest;

import java.util.ArrayList;

public class BarGraphValidation {

    ArrayList<String> cat = new ArrayList<String>(4) {
        {
            add("food");
            add("type");
            add("subtype");
            add("supertype");
        }
    };

    public boolean isValidCategory(String category){
        for (int i = 0; i < cat.size(); ++i){
            if (cat.get(i).equals(category)){
                return true;
            }
        }
        return false;
    }
}
