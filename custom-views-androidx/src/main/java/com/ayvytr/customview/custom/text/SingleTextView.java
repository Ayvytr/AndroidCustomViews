package com.ayvytr.customview.custom.text;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

/**
 * TextView，文字居中，单行，末尾省略.
 *
 * @author Ayvytr <a href="https://github.com/Ayvytr" target="_blank">'s GitHub</a>
 * @since 0.1.0
 */
public class SingleTextView extends AppCompatTextView {
    public SingleTextView(Context context) {
        this(context, null);
    }

    public SingleTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public SingleTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setGravity(Gravity.CENTER);
        setEllipsize(TextUtils.TruncateAt.END);
        setSingleLine(true);
    }
}
