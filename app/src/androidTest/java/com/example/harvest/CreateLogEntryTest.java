package com.example.harvest;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class CreateLogEntryTest {

    @Rule
    public ActivityScenarioRule<CreateLogEntry> createLogEntryActivityScenarioRule = new ActivityScenarioRule<CreateLogEntry>(CreateLogEntry.class);

    private ActivityScenario<CreateLogEntry> createLogEntry = null;

    @Before
    public void setup() throws Exception{

        createLogEntry = createLogEntryActivityScenarioRule.getScenario();
    }

    @Test
    public void isActivityInView(){
        onView(withId(R.id.createLogentry)).check(matches(isDisplayed()));
    }

    @Test
    public void isProduceTypeEmpty(){
        onView(withId(R.id.produceET)).perform(ViewActions.typeText(""));
        closeSoftKeyboard();
        onView(withId(R.id.addLogEntry)).perform(ViewActions.click());
        onView(withId(R.id.produceET)).check(matches(hasErrorText("Enter produce type")));
    }

    @Test
    public void isProduceWeightEmpty(){
        onView(withId(R.id.produceET)).perform(ViewActions.typeText("Almond"));
        onView(withId(R.id.weightET)).perform(ViewActions.typeText(""));
        closeSoftKeyboard();
        onView(withId(R.id.addLogEntry)).perform(ViewActions.click());
        onView(withId(R.id.weightET)).check(matches(hasErrorText("Enter produce weight")));
    }
}
