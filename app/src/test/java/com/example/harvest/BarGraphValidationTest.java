package com.example.harvest;

import static org.junit.Assert.*;

import org.junit.Test;

public class BarGraphValidationTest {

    @Test
    public void selectingFoodPasses() throws Exception{

        BarGraphValidation utils = new BarGraphValidation();

        assertTrue(utils.isValidCategory("Food"));
    }

    @Test
    public void selectingTypePasses() throws Exception{

        BarGraphValidation utils = new BarGraphValidation();

        assertTrue(utils.isValidCategory("Type"));
    }

    @Test
    public void selectingSubtypePasses() throws Exception{

        BarGraphValidation utils = new BarGraphValidation();

        assertTrue(utils.isValidCategory("Subtype"));
    }

    @Test
    public void selectingSupertypePasses() throws Exception{

        BarGraphValidation utils = new BarGraphValidation();

        assertTrue(utils.isValidCategory("Supertype"));
    }

    @Test
    public void selectingNoneFails() throws Exception{

        BarGraphValidation utils = new BarGraphValidation();

        assertFalse(utils.isValidCategory(""));
    }

    @Test
    public void selectingPastmonthPasses() throws Exception{

        BarGraphValidation utils = new BarGraphValidation();

        assertTrue(utils.isValidTimePeriod("Past month"));
    }

    @Test
    public void selectingPastyearPasses() throws Exception{

        BarGraphValidation utils = new BarGraphValidation();

        assertTrue(utils.isValidTimePeriod("Past year"));
    }

    @Test
    public void selectingPast6monthsPasses() throws Exception{

        BarGraphValidation utils = new BarGraphValidation();

        assertTrue(utils.isValidTimePeriod("Past 6 months"));
    }

    @Test
    public void selectingPast5yearsPasses() throws Exception{

        BarGraphValidation utils = new BarGraphValidation();

        assertTrue(utils.isValidTimePeriod("Past 5 years"));
    }

    @Test
    public void selectingAlltimePasses() throws Exception{

        BarGraphValidation utils = new BarGraphValidation();

        assertTrue(utils.isValidTimePeriod("All time"));
    }

    @Test
    public void selectingNoTimeFails() throws Exception{

        BarGraphValidation utils = new BarGraphValidation();

        assertFalse(utils.isValidTimePeriod(""));
    }


}