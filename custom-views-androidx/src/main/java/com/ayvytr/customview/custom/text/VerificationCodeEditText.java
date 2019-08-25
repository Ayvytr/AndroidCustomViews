package com.ayvytr.customview.custom.text;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Editable;
import android.text.InputType;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.ColorRes;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;

import com.ayvytr.customview.R;
import com.ayvytr.customview.util.DensityUtil;


/**
 * 验证码输入框，类似支付宝6位密码输入框，可自定义输入长度.
 *
 * @author Ayvytr <a href="https://github.com/Ayvytr" target="_blank">'s GitHub</a>
 * @since 1.4.0
 */

public class VerificationCodeEditText extends AppCompatEditText implements TextWatcher {
    public static final String DEFAULT_PASSWORD_TEXT = "*";

    private int mMaxLength;//需要输入的位数
    private int mTextBgMargin;//验证码背景之间的间距
    private int mTextBgSelectedColor;//底部选中的颜色
    private int mTextBgColor;//未选中的颜色
    private float mTextBgStrokeWidth;//底线的宽度

    private OnVerificationCodeChangedListener onCodeChangedListener;
    private int mCurrentPosition = 0;
    private int mEachRectLength = 0;//每个矩形的边长
    private Paint mBottomSelectedPaint;
    private Paint mBottomNormalPaint;
    private int mSelectedTextColor;

    private boolean mIsPassword;
    private String mPasswordText = DEFAULT_PASSWORD_TEXT;

    private TextBgType mTextBgType;

    public enum TextBgType {
        UNDERLINE,
        STROKE,
        FILL;

        public static TextBgType valueOf(int i) {
            switch(i) {
                case 1:
                    return STROKE;
                case 2:
                    return FILL;
                default:
                    return UNDERLINE;
            }
        }
    }

    public VerificationCodeEditText(Context context) {
        this(context, null);
    }

