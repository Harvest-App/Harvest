package com.example.harvest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import static org.junit.Assert.assertNotNull;

import android.app.Instrumentation;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

//@RunWith(AndroidJUnit4.class)
public class LogEntryHomeTest {

//    @Rule
//    public ActivityScenarioRule<LogEntryHome> logEntryHomeActivityScenarioRule = new ActivityScenarioRule<LogEntryHome>(LogEntryHome.class);
//
//    private ActivityScenario<LogEntryHome> logEntryHome = null;
//
//    Instrumentation.ActivityMonitor m = getInstrumentation().addMonitor(LogEntryHome.class.getName(),null,false);
//
//    @Before
//    public void setup() throws Exception{
//
//        logEntryHome = logEntryHomeActivityScenarioRule.getScenario();
//    }
//
//    @Test
//    public void isActivityInView(){
//        assertNotNull(m);
//    }
}
