package com.example.harvest;

import static org.junit.Assert.*;

import org.junit.Test;

public class BarGraphValidationTest {

    // Tests to see if "Food" can be selected
    @Test
    public void selectingFoodPasses() throws Exception{

        BarGraphValidation utils = new BarGraphValidation();

        assertTrue(utils.isValidCategory("Food"));
    }

    // Tests to see if "Type" can be selected
    @Test
    public void selectingTypePasses() throws Exception{

        BarGraphValidation utils = new BarGraphValidation();

        assertTrue(utils.isValidCategory("Type"));
    }

    // Tests to see if "Subtype" can be selected
    @Test
    public void selectingSubtypePasses() throws Exception{

        BarGraphValidation utils = new BarGraphValidation();

        assertTrue(utils.isValidCategory("Subtype"));
    }

    // Tests to see if "Supertype" can be selected
    @Test
    public void selectingSupertypePasses() throws Exception{

        BarGraphValidation utils = new BarGraphValidation();

        assertTrue(utils.isValidCategory("Supertype"));
    }

    // Tests to see if something has to be selected
    @Test
    public void selectingNoneFails() throws Exception{

        BarGraphValidation utils = new BarGraphValidation();

        assertFalse(utils.isValidCategory(""));
    }

    // Tests to see if "Past month" can be selected
    @Test
    public void selectingPastmonthPasses() throws Exception{

        BarGraphValidation utils = new BarGraphValidation();

        assertTrue(utils.isValidTimePeriod("Past month"));
    }

    // Tests to see if "Past year" can be selected
    @Test
    public void selectingPastyearPasses() throws Exception{

        BarGraphValidation utils = new BarGraphValidation();

        assertTrue(utils.isValidTimePeriod("Past year"));
    }

    // Tests to see if "Past 6 months" can be selected
    @Test
    public void selectingPast6monthsPasses() throws Exception{

        BarGraphValidation utils = new BarGraphValidation();

        assertTrue(utils.isValidTimePeriod("Past 6 months"));
    }

    // Tests to see if "Past 5 years" can be selected
    @Test
    public void selectingPast5yearsPasses() throws Exception{

        BarGraphValidation utils = new BarGraphValidation();

        assertTrue(utils.isValidTimePeriod("Past 5 years"));
    }

    // Tests to see if "All time" can be selected
    @Test
    public void selectingAlltimePasses() throws Exception{

        BarGraphValidation utils = new BarGraphValidation();

        assertTrue(utils.isValidTimePeriod("All time"));
    }

    // Tests to see if something has to be selected
    @Test
    public void selectingNoTimeFails() throws Exception{

        BarGraphValidation utils = new BarGraphValidation();

        assertFalse(utils.isValidTimePeriod(""));
    }


}