    public VerificationCodeEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerificationCodeEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
        initPaint();
        setFocusableInTouchMode(true);
        super.addTextChangedListener(this);
    }

    /**
     * 初始化paint
     */
    private void initPaint() {
        mBottomSelectedPaint = new Paint();
        mBottomNormalPaint = new Paint();
        mBottomSelectedPaint.setColor(mTextBgSelectedColor);
        mBottomNormalPaint.setColor(mTextBgColor);

        if(mTextBgType == TextBgType.STROKE) {
            mBottomNormalPaint.setStyle(Paint.Style.STROKE);
            mBottomSelectedPaint.setStyle(Paint.Style.STROKE);
            mBottomSelectedPaint.setStrokeWidth(mTextBgStrokeWidth);
            mBottomNormalPaint.setStrokeWidth(mTextBgStrokeWidth);
        } else if(mTextBgType == TextBgType.FILL) {
            mBottomNormalPaint.setStyle(Paint.Style.FILL);
            mBottomSelectedPaint.setStyle(Paint.Style.FILL);
            mBottomSelectedPaint.setStrokeWidth(0);
            mBottomNormalPaint.setStrokeWidth(0);
        } else {
            mBottomSelectedPaint.setStrokeWidth(mTextBgStrokeWidth);
            mBottomNormalPaint.setStrokeWidth(mTextBgStrokeWidth);
            mBottomNormalPaint.setStyle(Paint.Style.STROKE);
            mBottomSelectedPaint.setStyle(Paint.Style.STROKE);
        }
    }

    /**
     * 初始化Attrs
     */
    private void initAttrs(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.VerificationCodeEditText);
        mMaxLength = ta.getInteger(R.styleable.VerificationCodeEditText_android_maxLength, 6);
        if(mMaxLength <= 0) {
            mMaxLength = 6;
        }

        mTextBgMargin = ta.getDimensionPixelSize(R.styleable.VerificationCodeEditText_textBgMargin,
                DensityUtil.dp2px(getContext(), 10));
        mTextBgSelectedColor = ta.getColor(R.styleable.VerificationCodeEditText_textBgSelectedColor,
                getCurrentTextColor());
        mTextBgColor = ta.getColor(R.styleable.VerificationCodeEditText_textBgColor,
                getColor(android.R.color.darker_gray));
        mTextBgStrokeWidth = ta.getDimension(R.styleable.VerificationCodeEditText_textBgStrokeWidth,
                DensityUtil.dp2px(getContext(), 5));
        mSelectedTextColor = ta.getColor(R.styleable.VerificationCodeEditText_selectedTextColor, getCurrentTextColor());
        int inputType = ta.getInt(R.styleable.VerificationCodeEditText_android_inputType, InputType.TYPE_CLASS_NUMBER);
        setInputType(inputType);

        String passwordText = ta.getString(R.styleable.VerificationCodeEditText_passwordText);
        setPasswordText(passwordText);

        Drawable drawable = ta.getDrawable(R.styleable.VerificationCodeEditText_android_background);
        if(drawable == null) {
            setBackgroundDrawable(null);
        } else {
            setBackgroundDrawable(drawable);
        }

        mTextBgType = TextBgType.valueOf(ta.getInt(R.styleable.VerificationCodeEditText_textBgType, 0));

        ta.recycle();

        setCursorVisible(false);
        // force LTR because of bug: https://github.com/JustKiddingBaby/VercodeEditText/issues/4
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            setLayoutDirection(LAYOUT_DIRECTION_LTR);
        }
    }

    private void setPasswordText(String passwordText) {
        if(TextUtils.isEmpty(passwordText) || mPasswordText.equals(passwordText) || passwordText.length() > 1) {
            return;
        }
        mPasswordText = passwordText;
        postInvalidate();
    }


    @Override
    public void setInputType(int type) {
        setPasswordMode((type & InputType.TYPE_NUMBER_VARIATION_PASSWORD) != 0);
    }

    private void setPasswordMode(boolean isPassword) {
        mIsPassword = isPassword;
        if(isPassword) {
            //一定要用super，不然死循环
            super.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD | InputType.TYPE_CLASS_NUMBER);
        } else {
            //一定要用super，不然死循环
            super.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize;
        if(widthMode == MeasureSpec.UNSPECIFIED) {
            widthSize = getSuggestedMinimumWidth();
        } else {
            widthSize = MeasureSpec.getSize(widthMeasureSpec);
        }
        //每个矩形形的宽度
        mEachRectLength = (widthSize - (mTextBgMargin * (mMaxLength - 1))) / mMaxLength;
        //最终的高度
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize;
        if(heightMode == MeasureSpec.EXACTLY) {
            heightSize = MeasureSpec.getSize(heightMeasureSpec);
        } else {
            //建议固定高度，不然按照字体大小动态调整
            int dp50 = DensityUtil.dp2px(getContext(), 50);
            heightSize = (int) Math.max(getTextSize() * 1.5, dp50);
        }
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            requestFocus();
            setSelection(getText().length());
            showKeyBoard(getContext());
            return false;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int length = getText().length();
        mCurrentPosition = length > 0 ? length - 1 : 0;
        int width = mEachRectLength;
        int height = getMeasuredHeight();

        //绘制底线
        for(int i = 0; i < mMaxLength; i++) {
            canvas.save();
            float lineY = height - mTextBgStrokeWidth / 2;
            int start = width * i + i * mTextBgMargin;
            int end = width + start;
            if(i == mCurrentPosition) {
                if(mTextBgType == TextBgType.UNDERLINE) {
                    canvas.drawLine(start, lineY, end, lineY, mBottomSelectedPaint);
                } else {
                    canvas.drawRect(start, 0, end, height, mBottomSelectedPaint);
                }
            } else {
                if(mTextBgType == TextBgType.UNDERLINE) {
                    canvas.drawLine(start, lineY, end, lineY, mBottomNormalPaint);
                } else {
                    canvas.drawRect(start, 0, end, height, mBottomNormalPaint);
                }
            }
            canvas.restore();
        }

        //绘制文字
        String value = getText().toString();
        for(int i = 0; i < value.length(); i++) {
            canvas.save();
            int start = width * i + i * mTextBgMargin;
            float x = start + width / 2;
            TextPaint paint = getPaint();
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setColor(i == mCurrentPosition ? mSelectedTextColor : getCurrentTextColor());
            Paint.FontMetrics fontMetrics = paint.getFontMetrics();
            if(mIsPassword) {
                //绘制密码的*时，直接使用绘制text的baseline，导致*离方框的中心偏离较大
                float baseline = fontMetrics.bottom - fontMetrics.ascent;
                canvas.drawText(mPasswordText, x, baseline, paint);
            } else {
                float baseline = (height - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
                canvas.drawText(String.valueOf(value.charAt(i)), x, baseline, paint);
            }
            canvas.restore();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        postInvalidate();
        if(onCodeChangedListener != null) {
            onCodeChangedListener.onTextChanged(getText(), start, before, count);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        if(getText().length() == mMaxLength) {
            if(onCodeChangedListener != null) {
                onCodeChangedListener.onCompleted(getText());
            }
        } else if(getText().length() > mMaxLength) {
            getText().delete(mMaxLength, getText().length());
        }
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if(focused) {
            setSelection(getText().length());
        }
    }

    public void setMaxLength(int maxLength) {
        mMaxLength = maxLength;
        postInvalidate();
    }

    public void setBottomLineMargin(int margin) {
        if(margin >= 0 && mTextBgMargin != margin) {
            mTextBgMargin = margin;
            postInvalidate();
        }
    }

    public void setBottomSelectedColor(@ColorRes int bottomSelectedColor) {
        mTextBgSelectedColor = getColor(bottomSelectedColor);
        postInvalidate();
    }

    public void setBottomNormalColor(@ColorRes int bottomNormalColor) {
        mTextBgSelectedColor = getColor(bottomNormalColor);
        postInvalidate();
    }

    public void setBottomLineHeight(int bottomLineHeight) {
        this.mTextBgStrokeWidth = bottomLineHeight;
        postInvalidate();
    }

    public void setOnVerificationTextChangedListener(OnVerificationCodeChangedListener listener) {
        this.onCodeChangedListener = listener;
    }

    /**
     * 返回颜色
     */
    private int getColor(@ColorRes int color) {
        return ContextCompat.getColor(getContext(), color);
    }


    public void showKeyBoard(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(this, InputMethodManager.SHOW_FORCED);
    }

    public void setTextBgType(TextBgType type) {
        this.mTextBgType = type;
        initPaint();
        postInvalidate();
    }

    /**
     * 验证码变化时候的监听事件
     */
    public interface OnVerificationCodeChangedListener {

        /**
         * 当验证码变化的时候
         */
        void onTextChanged(CharSequence s, int start, int before, int count);

        /**
         * 输入完毕后的回调
         */
        void onCompleted(CharSequence s);
    }
}
