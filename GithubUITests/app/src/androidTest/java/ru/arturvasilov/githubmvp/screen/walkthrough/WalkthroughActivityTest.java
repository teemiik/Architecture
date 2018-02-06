package ru.arturvasilov.githubmvp.screen.walkthrough;

import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ru.gdgkazan.githubmvp.R;
import ru.gdgkazan.githubmvp.screen.walkthrough.WalkthroughActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * @author Artur Vasilov
 */
@RunWith(AndroidJUnit4.class)
public class WalkthroughActivityTest {

    /**
     * TODO : task
     *
     * Write at least 5 tests for the {@link WalkthroughActivity} class
     * Test UI elements behaviour, new Activity starts and user actions
     */

    @Rule
    public final ActivityTestRule<WalkthroughActivity> mActivityRule = new ActivityTestRule<>(WalkthroughActivity.class);

    @Before
    public void setUp() throws Exception {
        Intents.init();
    }

    @Test
    public void testActionButton() throws Exception {
        onView(withId(R.id.btn_walkthrough)).check(matches(allOf(
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE),
                isClickable(),
                withText(R.string.next_uppercase)
        )));
    }

    @Test
    public void testSwipeBetweenFirstAndSecondPage() {
        onView(ViewMatchers.withId(R.id.pager)).perform(swipeLeft());
    }

    @Test
    public void testSwipeBetweenFirstSecondAndBackToFirstPage() {
        onView(allOf(withId(R.id.benefitText), withText(R.string.benefit_work_together))).check(matches(isDisplayed()));
        onView(withId(R.id.pager)).perform(swipeLeft());
        onView(allOf(withId(R.id.benefitText), withText(R.string.benefit_code_history))).check(matches(isDisplayed()));
        onView(withId(R.id.pager)).perform(swipeRight());
    }

    @Test
    public void testSwipeToTheEnd() {
        onView(allOf(withId(R.id.benefitText), withText(R.string.benefit_work_together))).check(matches(isDisplayed()));
        onView(withId(R.id.pager)).perform(swipeLeft()).perform(swipeLeft()).perform(swipeLeft());
        onView(allOf(withId(R.id.benefitText), withText(R.string.benefit_publish_source))).check(matches(isDisplayed()));
    }

    @Test
    public void testSwipeBeyondTheEnd() {
        onView(allOf(withId(R.id.benefitText), withText(R.string.benefit_work_together))).check(matches(isDisplayed()));
        onView(withId(R.id.pager)).perform(swipeLeft()).perform(swipeLeft()).perform(swipeLeft()).perform(swipeLeft());
        onView(allOf(withId(R.id.benefitText), withText(R.string.benefit_publish_source))).check(matches(isDisplayed()));
    }


    @After
    public void tearDown() throws Exception {
        Intents.release();
    }

}
