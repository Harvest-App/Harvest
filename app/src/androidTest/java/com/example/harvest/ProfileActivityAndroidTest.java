package com.example.harvest;

import static androidx.test.espresso.Espresso.onView;
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
public class ProfileActivityAndroidTest {

    @Rule
    public ActivityScenarioRule<ProfileActivity> profileActivityActivityScenarioRule = new ActivityScenarioRule<ProfileActivity>(ProfileActivity.class);

    private ActivityScenario<ProfileActivity> profileActivity = null;

    //Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(ProfileActivity.class.getName(),null,false);

    @Before
    public void setup() throws Exception{
        profileActivity = profileActivityActivityScenarioRule.getScenario();
    }

    @Test
    public void isLogoutSuccessful(){
        onView(withId(R.id.logOut)).perform(ViewActions.click());
        Instrumentation.ActivityMonitor LandingMonitor = getInstrumentation().addMonitor(MainActivity.class.getName(),null,false);
        assertNotNull(LandingMonitor);

    }
}
