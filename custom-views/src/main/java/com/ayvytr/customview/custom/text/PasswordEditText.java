package com.ayvytr.customview.custom.text;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;

import com.ayvytr.customview.R;

/**
 * @author
 */
public class PasswordEditText extends AppCompatEditText {
    private boolean showDrawableWhenEmptyText;
    private boolean isShow;
    private Drawable showPasswordDrawable;
    private Drawable hidePasswordDrawable;

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
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.PasswordEditText);
        showPasswordDrawable = ta.getDrawable(R.styleable.PasswordEditText_showPasswordDrawable);
        if(showPasswordDrawable == null) {
            showPasswordDrawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_show_password).mutate();
        }
        hidePasswordDrawable = ta.getDrawable(R.styleable.PasswordEditText_hidePasswordDrawable);
        if(hidePasswordDrawable == null) {
            hidePasswordDrawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_hide_password).mutate();
        }

        showDrawableWhenEmptyText = ta.getBoolean(R.styleable.PasswordEditText_showDrawableWhenEmptyText, false);

        ta.recycle();

        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                onPasswordDrawableChanged(!s.toString().isEmpty());
            }
        });

        onPasswordDrawableChanged(!getText().toString().isEmpty());
    }

    private void onPasswordDrawableChanged(boolean isNotEmpty) {
        Drawable[] cs = getCompoundDrawables();
        if(showDrawableWhenEmptyText) {
            setCompoundDrawablesWithIntrinsicBounds(cs[0], cs[1], isShow ? showPasswordDrawable : hidePasswordDrawable, cs[3]);
        } else {
            if(isNotEmpty) {
                setCompoundDrawablesWithIntrinsicBounds(cs[0], cs[1], isShow ? showPasswordDrawable : hidePasswordDrawable, cs[3]);
            } else {
                setCompoundDrawablesWithIntrinsicBounds(cs[0], cs[1], null, cs[3]);
            }
        }
    }
}
