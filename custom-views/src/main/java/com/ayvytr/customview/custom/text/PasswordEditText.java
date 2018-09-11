package com.ayvytr.customview.custom.text;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.inputmethod.EditorInfo;

import com.ayvytr.customview.R;
import com.ayvytr.customview.util.DensityUtil;

/**
 * @author Ayvytr <a href="https://github.com/Ayvytr" target="_blank">'s GitHub</a>
 * @since 0.2.0
 */
public class PasswordEditText extends AppCompatEditText {
    private int defaultPwdDrawableArea;

    //Drawable是否正在显示
    private boolean isDrawableShowing;
    //当前显示的文本是：密码/文本
    private boolean isShowingPwd;

    //显示/隐藏密码的Drawable
    private Drawable showPasswordDrawable;
    private Drawable hidePasswordDrawable;

    private boolean handlingHoverEvent;

    //DrawableRight点击模式, true:点击模式 false:触摸模式，DrawableRight按下显示抬起隐藏密码
    private boolean clickMode;

    //没有焦点时是否显示Drawable
    private boolean showDrawableNoFocus;


    public PasswordEditText(Context context) {
        this(context, null);
    }

    public PasswordEditText(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.editTextStyle);
    }

    public PasswordEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        defaultPwdDrawableArea = DensityUtil.dp2px(getContext(), 32);

        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.PasswordEditText);
        showPasswordDrawable = ta.getDrawable(R.styleable.PasswordEditText_showPasswordDrawable);
        if(showPasswordDrawable == null) {
            showPasswordDrawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_show_password);
        }
        hidePasswordDrawable = ta.getDrawable(R.styleable.PasswordEditText_hidePasswordDrawable);
        if(hidePasswordDrawable == null) {
            hidePasswordDrawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_hide_password);
        }

        clickMode = ta.getBoolean(R.styleable.PasswordEditText_clickMode, false);
        showDrawableNoFocus = ta.getBoolean(R.styleable.PasswordEditText_showDrawableNoFocus, false);

        ta.recycle();

        if(!isPasswordInputType(getInputType())) {
            setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }

        int defaultRightPadding = DensityUtil.dp2px(getContext(), 5);
        if(getPaddingRight() < defaultRightPadding) {
            setPadding(getPaddingLeft(), getPaddingTop(), defaultRightPadding, getPaddingBottom());
        }

        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                onPasswordDrawableChanged();
            }
        });

        onPasswordDrawableChanged();
    }

    public void setShowPasswordDrawable(Drawable showPasswordDrawable) {
        this.showPasswordDrawable = showPasswordDrawable;
    }

    public void setHidePasswordDrawable(Drawable hidePasswordDrawable) {
        this.hidePasswordDrawable = hidePasswordDrawable;
    }

    public void setClickMode(boolean clickMode) {
        if(this.clickMode != clickMode) {
            this.clickMode = clickMode;
        }
    }

    public void setShowDrawableNoFocus(boolean showDrawableNoFocus) {
        if(this.showDrawableNoFocus != showDrawableNoFocus) {
            this.showDrawableNoFocus = showDrawableNoFocus;
            onPasswordDrawableChanged();
        }
    }

    public boolean isShowDrawableNoFocus() {
        return showDrawableNoFocus;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int iconXRect = getWidth() - getPaddingRight() - defaultPwdDrawableArea;
        if(!isDrawableShowing || x < iconXRect) {
            return super.onTouchEvent(event);
        }

        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(!clickMode) {
                    //支持按下立即显示密码，松开立即隐藏密码
                    // prevent keyboard from coming up
                    event.setAction(MotionEvent.ACTION_CANCEL);
                    handlingHoverEvent = true;
                    handlePasswordVisibility();
                }
                break;
            case MotionEvent.ACTION_UP:
                if(!clickMode) {
                    if(handlingHoverEvent) {
                        // prevent keyboard from coming up
                        event.setAction(MotionEvent.ACTION_CANCEL);
                        handlingHoverEvent = false;
                        handlePasswordVisibility();
                    }
                } else {
                    handlePasswordVisibility();
                }
                break;
            default:
                break;
        }

        return super.onTouchEvent(event);
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        onPasswordDrawableChanged();
    }

    private void handlePasswordVisibility() {
        isShowingPwd = !isShowingPwd;
        onPasswordDrawableChanged();

        setTransformationMethod(isShowingPwd ? null : PasswordTransformationMethod.getInstance());
        setSelection(getText().length());
    }


    public static boolean isPasswordInputType(int inputType) {
        final int variation =
                inputType & (EditorInfo.TYPE_MASK_CLASS | EditorInfo.TYPE_MASK_VARIATION);
        return variation
                == (EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_PASSWORD)
                || variation
                == (EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_WEB_PASSWORD)
                || variation
                == (EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_VARIATION_PASSWORD);
    }

    private void onPasswordDrawableChanged() {
        boolean isNotEmpty = !getText().toString().isEmpty();
        this.isDrawableShowing = isNotEmpty && (showDrawableNoFocus || hasFocus());

        Drawable[] cs = getCompoundDrawables();
        if(isDrawableShowing) {
            setCompoundDrawablesWithIntrinsicBounds(cs[0], cs[1],
                    isShowingPwd ? showPasswordDrawable : hidePasswordDrawable, cs[3]);
        } else {
            setCompoundDrawablesWithIntrinsicBounds(cs[0], cs[1], null, cs[3]);
        }
    }
}
