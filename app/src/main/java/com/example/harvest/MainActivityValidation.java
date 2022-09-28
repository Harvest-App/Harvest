package com.example.harvest;

public class MainActivityValidation {

    /**
     * Checks to see if password is not 6 characters or longer
     *
     * @param password
     * @return greaterThan5
     */
    public boolean isValidPassword(String password){
        boolean greaterThan5 = password.length() > 5;

        return greaterThan5;
    }

    /**
     * Checks to see if email was filled
     *
     * @param email
     * @return EmailFilled
     */
    public boolean isValidEmail(String email){
        boolean EmailFilled = !email.isEmpty();

        return EmailFilled;
    }

    /**
     * Checks to see if password was filled
     *
     * @param password
     * @return passwordFilled
     */
    public boolean isValidpassword(String password){
        boolean passwordFilled = !password.isEmpty();

        return passwordFilled;
    }
}
