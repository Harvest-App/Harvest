package com.example.harvest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.core.app.ActivityScenario;
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
}
