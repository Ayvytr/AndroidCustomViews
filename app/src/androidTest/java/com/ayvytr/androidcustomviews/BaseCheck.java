package com.ayvytr.androidcustomviews;

import android.support.annotation.IdRes;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isSelected;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

public class BaseCheck {

    /**
     * check 某个控件是否显示
     */
    @Deprecated
    public static void checkDisplay(Object id) {
        checkDisplay(id,true);
    }

    public static void checkDisplay(Object id,boolean check) {
        checkWith(id,isDisplayed(),check);
    }

    /**
     * check Toast是否显示文字
     * @param text
     * @param activityTestRule
     */
    public static void checkToast(String text, ActivityTestRule activityTestRule) {
        //BaseView.getView(text)
          //      .inRoot(withDecorView(not(activityTestRule.getActivity().getWindow().getDecorView())))
          //      .check(matches(isDisplayed()));
    }

    /**
     *check 是否包含某些文字
     * @param id
     * @param text
     */
    public static void checkContainsText(Object id, String text,boolean check) {
        checkWith(id,withText(containsString(text)),check);
    }

    /**
     * check list的item布局中的某个控件是否包含文字
     * @param id list的ID
     * @param child 需要check的那个控件的ID
     * @param itemPosition item的index
     * @param text 所包含的文字
     * @param check 是否包含
     */
    public static void checkListChildContainsText(@IdRes int id, @IdRes int child, int itemPosition, String text, boolean check) {
        checkListChild(id, child, itemPosition, withText(containsString(text)), check);
    }

    /**
     * check 是否选中
     */
    public static void checkSelected(Object id, boolean check) {
        checkWith(id,isSelected(),check);
    }

    /**
     * 是否存在
     * @param id
     * @param check
     */
    public static void checkExist(Object id, boolean check){
        if(check){
            checkWith(id,anyOf(isDisplayed(),not(isDisplayed())),true);
        }else{
            BaseView.getView(id).check(doesNotExist());
        }
    }

    /**
     * list item是否包含某项数据
     * @param id
     * @param data
     * @param check
     * @param <T>
     */
    public static <T> void checkListContainsText(Object id,MatchListAdapter<T> data,boolean check){
        checkWith(id,withAdaptedData(data),check);
    }
    private static <T> Matcher<View> withAdaptedData(final MatchListAdapter<T> data) {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("with class name: ");
            }
            @Override
            public boolean matchesSafely(View view) {
                if (!(view instanceof AdapterView)) {
                    return false;
                }
                @SuppressWarnings("rawtypes")
                Adapter adapter = ((AdapterView) view).getAdapter();
                for (int i = 0; i < adapter.getCount(); i++) {
                    if(data.isMatches((T) adapter.getItem(i))){
                        return true;
                    }
                }
                return false;
            }
        };
    }
    public  interface MatchListAdapter<T>{
        boolean isMatches(T item);
    }

    /**
     * 检查recycler是否包含某项数据
     */

    public static void checkRecyclerItemContainText(Object id, int itemPosition, final int childId, String text, boolean isCheck){
        final View[] childView = {null};
        BaseView.getView(id).perform(RecyclerViewActions.actionOnItemAtPosition(itemPosition, new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return BaseCheck.anyView();
            }

            @Override
            public String getDescription() {
                return "";
            }

            @Override
            public void perform(UiController uiController, View view) {
                childView[0] =view;
            }
        }));
    }

    public static <T> void checkRecyclerText(Object id,MatchListAdapter<T> match,boolean check){
        checkWith(id,withRecyclerAdaptedData(match),check);
    }
    private static <T> Matcher<View> withRecyclerAdaptedData(final MatchListAdapter<T> data) {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("find recycler view ");
            }
            @Override
            public boolean matchesSafely(View view) {
                if (!(view instanceof RecyclerView)) {
                    return false;
                }
                for (int i = 0; i < ((RecyclerView) view).getAdapter().getItemCount(); i++) {
                    if(data.isMatches((T) ((RecyclerView) view).getChildViewHolder((((RecyclerView) view).getChildAt(i))))){
                        return true;
                    }
                }
                return false;
            }
        };
    }

    /**
     * 原子操作
     * @param id
     * @param matcher
     * @param check
     */
    private static void checkWith(Object id,Matcher matcher,boolean check){
        if(!check){
            matcher=not(matcher);
        }
        BaseView.getView(id).check(matches(matcher));
    }

    private static void checkListChild(@IdRes int id, @IdRes int child, int itemPosition, Matcher matcher,boolean check) {
        if(!check){
            matcher=not(matcher);
        }
        BaseView.getListItemView(id, itemPosition).onChildView(withId(child)).check(matches(matcher));
    }

    public static Matcher<View> anyView(){
        return anyOf(isDisplayed(),not(isDisplayed()));
    }
}

