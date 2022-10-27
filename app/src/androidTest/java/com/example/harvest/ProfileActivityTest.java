package com.example.harvest;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import static org.junit.Assert.assertNotNull;

import android.app.Instrumentation;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ProfileActivityTest {

    @Rule
    public ActivityScenarioRule<ProfileActivity> profileActivityActivityScenarioRule = new ActivityScenarioRule<ProfileActivity>(ProfileActivity.class);

    private ActivityScenario<ProfileActivity> profileActivity = null;

    Instrumentation.ActivityMonitor m = getInstrumentation().addMonitor(ProfileActivity.class.getName(),null,false);

    @Before
    public void setup() throws Exception{

        profileActivity = profileActivityActivityScenarioRule.getScenario();
    }

    @Test
    public void isActivityInView(){
        onView(withId(R.id.profileActivity)).check(matches(isDisplayed()));
    }

//    @Test
//    public void isLogoutSuccessful(){
//        onView(withId(R.id.logOut)).perform(ViewActions.click());
//        Instrumentation.ActivityMonitor LandingMonitor = getInstrumentation().addMonitor(MainActivity.class.getName(),null,false);
//        assertNotNull(LandingMonitor);
//
//    }

//    @Test
//    public void isAddLogSuccessful(){
//        onView(withId(R.id.addLog)).perform(ViewActions.click());
//        Instrumentation.ActivityMonitor LandingMonitor = getInstrumentation().addMonitor(CreateLog.class.getName(),null,false);
//        assertNotNull(LandingMonitor);
//
//    }

//    @Test
//    public void isLogEntryClickSuccessful(){
//        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(0, ViewActions.click()));
//        Instrumentation.ActivityMonitor Lmonitor = getInstrumentation().addMonitor(LogEntryHome.class.getName(),null,false);
//        assertNotNull(Lmonitor);
//
//    }

}
