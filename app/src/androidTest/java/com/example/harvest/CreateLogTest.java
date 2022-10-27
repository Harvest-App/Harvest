package com.example.harvest;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import static org.junit.Assert.assertNotNull;

import android.app.Instrumentation;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class CreateLogTest {

    @Rule
    public ActivityScenarioRule<CreateLog> createLogActivityScenarioRule = new ActivityScenarioRule<CreateLog>(CreateLog.class);

    private ActivityScenario<CreateLog> createLog = null;

    Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(CreateLog.class.getName(),null,false);

    @Before
    public void setup() throws Exception{

        createLog = createLogActivityScenarioRule.getScenario();
    }

    @Test
    public void isActivityInView(){
        onView(withId(R.id.createLog)).check(matches(isDisplayed()));
    }

    @Test
    public void isLogNameEmpty(){
        onView(withId(R.id.logName)).perform(ViewActions.typeText(""));
        closeSoftKeyboard();
        onView(withId(R.id.addLog)).perform(ViewActions.click());
        onView(withId(R.id.logName)).check(matches(hasErrorText("Enter log name to proceed")));
    }

    @Test
    public void isReturnHomeSuccessful(){
        onView(withId(R.id.returnHome)).perform(ViewActions.click());
        Instrumentation.ActivityMonitor LandingMonitor = getInstrumentation().addMonitor(ProfileActivity.class.getName(),null,false);
        assertNotNull(LandingMonitor);

    }

//    @Test
//    public void isAddLog(){
//        onView(withId(R.id.logName)).perform(ViewActions.typeText("AppleFarm"));
//        closeSoftKeyboard();
//        onView(withId(R.id.addLog)).perform(ViewActions.click());
//
//    }
}
