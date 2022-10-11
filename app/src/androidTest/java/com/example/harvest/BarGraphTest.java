package com.example.harvest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class BarGraphTest {

    @Rule
    public ActivityScenarioRule<BarGraph> barGraphActivityScenarioRule = new ActivityScenarioRule<BarGraph>(BarGraph.class);

    private ActivityScenario<BarGraph> barGraph = null;

    @Before
    public void setup() throws Exception{

        barGraph = barGraphActivityScenarioRule.getScenario();
    }

    @Test
    public void isActivityInView(){
        onView(withId(R.id.barGraph)).check(matches(isDisplayed()));
    }
}
