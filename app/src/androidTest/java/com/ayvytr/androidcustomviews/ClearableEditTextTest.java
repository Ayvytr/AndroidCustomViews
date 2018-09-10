package com.ayvytr.androidcustomviews;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.MotionEvent;
import android.widget.EditText;

import com.ayvytr.androidcustomviews.testactivity.ClearableEditTextActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasFocus;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.ayvytr.androidcustomviews.BaseAction.touch;
import static org.hamcrest.CoreMatchers.not;

/**
 * @author ayvytr
 */
@RunWith(AndroidJUnit4.class)
public class ClearableEditTextTest {
    @Rule
    public ActivityTestRule<ClearableEditTextActivity> rule = new ActivityTestRule<>(
            ClearableEditTextActivity.class);

    @Test
    public void testFocus() {
        onView(withId(R.id.et)).check(matches(hasFocus()));

        onView(withId(R.id.et2)).perform(click());

        onView(withId(R.id.et)).check(matches(not(hasFocus())));
    }

    @Test
    public void testClear() {
        onView(withId(R.id.et)).perform(replaceText("狗小妹是坏蛋"), closeSoftKeyboard());
        onView(withId(R.id.et)).check(matches(withText("狗小妹是坏蛋")));

        onView(withId(R.id.et));

        EditText et = rule.getActivity().findViewById(R.id.et);
        int x = et.getWidth() - 30;
        //不先ACTION_DOWN会有空指针问题
        onView(withId(R.id.et)).perform(touch(x, 20, MotionEvent.ACTION_DOWN));
        onView(withId(R.id.et)).perform(touch(x, 20, MotionEvent.ACTION_UP));
        onView(withId(R.id.et)).check(matches(withText("")));
    }
}
