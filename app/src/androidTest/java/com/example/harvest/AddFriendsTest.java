package com.example.harvest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import android.app.Instrumentation;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class AddFriendsTest {

    @Rule
    public ActivityScenarioRule<AddFriends> addFriendsActivityScenarioRule = new ActivityScenarioRule<AddFriends>(AddFriends.class);

    private ActivityScenario<AddFriends> addFriends = null;

    Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(AddFriends.class.getName(),null,false);

    @Before
    public void setup() throws Exception{
        addFriends = addFriendsActivityScenarioRule.getScenario();
    }

    @Test
    public void isActivityInView(){
        onView(withId(R.id.add_Friend)).check(matches(isDisplayed()));
    }
}
