package com.example.harvest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;


import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class CreateLogTest {

    @Rule
    public ActivityScenarioRule<CreateLog> createLogActivityScenarioRule = new ActivityScenarioRule<CreateLog>(CreateLog.class);

    private ActivityScenario<CreateLog> createLog = null;

    @Before
    public void setup() throws Exception{
        createLog = createLogActivityScenarioRule.getScenario();
    }

    @Test
    public void isActivityInView(){
        onView(withId(R.id.createLog)).check(matches(isDisplayed()));
    }

    @Test
    public void loginTextTest() {
        onView(withId(R.id.createInstruction)).check(matches(isDisplayed()));
    }
}
