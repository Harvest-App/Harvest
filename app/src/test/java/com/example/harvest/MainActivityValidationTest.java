package com.example.harvest;

import org.junit.Test;

import static org.junit.Assert.*;

public class MainActivityValidationTest {

    @Test
    public void aValidPasswordPasses() throws Exception{

        MainActivityValidation utils = new MainActivityValidation();

        assertTrue(utils.isValidPassword("123456"));
    }

    @Test
    public void aValidPasswordFails() throws Exception{

        MainActivityValidation utils = new MainActivityValidation();

        assertTrue(!utils.isValidPassword("12356"));
    }

    @Test
    public void aValidEmailPasses() throws Exception{

        MainActivityValidation utils = new MainActivityValidation();

        assertTrue(utils.isValidEmail("123@123"));
    }

    @Test
    public void aValidEmailFails() throws Exception{

        MainActivityValidation utils = new MainActivityValidation();

        assertTrue(!utils.isValidEmail(""));
    }

    @Test
    public void aValidPasswordPasses2() throws Exception{

        MainActivityValidation utils = new MainActivityValidation();

        assertTrue(utils.isValidpassword("123456"));
    }

    @Test
    public void aValidPasswordFails2() throws Exception{

        MainActivityValidation utils = new MainActivityValidation();

        assertTrue(!utils.isValidpassword(""));
    }

}