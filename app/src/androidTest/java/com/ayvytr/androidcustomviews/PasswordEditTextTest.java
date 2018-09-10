package com.ayvytr.androidcustomviews;

import android.graphics.drawable.Drawable;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.ayvytr.androidcustomviews.testactivity.PasswordEditTextActivity;
import com.ayvytr.customview.custom.text.PasswordEditText;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasFocus;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.hamcrest.core.IsNot.not;

/**
 * @author ayvytr
 */
@RunWith(AndroidJUnit4.class)
public class PasswordEditTextTest {
    @Rule
    public ActivityTestRule<PasswordEditTextActivity> rule = new ActivityTestRule<>(PasswordEditTextActivity.class);
    private PasswordEditText et;
    private PasswordEditText et2;

    @Before
    public void findView() {
        et = rule.getActivity().findViewById(R.id.et);
        et2 = rule.getActivity().findViewById(R.id.et);
    }

    @Test
    public void textDefault() {
        onView(withId(R.id.et)).check(matches(withText("")));
        onView(withId(R.id.et2)).check(matches(withText("")));

        onView(withId(R.id.et)).check(matches(hasFocus()));
        onView(withId(R.id.et2)).check(matches(not(hasFocus())));
    }

    @Test
    public void testDrawablePasswordVisibility() {
        Drawable[] cd = et.getCompoundDrawables();
        assertNull(cd[2]);

        Drawable[] cd2 = et2.getCompoundDrawables();
        assertNull(cd2[2]);
    }

    @Test
    public void testInput() {
        onView(withId(R.id.et)).perform(replaceText("edittext"), closeSoftKeyboard());
        assertNotNull(et.getCompoundDrawables()[2]);

        onView(withId(R.id.et2)).perform(replaceText("edittext"), closeSoftKeyboard());
        assertNotNull(et2.getCompoundDrawables()[2]);
    }

    @Test
    public void testFocus()
    {
        rule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                et.setShowDrawableNoFocus(true);
            }
        });
        onView(withId(R.id.et)).perform(replaceText("focus"), closeSoftKeyboard());
        assertNotNull(et.getCompoundDrawables()[2]);

        onView(withId(R.id.et2)).perform(replaceText("focus"), closeSoftKeyboard());
        assertNotNull(et.getCompoundDrawables()[2]);

        rule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                et.setShowDrawableNoFocus(false);
            }
        });
        onView(withId(R.id.et2)).perform(replaceText("focus"), closeSoftKeyboard());

        assertEquals(et.isShowDrawableNoFocus(), false);
    }
}
