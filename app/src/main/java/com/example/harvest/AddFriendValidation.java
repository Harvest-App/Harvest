package com.example.harvest;

public class AddFriendValidation {
    String user = "FirstName";

    /**
     * checks to see if friends name is valid
     *
     * @param name
     * @return
     */
    public boolean isValidName(String name){
        if (name.isEmpty()){
            return false;
        }
        if (name.equals(user)){
            return false;
        }

        return true;
    }
}
