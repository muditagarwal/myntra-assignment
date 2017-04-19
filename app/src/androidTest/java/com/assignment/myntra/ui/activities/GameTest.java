package com.assignment.myntra.ui.activities;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingPolicies;
import android.support.test.espresso.IdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.assignment.myntra.R;
import com.assignment.myntra.utils.ElapsedTimeIdlingResource;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.assignment.myntra.utils.RecyclerViewMatcher.withRecyclerView;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class GameTest {

	@Rule
	public ActivityTestRule<StartAGameActivity> mActivityTestRule = new ActivityTestRule<>(StartAGameActivity.class);

	@Test
	public void sanityTest() {
		// Make sure Espresso does not time out
		IdlingPolicies.setMasterPolicyTimeout(15 * 2, TimeUnit.SECONDS);
		IdlingPolicies.setIdlingResourceTimeout(15 * 2, TimeUnit.SECONDS);

		onView(withId(R.id.start_a_game)).perform(click());
		onView(withId(R.id.progress_layout)).check(matches(isDisplayed()));

		IdlingResource idlingResource = new ElapsedTimeIdlingResource(7000);
		Espresso.registerIdlingResources(idlingResource);
		onView(withId(R.id.timer)).check(matches(isDisplayed()));
		Espresso.unregisterIdlingResources(idlingResource);


		idlingResource = new ElapsedTimeIdlingResource(15000);
		Espresso.registerIdlingResources(idlingResource);
		onView(withId(R.id.game_area_layout)).check(matches(isDisplayed()));
		Espresso.unregisterIdlingResources(idlingResource);

		onView(withRecyclerView(R.id.photo_grid).atPositionOnView(0, R.id.identify_photo)).check(matches(isDisplayed()));
		onView(withRecyclerView(R.id.photo_grid).atPositionOnView(0, R.id.identify_photo)).perform(click());
		onView(withId(R.id.result)).check(matches(isDisplayed()));
		onView(withText("Score\n1")).check(matches(isDisplayed()));
	}
}
