package com.g.pocketmal;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.rule.ActivityTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.g.pocketmal.ui.SplashActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasSibling;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class PeopleSearchTest {

    private static final String USER_NAME = "G-Lodan";

    @Rule
    public ActivityTestRule<SplashActivity> activityRule = new ActivityTestRule<>(SplashActivity.class);

    @Test
    public void testSearch() throws InterruptedException {

        Thread.sleep(3000);

        onView(withId(R.id.dl_layout)).perform(DrawerActions.open());
        onView(withId(R.id.tv_people_search)).perform(click());

        onView(withId(R.id.et_search_field))
                .perform(typeText(USER_NAME), pressImeActionButton());

        Thread.sleep(3000);

        onView(withId(R.id.ll_found_user)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_empty_result)).check(matches(not(isDisplayed())));

        onView(withId(R.id.iv_clear_result)).perform(click());

        onView(withId(R.id.ll_found_user)).check(matches(not(isDisplayed())));
        onView(withId(R.id.et_search_field)).check(matches(withText("")));

        onView(withId(R.id.et_search_field))
                .perform(typeText(UUID.randomUUID().toString()), pressImeActionButton());

        Thread.sleep(3000);

        onView(withId(R.id.tv_empty_result)).check(matches(isDisplayed()));

        Espresso.pressBack();
        onView(withId(R.id.dl_layout)).perform(DrawerActions.open());
        onView(withId(R.id.tv_people_search)).perform(click());

        onView(allOf(withText(USER_NAME), hasSibling(withText("just now"))))
                .check(matches(isDisplayed()));

        onView(withId(R.id.et_search_field)).perform(typeText(USER_NAME.substring(0, 4)));
        onView(allOf(withText(USER_NAME), hasSibling(withText("just now"))))
                .check(matches(isDisplayed()));

        onView(withId(R.id.et_search_field)).perform(typeText(USER_NAME.substring(0, 4) + USER_NAME));
        onView(allOf(withText(USER_NAME), hasSibling(withText("just now")))).check(doesNotExist());

        onView(withId(R.id.et_search_field)).perform(clearText());
        onView(allOf(withText(USER_NAME), hasSibling(withText("just now"))))
                .check(matches(isDisplayed()));

        Thread.sleep(1000);
    }
}
