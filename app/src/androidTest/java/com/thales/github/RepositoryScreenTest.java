package com.thales.github;

import android.content.Intent;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.squareup.okhttp.mockwebserver.Dispatcher;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;
import com.thales.github.model.repository.Repository;
import com.thales.github.ui.repository.RepositoryActivity;
import com.thales.github.utilities.Constants;
import com.thales.github.utilities.RestServiceTestHelper;
import com.thales.github.utilities.ViewVisibilityIdlingResource;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.registerIdlingResources;
import static android.support.test.espresso.Espresso.unregisterIdlingResources;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.intent.Checks.checkNotNull;
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
            = new ActivityTestRule<>(RepositoryActivity.class, true, false);

    private MockWebServer server;
    private static final Repository REPOSITORY_OK_HTTP = new Repository();
    private static final Repository REPOSITORY_PICASSO = new Repository();


    @Before
    public void setUp() throws Exception {
        server = new MockWebServer();
        server.start();

        Constants.INSTANCE.setGITHUB_API_URL(server.url("/").toString());

        server.setDispatcher(dispatcher);

        initRepositoryOKHttp();
        initRepositoryPicasso();
    }

    private void initRepositoryOKHttp() {
        REPOSITORY_OK_HTTP.setName("okhttp");
        REPOSITORY_OK_HTTP.setForksCount(4548);
        REPOSITORY_OK_HTTP.setStarsCount(17956);
    }

    private void initRepositoryPicasso() {
        REPOSITORY_PICASSO.setName("picasso");
        REPOSITORY_PICASSO.setForksCount(3450);
        REPOSITORY_PICASSO.setStarsCount(12798);
    }

    @Test
    public void showRepositories() throws Exception {
//        String fileName = "repositories_response.json";
//
//        server.enqueue(new MockResponse()
//                .setResponseCode(200)
//                .setBody(RestServiceTestHelper.getStringFromFile(getInstrumentation().getContext(), fileName)));

        Intent intent = new Intent();
        mActivityTestRule.launchActivity(intent);

        SystemClock.sleep(1000);

        View view = mActivityTestRule.getActivity().findViewById(R.id.progress_bar);
        ViewVisibilityIdlingResource resource = new ViewVisibilityIdlingResource(view, View.GONE);
        registerIdlingResources(resource);

        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())));
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()));

        onView(withId(R.id.recycler_view))
                .perform(scrollToPosition(14))
                .check(matches(atPosition(14, hasDescendant(withText(REPOSITORY_PICASSO.getName())))));

        onView(withId(R.id.recycler_view))
                .perform(scrollToPosition(14))
                .check(matches(atPosition(14, hasDescendant(withText(String.valueOf(REPOSITORY_PICASSO.getForksCount()))))));

        onView(withId(R.id.recycler_view))
                .perform(scrollToPosition(14))
                .check(matches(atPosition(14, hasDescendant(withText(String.valueOf(REPOSITORY_PICASSO.getStarsCount()))))));

        unregisterIdlingResources(resource);
        SystemClock.sleep(1000);
    }

    public static Matcher<View> atPosition(final int position, @NonNull final Matcher<View> itemMatcher) {
        checkNotNull(itemMatcher);
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("has item at position " + position + ": ");
                itemMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(final RecyclerView view) {
                RecyclerView.ViewHolder viewHolder = view.findViewHolderForAdapterPosition(position);
                if (viewHolder == null) {
                    // has no item on such position
                    return false;
                }
                return itemMatcher.matches(viewHolder.itemView);
            }
        };
    }

    @Test
    public void clickRepositoryItem_ShowsPullRequestScreen() throws Exception {

        Intent intent = new Intent();
        mActivityTestRule.launchActivity(intent);
        SystemClock.sleep(1000);

        View view = mActivityTestRule.getActivity().findViewById(R.id.progress_bar);
        ViewVisibilityIdlingResource resource = new ViewVisibilityIdlingResource(view, View.GONE);
        registerIdlingResources(resource);
        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())));
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()));
        onView(withId(R.id.recycler_view))
                .perform(RecyclerViewActions.actionOnItem(
                        hasDescendant(withText(REPOSITORY_PICASSO.getName())),
                        click()));
        SystemClock.sleep(1000);
        unregisterIdlingResources(resource);

        onView(withText(REPOSITORY_PICASSO.getName())).check(matches(withParent(withId(R.id.toolbar))));
        onView(withId(R.id.recycler_view))
                .perform(scrollToPosition(14))
                .check(matches(atPosition(14, hasDescendant(withText("mattprecious")))));
        SystemClock.sleep(1000);
    }


    private final Dispatcher dispatcher = new Dispatcher() {

        @Override
        public MockResponse dispatch(RecordedRequest request) throws InterruptedException {

            try {
                if (request.getPath().contains("search/repositories")) {
                    String fileName = "repositories_response.json";
                    return new MockResponse()
                            .setResponseCode(200)
                            .setBody(RestServiceTestHelper.getStringFromFile(getInstrumentation().getContext(), fileName));
                } else if (request.getPath().contains("pulls")) {
                    String fileName = "pulls_picasso_response.json";
                    return new MockResponse()
                            .setResponseCode(200)
                            .setBody(RestServiceTestHelper.getStringFromFile(getInstrumentation().getContext(), fileName));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return new MockResponse().setResponseCode(404);
        }
    };
}
