package com.example.harvest;

import static org.junit.Assert.*;

import org.junit.Test;

public class AddFriendValidationTest {

    @Test
    public void isValidNameSuccessful() {
        AddFriendValidation utils = new AddFriendValidation();

        assertTrue(utils.isValidName("Friend"));
    }

    @Test
    public void isValidNameUnsuccessful() {
        AddFriendValidation utils = new AddFriendValidation();

        assertFalse(utils.isValidName(""));
    }

    @Test
    public void isValidNameUnsuccessful2() {
        AddFriendValidation utils = new AddFriendValidation();

        assertFalse(utils.isValidName("FirstName"));
    }
}