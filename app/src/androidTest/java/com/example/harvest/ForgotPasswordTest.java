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
public class ForgotPasswordTest {

    @Rule
    public ActivityScenarioRule<ForgotPassword> forgotPasswordActivityScenarioRule = new ActivityScenarioRule<ForgotPassword>(ForgotPassword.class);

    private ActivityScenario<ForgotPassword> forgotPassword = null;

    @Before
    public void setup() throws Exception{

        forgotPassword = forgotPasswordActivityScenarioRule.getScenario();
    }

    @Test
    public void isActivityInView(){
        onView(withId(R.id.FOrgotPassword)).check(matches(isDisplayed()));
    }

    @Test
    public void isEmailEmpty(){

        onView(withId(R.id.email)).perform(ViewActions.typeText(""));
        closeSoftKeyboard();
        onView(withId(R.id.resetPassword)).perform(ViewActions.click());
        onView(withId(R.id.email)).check(matches(hasErrorText("Email required")));

    }

    @Test
    public void isEmailValid(){
        onView(withId(R.id.email)).perform(ViewActions.typeText("123456com"));
        closeSoftKeyboard();
        onView(withId(R.id.resetPassword)).perform(ViewActions.click());
        onView(withId(R.id.email)).check(matches(hasErrorText("Email invalid")));

    }

    @Test
    public void doesReturnHome(){
        onView(withId(R.id.email)).perform(ViewActions.typeText("zagesh2000@gmail.com"));
        closeSoftKeyboard();
        onView(withId(R.id.resetPassword)).perform(ViewActions.click());
        Instrumentation.ActivityMonitor LandingMonitor = getInstrumentation().addMonitor(ProfileActivity.class.getName(),null,false);
        assertNotNull(LandingMonitor);

    }
}
