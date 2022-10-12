package com.example.harvest;

import static org.junit.Assert.*;

import org.junit.Test;

public class PieGraphValidationTest {

    @Test
    public void applePercentagePasses() throws Exception{

        PieGraphValidation utils = new PieGraphValidation();

        double exp = 1223.0/10323 * 100;

        assertEquals(exp, utils.getApplePercentage(1223), 0.01);
    }

    @Test
    public void applePercentageFails() throws Exception{

        PieGraphValidation utils = new PieGraphValidation();

        double exp = 1223.0/10323 * 100 + 1;

        assertNotEquals(exp, utils.getApplePercentage(1223), 0.01);
    }

    @Test
    public void BlueBerryPercentagePasses() throws Exception{

        PieGraphValidation utils = new PieGraphValidation();

        double exp = 1223.0/10323 * 100;

        assertEquals(exp, utils.getBlueBerryPercentage(1223), 0.01);
    }

    @Test
    public void BlueBerryPercentageFails() throws Exception{

        PieGraphValidation utils = new PieGraphValidation();

        double exp = 1223.0/10323 * 100 + 1;

        assertNotEquals(exp, utils.getBlueBerryPercentage(1223), 0.01);
    }

    @Test
    public void AlmondPercentagePasses() throws Exception{

        PieGraphValidation utils = new PieGraphValidation();

        double exp = 1223.0/10323 * 100;

        assertEquals(exp, utils.getAlmondPercentage(1223), 0.01);
    }

    @Test
    public void AlmondPercentageFails() throws Exception{

        PieGraphValidation utils = new PieGraphValidation();

        double exp = 1223.0/10323 * 100 + 1;

        assertNotEquals(exp, utils.getAlmondPercentage(1223), 0.01);
    }

    @Test
    public void PumpkinPercentagePasses() throws Exception{

        PieGraphValidation utils = new PieGraphValidation();

        double exp = 1223.0/10323 * 100;

        assertEquals(exp, utils.getBlueBerryPercentage(1223), 0.01);
    }

    @Test
    public void PumpkinPercentageFails() throws Exception{

        PieGraphValidation utils = new PieGraphValidation();

        double exp = 1223.0/10323 * 100 + 1;

        assertNotEquals(exp, utils.getPumpkinPercentage(1223), 0.01);
    }

}