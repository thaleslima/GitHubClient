package com.thales.github;

import android.os.SystemClock;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.thales.github.ui.repository.RepositoryActivity;
import com.thales.github.utilities.ViewVisibilityIdlingResource;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.registerIdlingResources;
import static android.support.test.espresso.Espresso.unregisterIdlingResources;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;

@RunWith(AndroidJUnit4.class)
public class RepositoryScreenTest {

    @Rule
    public final ActivityTestRule<RepositoryActivity> mActivityTestRule
            = new ActivityTestRule<>(RepositoryActivity.class);

    @Test
    public void showRepositories() {
        View view = mActivityTestRule.getActivity().findViewById(R.id.progress_bar);
        ViewVisibilityIdlingResource resource = new ViewVisibilityIdlingResource(view, View.GONE);
        registerIdlingResources(resource);
        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())));
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()));
        unregisterIdlingResources(resource);

        SystemClock.sleep(1000);
    }


    @Test
    public void clickRepositoryItem_ShowsPullRequestScreen() {
        View view = mActivityTestRule.getActivity().findViewById(R.id.progress_bar);
        ViewVisibilityIdlingResource resource = new ViewVisibilityIdlingResource(view, View.GONE);
        registerIdlingResources(resource);
        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())));
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()));
        unregisterIdlingResources(resource);

        SystemClock.sleep(1000);

        onView(withId(R.id.recycler_view))
                .perform(RecyclerViewActions.actionOnItem(
                        hasDescendant(withText("retrofit")),
                        click()));

        SystemClock.sleep(1000);

        onView(withText("retrofit")).check(matches(withParent(withId(R.id.toolbar))));

        SystemClock.sleep(1000);
    }
}
