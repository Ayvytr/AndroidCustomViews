package com.ayvytr.customview.custom.text;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
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


/**
 * 可清除文本的EditText，清除图标是drawableRight指定的。预览会看不到图，运行起来会显示，直接继承的{@link AppCompatEditText}.
 * 请注意drawableRight是做清除文本的。
 *
 * @author Ayvytr <a href="https://github.com/Ayvytr" target="_blank">'s GitHub</a>
 * @since 0.2.0
 */
public class ClearableEditText extends AppCompatEditText
        implements View.OnTouchListener, View.OnFocusChangeListener, TextWatcher {
    //清除Drawable，直接使用drawableRight设置
    private Drawable mClearTextIcon;
    private OnFocusChangeListener mOnFocusChangeListener;
    private OnTouchListener mOnTouchListener;
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
    public void setOnFocusChangeListener(final OnFocusChangeListener onFocusChangeListener) {
        mOnFocusChangeListener = onFocusChangeListener;
    }

    @Override
    public void setOnTouchListener(final OnTouchListener onTouchListener) {
        mOnTouchListener = onTouchListener;
    }

    private void init(AttributeSet attrs) {
        Drawable[] cd = getCompoundDrawables();
        if(cd[2] == null) {
            mClearTextIcon = DrawableCompat
                    .wrap(ContextCompat.getDrawable(getContext(), R.drawable.ic_text_clear_light));
        } else {
            mClearTextIcon = cd[2];
        }
        DrawableCompat.setTint(mClearTextIcon, getCurrentHintTextColor());
        mClearTextIcon.setBounds(0, 0, mClearTextIcon.getIntrinsicHeight(), mClearTextIcon.getIntrinsicHeight());
        setClearIconVisible(false);
        super.setOnTouchListener(this);
        super.setOnFocusChangeListener(this);
        addTextChangedListener(this);

        int padding = DensityUtil.dp2px(getContext(), 5);
        if(getCompoundDrawablePadding() <= 0) {
            setCompoundDrawablePadding(padding);
        }

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ClearableEditText);
        showClearDrawableNoFocus = a.getBoolean(R.styleable.ClearableEditText_showClearDrawableNoFocus, false);
        a.recycle();
    }

    @Override
    public void onFocusChange(final View view, final boolean hasFocus) {
        setClearIconVisible(showClearDrawableNoFocus ? getText().length() > 0 : hasFocus && getText().length() > 0);
        if(mOnFocusChangeListener != null) {
            mOnFocusChangeListener.onFocusChange(view, hasFocus);
        }
    }

    @Override
    public boolean onTouch(final View view, final MotionEvent motionEvent) {
        final int x = (int) motionEvent.getX();
        if(mClearTextIcon.isVisible() && x > getWidth() - getPaddingRight() - mClearTextIcon.getIntrinsicWidth()) {
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
        if(isFocused()) {
            setClearIconVisible(s.length() > 0);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void afterTextChanged(Editable s) {
    }


    private void setClearIconVisible(boolean visible) {
        mClearTextIcon.setVisible(visible, false);
        Drawable[] compoundDrawables = getCompoundDrawables();
        setCompoundDrawables(compoundDrawables[0], compoundDrawables[1], visible ? mClearTextIcon : null,
                compoundDrawables[3]);
    }
}
