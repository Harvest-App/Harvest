package com.example.harvest;

public class ProduceItem {

    private String foodType;
    private String subType;
    private String type;
    private String superType;

    public ProduceItem(){
        //Firestore requires an empty public constructor
    }

    public ProduceItem(String foodType, String subType, String type, String superType) {

        this.foodType=foodType;
        this.subType=subType;
        this.type=type;
        this.superType=superType;

    }

    public void changeText(){
        foodType="Selected";
    }

    public String getFoodType() {
        return foodType;
    }

    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSuperType() {
        return superType;
    }

    public void setSuperType(String superType) {
        this.superType = superType;
    }

}




