package com.example.harvest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.widget.Toast;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class CreateAccountTest {

    @Rule
    public ActivityScenarioRule<CreateAccount> createAccountActivityScenarioRule = new ActivityScenarioRule<CreateAccount>(CreateAccount.class);

    private ActivityScenario<CreateAccount> createAccount = null;

    @Before
    public void setup() throws Exception{

        createAccount = createAccountActivityScenarioRule.getScenario();
    }

    @Test
    public void isActivityInView(){
        onView(withId(R.id.CreateAccount)).check(matches(isDisplayed()));
    }

    @Test
    public void isFullNameEmpty(){
        onView(withId(R.id.fullname)).perform(ViewActions.typeText(""));
        onView(withId(R.id.username)).perform(ViewActions.typeText("jefferson"));
        onView(withId(R.id.email)).perform(ViewActions.typeText("123@456.com"));
        onView(withId(R.id.password)).perform(ViewActions.typeText("123456"));
        onView(withId(R.id.createAccount)).perform(ViewActions.click());
        onView(withId(R.id.fullname)).check(matches(hasErrorText("Full name is a required field")));

    }

    @Test
    public void isUserNameEmpty(){
        onView(withId(R.id.fullname)).perform(ViewActions.typeText("Jeff"));
        onView(withId(R.id.username)).perform(ViewActions.typeText(""));
        onView(withId(R.id.email)).perform(ViewActions.typeText("123@456.com"));
        onView(withId(R.id.password)).perform(ViewActions.typeText("123456"));
        onView(withId(R.id.createAccount)).perform(ViewActions.click());
        onView(withId(R.id.username)).check(matches(hasErrorText("Username is a required field")));

    }

    @Test
    public void isEmailEmpty(){
        onView(withId(R.id.fullname)).perform(ViewActions.typeText("Jeff"));
        onView(withId(R.id.username)).perform(ViewActions.typeText("jefferson"));
        onView(withId(R.id.email)).perform(ViewActions.typeText(""));
        onView(withId(R.id.password)).perform(ViewActions.typeText("123456"));
        onView(withId(R.id.createAccount)).perform(ViewActions.click());
        onView(withId(R.id.email)).check(matches(hasErrorText("Email is a required field")));

    }

    @Test
    public void isPasswordEmpty(){
        onView(withId(R.id.fullname)).perform(ViewActions.typeText("Jeff"));
        onView(withId(R.id.username)).perform(ViewActions.typeText("jefferson"));
        onView(withId(R.id.email)).perform(ViewActions.typeText("123@456.com"));
        onView(withId(R.id.password)).perform(ViewActions.typeText(""));
        onView(withId(R.id.createAccount)).perform(ViewActions.click());
        onView(withId(R.id.password)).check(matches(hasErrorText("Password is a required field")));

    }

    @Test
    public void isPasswordLength(){
        onView(withId(R.id.fullname)).perform(ViewActions.typeText("Jeff"));
        onView(withId(R.id.username)).perform(ViewActions.typeText("jefferson"));
        onView(withId(R.id.email)).perform(ViewActions.typeText("123@456.com"));
        onView(withId(R.id.password)).perform(ViewActions.typeText("123"));
        onView(withId(R.id.createAccount)).perform(ViewActions.click());
        onView(withId(R.id.password)).check(matches(hasErrorText("Password length must be at least 6 characters")));

    }

    @Test
    public void isEmailValid(){
        onView(withId(R.id.fullname)).perform(ViewActions.typeText("Jeff"));
        onView(withId(R.id.username)).perform(ViewActions.typeText("jefferson"));
        onView(withId(R.id.email)).perform(ViewActions.typeText("123456com"));
        onView(withId(R.id.password)).perform(ViewActions.typeText("123456"));
        onView(withId(R.id.createAccount)).perform(ViewActions.click());
        onView(withId(R.id.email)).check(matches(hasErrorText("Invalid email address")));

    }

    @Test
    public void ViewChangeOnSuccess(){
        onView(withId(R.id.fullname)).perform(ViewActions.typeText("Jeff"));
        onView(withId(R.id.username)).perform(ViewActions.typeText("jefferson"));
        onView(withId(R.id.email)).perform(ViewActions.typeText("123@456.com"));
        onView(withId(R.id.password)).perform(ViewActions.typeText("123456"));
        onView(withId(R.id.createAccount)).perform(ViewActions.click());
        onView(withId(R.id.mainActivity)).check(matches(isDisplayed()));

    }
}
