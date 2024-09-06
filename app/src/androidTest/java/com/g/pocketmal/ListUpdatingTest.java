package com.g.pocketmal;

import android.app.Activity;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import android.view.View;
import android.widget.NumberPicker;

import com.g.pocketmal.database.helpers.AnimeDBHelper;
import com.g.pocketmal.database.models.Anime;
import com.g.pocketmal.ui.legacy.SplashActivity;
import com.g.pocketmal.util.EpisodeType;
import com.g.pocketmal.util.helpers.DateHelper;
import com.g.pocketmal.data.common.ListCounts;
import com.g.pocketmal.util.list.ListsManager;

import org.hamcrest.Matcher;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.hasSibling;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class ListUpdatingTest {

    private static final int ID = 6682;
    private static final String TITLE = "11eyes";

    private Activity activity;

    private final ListsManager manager = ListsManager.getInstance();
    private final AnimeDBHelper dbHelper = new AnimeDBHelper();

    @Rule
    public ActivityTestRule<SplashActivity> activityRule = new ActivityTestRule<>(SplashActivity.class);

    @Before
    public void init() {
        activity = activityRule.getActivity();
    }

    @Test
    public void testUpdating() throws InterruptedException {
        add();
        update();
        remove();
    }

    private void add() throws InterruptedException {

        Thread.sleep(3000);

        Assert.assertFalse(isInList());
        Anime animePreAdding = dbHelper.getAnimeById(ID);
        Assert.assertNull(animePreAdding);

        int plannedToWatch = ListsManager.getInstance().getAnimeCounts().getPlannedCount();

        onView(withId(R.id.action_search)).perform(click());
        onView(withId(R.id.action_search)).perform(click());
        onView(withHint(R.string.search)).perform(click());
        onView(withHint(R.string.search)).perform(typeText(TITLE), pressImeActionButton());

        Thread.sleep(3000);

        onView(allOf(withText(TITLE), hasSibling(withText("Season: TV"))))
                .check(matches(isDisplayed())).perform(click());

        Thread.sleep(7000);

        onView(withId(R.id.btn_add_to_my_list)).check(matches(isDisplayed()));
        onView(withId(R.id.btn_add_to_my_list)).perform(click());

        Thread.sleep(2000);

        Assert.assertTrue(isInList());
        Assert.assertTrue(isInList(MalTitle.PLANNED));

        Anime anime = dbHelper.getAnimeById(ID);
        Assert.assertNotNull(anime);
        Assert.assertEquals(anime.getMyStatus(), MalTitle.PLANNED);

        Espresso.pressBack();
        Espresso.pressBack();

        onView(withId(R.id.dl_layout)).perform(DrawerActions.open());
        onView(withId(R.id.rl_lists)).check(matches(isDisplayed())).perform(click());
        onView(withId(R.id.tv_planned_count)).check(matches(withText(String.valueOf(plannedToWatch + 1))));
        onView(withId(R.id.rl_planned_holder)).perform(click());
        onView(withText(TITLE)).check(matches(isDisplayed()));
    }

    private void update() throws InterruptedException {

        ListCounts counts = ListsManager.getInstance().getAnimeCounts();

        onView(withText(TITLE)).perform(click());
        Thread.sleep(3000);
        onView(withId(R.id.btn_status)).perform(click());

        onView(allOf(withText(R.string.watching), hasSibling(withParent(withId(R.id.ll_list_popup_holder)))))
                .perform(click());
        onView(withId(R.id.btn_status)).check(matches(withText(R.string.watching)));

        Assert.assertFalse(isInList(MalTitle.PLANNED));
        Assert.assertTrue(isInList(MalTitle.IN_PROGRESS));
        Anime animeInProgress = dbHelper.getAnimeById(ID);
        Assert.assertNotNull(animeInProgress);
        Assert.assertEquals(animeInProgress.getMyStatus(), MalTitle.IN_PROGRESS);

        Thread.sleep(3000);

        Espresso.pressBack();
        onView(withId(R.id.dl_layout)).perform(DrawerActions.open());

        onView(withId(R.id.tv_planned_count)).check(matches(withText(String.valueOf(counts.getPlannedCount() - 1))));
        onView(withId(R.id.tv_in_progress_count)).check(matches(withText(String.valueOf(counts.getInProgressCount() + 1))));
        onView(withId(R.id.rl_in_progress_holder)).perform(click());
        onView(withText(TITLE)).check(matches(isDisplayed())).perform(click());

        Thread.sleep(3000);

        onView(withId(R.id.btn_increment_episodes)).perform(click());

        Thread.sleep(3000);

        onView(withId(R.id.tv_episodes)).check(matches(withText("1 / 12")));

        onView(withId(R.id.action_edit_details)).perform(click());
        onView(withId(R.id.tv_start_date)).check(matches(
                withText(DateHelper.getDateString(DateHelper.getMalCurrentDateFormat()))));
        Anime animeEpisodes = dbHelper.getAnimeById(ID);
        Assert.assertNotNull(animeEpisodes);
        Assert.assertEquals(animeEpisodes.getEpisodes().getMyEpisodes().intValue(), 1);
        Assert.assertEquals(animeEpisodes.getStartDate(), DateHelper.getMalCurrentDateFormat());

        Espresso.pressBack();

        onView(withId(R.id.ib_episodes)).perform(click());
        onView(withId(R.id.et_episodes_field)).perform(clearText(), typeText("13"));
        onView(withId(R.id.ib_update)).perform(click());
        onView(withId(R.id.tv_error)).check(matches(isDisplayed()));
        onView(withId(R.id.et_episodes_field)).perform(clearText(), typeText("11"));
        onView(withId(R.id.ib_update)).perform(click());

        Thread.sleep(3000);

        onView(withId(R.id.tv_episodes)).check(matches(withText("11 / 12")));

        onView(withId(R.id.btn_increment_episodes)).perform(click());

        onView(withText(R.string.setAnimeAsCompleted)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_yes_button)).check(matches(isDisplayed())).perform(click());
        onView(withText(R.string.rateTitle)).check(matches(isDisplayed()));
        onView(withId(R.id.np_score)).perform(setScore(3));
        onView(withId(R.id.tv_ok_button)).perform(click());

        Thread.sleep(3000);

        onView(withId(R.id.btn_status)).check(matches(withText(R.string.completed)));
        onView(withId(R.id.btn_score)).check(matches(withText("Score: 8")));
        onView(withId(R.id.tv_episodes)).check(matches(withText("12 / 12")));

        Assert.assertFalse(isInList(MalTitle.IN_PROGRESS));
        Assert.assertTrue(isInList(MalTitle.COMPLETED));
        Anime animeCompleted = dbHelper.getAnimeById(ID);
        Assert.assertNotNull(animeCompleted);
        Assert.assertEquals(animeCompleted.getEpisodes().getMyEpisodes().intValue(), 12);
        Assert.assertEquals(animeCompleted.getMyStatus(), MalTitle.COMPLETED);
        Assert.assertEquals(animeCompleted.getMyScore(), 8);

        onView(withId(R.id.action_edit_details)).perform(click());
        onView(withId(R.id.tv_finish_date)).check(matches(
                withText(DateHelper.getDateString(DateHelper.getMalCurrentDateFormat()))));

        Espresso.pressBack();

        onView(withId(R.id.btn_increment_episodes)).perform(click());
        onView(withText(R.string.watchedAllEpisodes))
                .inRoot(withDecorView(not(is(activity.getWindow().getDecorView()))))
                .check(matches(isDisplayed()));

        Espresso.pressBack();
        onView(withText(TITLE)).check(doesNotExist());

        onView(withId(R.id.dl_layout)).perform(DrawerActions.open());

        onView(withId(R.id.tv_in_progress_count)).check(matches(withText(String.valueOf(counts.getInProgressCount()))));
        onView(withId(R.id.tv_completed_count)).check(matches(withText(String.valueOf(counts.getCompletedCount() + 1))));
        onView(withId(R.id.tv_completed_count)).perform(click());

        onView(withText(TITLE)).check(matches(isDisplayed()));
    }

    private void remove() throws InterruptedException {

        Anime animePreRemoving = dbHelper.getAnimeById(ID);
        Assert.assertNotNull(animePreRemoving);

        int completed = ListsManager.getInstance().getAnimeCounts().getCompletedCount();

        onView(withText(TITLE)).check(matches(isDisplayed())).perform(click());
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText(R.string.removeTitleFromList)).perform(click());
        onView(withText(R.string.removingConfirmation)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_yes_button)).check(matches(isDisplayed())).perform(click());

        Thread.sleep(2000);

        Assert.assertFalse(isInList());
        Assert.assertFalse(isInList(MalTitle.COMPLETED));
        Anime anime = dbHelper.getAnimeById(ID);
        Assert.assertNull(anime);

        Espresso.pressBack();

        onView(withText(TITLE)).check(doesNotExist());

        onView(withId(R.id.dl_layout)).perform(DrawerActions.open());
        onView(withId(R.id.tv_completed_count)).check(matches(withText(String.valueOf(completed - 1))));
    }

    private static ViewAction setScore(final int scoreId) {

        return new ViewAction() {

            @Override
            public void perform(UiController uiController, View view) {
                NumberPicker numberPicker = (NumberPicker) view;
                numberPicker.setValue(scoreId);
            }

            @Override
            public String getDescription() {
                return "Set the score into the NumberPicker";
            }

            @Override
            public Matcher<View> getConstraints() {
                return ViewMatchers.isAssignableFrom(NumberPicker.class);
            }
        };
    }

    private boolean isInList(int status) {

        for (Anime anime : manager.getAnimeListByStatus(status)) {
            if (anime.getId() == ID) {
                return true;
            }
        }
        return false;
    }

    private boolean isInList() {

        for (MalTitle anime : manager.getGeneralList(EpisodeType.ANIME)) {
            if (anime.getId() == ID) {
                return true;
            }
        }
        return false;
    }
}
