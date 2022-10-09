package com.example.harvest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
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

//@RunWith(AndroidJUnit4.class)
public class ProfileActivityTest {

//    @Rule
//    public ActivityScenarioRule<ProfileActivity> profileActivityActivityScenarioRule = new ActivityScenarioRule<ProfileActivity>(ProfileActivity.class);
//
//    private ActivityScenario<ProfileActivity> profileActivity = null;
//
//    @Before
//    public void setup() throws Exception{
//        onView(withId(R.id.loginEmail)).perform(ViewActions.typeText("pumpkinpraiser@gmail.com"));
//        onView(withId(R.id.loginPassword)).perform(ViewActions.typeText("pumpkins"));
//        onView(withId(R.id.login)).perform(ViewActions.click());
//        profileActivity = profileActivityActivityScenarioRule.getScenario();
//    }
//
//    @Test
//    public void isActivityInView(){
//        onView(withId(R.id.ProfileActivity)).check(matches(isDisplayed()));
//    }
}
