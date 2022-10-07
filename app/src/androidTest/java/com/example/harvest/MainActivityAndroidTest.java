package com.example.harvest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MainActivityAndroidTest {

    @Rule
    public ActivityScenarioRule<MainActivity> mainActivityActivityScenarioRule = new ActivityScenarioRule<MainActivity>(MainActivity.class);

    private ActivityScenario<MainActivity> mainActivity = null;

    @Before
    public void setup() throws Exception{
        mainActivity = mainActivityActivityScenarioRule.getScenario();
    }

    @Test
    public void isActivityInView(){
        onView(withId(R.id.mainActivity)).check(matches(isDisplayed()));
    }

    @Test
    public void isEmailEmpty(){
        onView(withId(R.id.loginPassword)).perform(ViewActions.typeText("123456"));
        onView(withId(R.id.login)).perform(ViewActions.click());
        onView(withId(R.id.loginEmail)).check(matches(hasErrorText("Email is a required field")));

    }

    @Test
    public void isEmailValid(){
        onView(withId(R.id.loginEmail)).perform(ViewActions.typeText("asdf"));
        onView(withId(R.id.loginPassword)).perform(ViewActions.typeText("123456"));
        onView(withId(R.id.login)).perform(ViewActions.click());
        onView(withId(R.id.loginEmail)).check(matches(hasErrorText("Invalid email address")));

    }

    @Test
    public void isPasswordEmpty(){
        onView(withId(R.id.loginEmail)).perform(ViewActions.typeText("asdf@gmail.com"));
        onView(withId(R.id.loginPassword)).perform(ViewActions.typeText(""));
        onView(withId(R.id.login)).perform(ViewActions.click());
        onView(withId(R.id.loginPassword)).check(matches(hasErrorText("Password is a required field")));

    }

    @Test
    public void isPasswordLengthErrorMessage(){
        onView(withId(R.id.loginEmail)).perform(ViewActions.typeText("abc@gmail.com"));
        onView(withId(R.id.loginPassword)).perform(ViewActions.typeText("abc"));
        onView(withId(R.id.login)).perform(ViewActions.click());
        onView(withId(R.id.loginPassword)).check(matches(hasErrorText("Password length must be at least 6 characters")));

    }

//    @Test
//    public void viewChangesToCreateAccount(){
//        onView(withId(R.id.login)).perform(ViewActions.click());
//        onView(withId(R.id.CreateAccount)).check(matches(isDisplayed()));
//    }

//    @Test
//    public void viewChangesToForgotPassword(){
//        onView(withId(R.id.login)).perform(ViewActions.click());
//        onView(withId(R.id.ForgotPassword)).check(matches(isDisplayed()));
//    }

}
