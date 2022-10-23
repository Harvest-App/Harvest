package com.example.harvest;

public class PieGraphValidation {
    double Total_Yield = 10323.0;

    /**
     * gets percentage of total that is apples
     * @param apple
     * @return
     */
    public double getApplePercentage(int apple){
        double p = apple / Total_Yield;

        return p * 100;
    }

    /**
     * gets percentage of total that is blueberries
     * @param blueberry
     * @return
     */
    public double getBlueBerryPercentage(int blueberry){
        double p = blueberry / Total_Yield;

        return p * 100;
    }

    /**
     * gets percentage of total that is almonds
     * @param almond
     * @return
     */
    public double getAlmondPercentage(int almond){
        double p = almond / Total_Yield;

        return p * 100;
    }

    /**
     * gets percentage of total that is pumpkins
     * @param pumpkin
     * @return
     */
    public double getPumpkinPercentage(int pumpkin){
        double p = pumpkin / Total_Yield;

        return p * 100;
    }
}
