package com.ayvytr.customview.custom.text;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.ayvytr.customview.R;
import com.ayvytr.customview.util.DensityUtil;
import com.ayvytr.customview.util.ResUtil;


/**
 * 可清除文本的EditText，清除图标是drawableRight指定的。预览会看不到图，运行起来会显示，直接继承的{@link AppCompatEditText}.
 * 请注意drawableRight是做清除文本的。
 *
 * @author Ayvytr <a href="https://github.com/Ayvytr" target="_blank">'s GitHub</a>
 * @since 0.2.0
 */
public class ClearableEditText extends AppCompatEditText
        implements View.OnTouchListener, TextWatcher {
    //清除Drawable，直接使用drawableRight设置
    private Drawable clearTextDrawable;
    private OnTouchListener mOnTouchListener;

    //是否在没有焦点时显示清除图标
    private boolean showClearDrawableNoFocus;

    public ClearableEditText(Context context) {
        this(context, null);
    }

    public ClearableEditText(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.editTextStyle);
    }

    public ClearableEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @Override
    public void setOnTouchListener(final OnTouchListener onTouchListener) {
        mOnTouchListener = onTouchListener;
    }

    private void init(AttributeSet attrs) {
        Drawable[] cd = getCompoundDrawables();
        if(cd[2] == null) {
            clearTextDrawable = DrawableCompat
                    .wrap(ContextCompat.getDrawable(getContext(), R.drawable.ic_text_clear_light));
        } else {
            clearTextDrawable = cd[2];
        }
        DrawableCompat.setTint(clearTextDrawable, getCurrentHintTextColor());
        clearTextDrawable
                .setBounds(0, 0, clearTextDrawable.getIntrinsicHeight(), clearTextDrawable.getIntrinsicHeight());
        super.setOnTouchListener(this);
        addTextChangedListener(this);

        int padding = DensityUtil.dp2px(getContext(), 5);
        if(getCompoundDrawablePadding() <= 0) {
            setCompoundDrawablePadding(padding);
        }

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ClearableEditText);
        showClearDrawableNoFocus = a.getBoolean(R.styleable.ClearableEditText_showClearDrawableNoFocus, false);
        a.recycle();

        onClearIconVisibleChanged();
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        onClearIconVisibleChanged();
    }

    @Override
    public boolean onTouch(final View view, final MotionEvent motionEvent) {
        final int x = (int) motionEvent.getX();
        if(clearTextDrawable.isVisible() && x > getWidth() - getPaddingRight() - clearTextDrawable
                .getIntrinsicWidth()) {
            if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                setError(null);
                setText("");
            }
            return true;
        }
        return mOnTouchListener != null && mOnTouchListener.onTouch(view, motionEvent);
    }

    @Override
    public final void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        onClearIconVisibleChanged();
    }


    private void onClearIconVisibleChanged() {
        boolean visible = showClearDrawableNoFocus ? getText().length() > 0 : isFocused() && getText().length() > 0;
        clearTextDrawable.setVisible(visible, false);
        Drawable[] compoundDrawables = getCompoundDrawables();
        setCompoundDrawables(compoundDrawables[0], compoundDrawables[1], visible ? clearTextDrawable : null,
                compoundDrawables[3]);
    }

    /**
     * 获取清除Drawable
     *
     * @return {@link #clearTextDrawable}
     */
    public Drawable getClearTextDrawable() {
        return clearTextDrawable;
    }

    /**
     * 设置清除Drawable
     *
     * @param clearTextDrawable 要设置的Drawable
     */
    public void setClearTextDrawable(@NonNull Drawable clearTextDrawable) {
        if(clearTextDrawable == null || this.clearTextDrawable == clearTextDrawable) {
            return;
        }

        this.clearTextDrawable = clearTextDrawable;
        clearTextDrawable
                .setBounds(0, 0, clearTextDrawable.getIntrinsicHeight(), clearTextDrawable.getIntrinsicHeight());
        onClearIconVisibleChanged();
    }

    /**
     * 设置清除Drawable
     *
     * @param drawableId Drawable Res Id
     */
    public void setClearTextDrawable(@DrawableRes int drawableId) {
        this.clearTextDrawable = ResUtil.getDrawable(getContext(), drawableId);
        clearTextDrawable
                .setBounds(0, 0, clearTextDrawable.getIntrinsicHeight(), clearTextDrawable.getIntrinsicHeight());
        onClearIconVisibleChanged();
    }

    /**
     * 没有焦点时是否显示清除Drawable
     *
     * @return {@link #showClearDrawableNoFocus}
     */
    public boolean isShowClearDrawableNoFocus() {
        return showClearDrawableNoFocus;
    }

    /**
     * 设置没有焦点时是否显示清除Drawable
     *
     * @param showClearDrawableNoFocus {@code true} 显示. {@code false} 不显示.
     */
    public void setShowClearDrawableNoFocus(boolean showClearDrawableNoFocus) {
        if(this.showClearDrawableNoFocus != showClearDrawableNoFocus) {
            this.showClearDrawableNoFocus = showClearDrawableNoFocus;
            onClearIconVisibleChanged();
        }
    }
}
