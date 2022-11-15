package com.example.harvest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.*;

import android.app.Instrumentation;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class SplashActivityTest {

    @Rule
    public ActivityScenarioRule<SplashActivity> splashActivityActivityScenarioRule = new ActivityScenarioRule<SplashActivity>(SplashActivity.class);

    private ActivityScenario<SplashActivity> splashActivity = null;

    Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(SplashActivity.class.getName(),null,false);

    @Before
    public void setup() throws Exception{
        splashActivity = splashActivityActivityScenarioRule.getScenario();
    }

    @Test
    public void isImageInView(){
        onView(withId(R.id.imagePumpkin)).check(matches(isDisplayed()));
    }

    @Test
    public void isImageInView2(){
        onView(withId(R.id.imageApple)).check(matches(isDisplayed()));
    }

    @Test
    public void isTextInView(){
        onView(withId(R.id.harvest)).check(matches(isDisplayed()));
    }
}