package com.ayvytr.customview.custom.text;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.KeyListener;
import android.text.method.NumberKeyListener;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ayvytr.customview.R;
import com.ayvytr.customview.util.DensityUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 可点击清除按钮清除文本的EditText，以及点击显示密码图标可分别显示/隐藏文字.
 *
 * @author Ayvytr <a href="https://github.com/Ayvytr" target="_blank">'s GitHub</a>
 * @since 0.1.0
 */
public class SuperEditText extends LinearLayout
{
    private EditText etInput;
    private ImageView ivClear;
    private ImageButton ibEye;

    private TextWatcher filterChineseWatcher;
    private boolean showClearIcon;

    public SuperEditText(Context context)
    {
        this(context, null);
    }

    public SuperEditText(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public SuperEditText(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs)
    {
        View.inflate(getContext(), R.layout.layout_clearable_edittext, this);
        etInput = findViewById(R.id.et_input);
        ivClear = findViewById(R.id.iv_clear);
        ibEye = findViewById(R.id.ib_eye_pwd);


        ivClear.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                etInput.setText(null);
            }
        });

        if(attrs != null)
        {
            TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.SuperEditText);
            setEnabled(ta.getBoolean(R.styleable.SuperEditText_android_enabled, true));
            etInput.setText(ta.getString(R.styleable.SuperEditText_android_text));

            final int inputType = ta.getInt(R.styleable.SuperEditText_android_inputType, InputType.TYPE_CLASS_TEXT);
            etInput.setInputType(inputType);

            boolean showEyeIcon = ta.getBoolean(R.styleable.SuperEditText_switchShowPassword, false);
            showEyeIcon(showEyeIcon);

            etInput.setHint(ta.getString(R.styleable.SuperEditText_android_hint));
            int color = ta.getColor(R.styleable.SuperEditText_android_textColor, -1);
            if(color != -1)
            {
                etInput.setTextColor(color);
            }
            color = ta.getColor(R.styleable.SuperEditText_android_textColorHint, -1);
            if(color != -1)
            {
                etInput.setHintTextColor(color);
            }

            Drawable drawable = ta.getDrawable(R.styleable.SuperEditText_android_background);
            if(drawable != null)
            {
                etInput.setBackgroundDrawable(drawable);
            }

            //输入长度限制
            int maxLength = ta.getInt(R.styleable.SuperEditText_android_maxLength, -1);
            if(maxLength >= 0)
            {
                etInput.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
            }
            else
            {
                etInput.setFilters(new InputFilter[0]);
            }

            int paddingTop, paddingRight, paddingLeft = 0, paddingBottom;
            int tempPadding = ta.getDimensionPixelSize(R.styleable.SuperEditText_textPadding,
                    DensityUtil.dp2px(getContext(), -1));
            if(tempPadding != -1)
            {
                paddingBottom = paddingLeft = paddingRight = paddingTop = tempPadding;
            }

            tempPadding = ta.getDimensionPixelSize(R.styleable.SuperEditText_textPaddingLeft, -1);
            if(tempPadding != -1)
            {
                paddingLeft = tempPadding;
            }
            paddingRight = ta.getDimensionPixelSize(R.styleable.SuperEditText_textPaddingRight, -1);
            if(tempPadding != -1)
            {
                paddingRight = tempPadding;
            }
            paddingTop = ta.getDimensionPixelSize(R.styleable.SuperEditText_textPaddingTop, -1);
            if(tempPadding != -1)
            {
                paddingTop = tempPadding;
            }
            paddingBottom = ta.getDimensionPixelSize(R.styleable.SuperEditText_textPaddingBottom, -1);
            if(tempPadding != -1)
            {
                paddingBottom = tempPadding;
            }
            etInput.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);

            //输入内容限制
            final String digits = ta.getString(R.styleable.SuperEditText_android_digits);
            if(digits != null)
            {
                etInput.setKeyListener(new NumberKeyListener()
                {
                    @NonNull
                    @Override
                    protected char[] getAcceptedChars()
                    {
                        return digits.toCharArray();
                    }

                    @Override
                    public int getInputType()
                    {
                        return InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;
                    }
                });
            }

            showClearIcon = ta.getBoolean(R.styleable.SuperEditText_showClearIcon, true);
            if(!showClearIcon)
            {
                ivClear.setVisibility(View.GONE);
            }
            addClearTextWatcher();

            boolean filterChinese = ta.getBoolean(R.styleable.SuperEditText_filterChinese, false);
            if(filterChinese)
            {
                addChineseTextWatcher();
            }

            drawable = ta.getDrawable(R.styleable.SuperEditText_clearIcon);
            if(drawable != null)
            {
                ivClear.setImageDrawable(drawable);
            }

            ta.recycle();

            etInput.setEnabled(isEnabled());
            ivClear.setEnabled(isEnabled());
        }
    }

    private void addClearTextWatcher()
    {
        TextWatcher textWatcher = new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                if(showClearIcon)
                {
                    boolean isEmpty = s.toString().isEmpty();
                    ivClear.setVisibility(isEmpty ? View.INVISIBLE : View.VISIBLE);
                    ivClear.setClickable(!isEmpty);
                }
            }
        };
        etInput.addTextChangedListener(textWatcher);
    }

    /**
     * 设置焦点变化监听器
     *
     * @param l {@link android.view.View.OnFocusChangeListener}
     */
    public void setFocusChangeListener(OnFocusChangeListener l)
    {
        etInput.setOnFocusChangeListener(l);
    }

    /**
     * 显示/隐藏显示密码图标
     *
     * @param showEyeIcon {@code true} 显示
     */
    private void showEyeIcon(boolean showEyeIcon)
    {
        if(showEyeIcon)
        {
            ibEye.setVisibility(VISIBLE);
            ibEye.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    ibEye.setSelected(!ibEye.isSelected());
                    etInput.setTransformationMethod(
                            ibEye.isSelected() ? HideReturnsTransformationMethod.getInstance() :
                                    PasswordTransformationMethod.getInstance());
                    etInput.setSelection(etInput.getText().length());
                }
            });
        }
        else
        {
            ibEye.setVisibility(GONE);
        }
    }

    /**
     * 添加中文字符过滤功能
     */
    private void addChineseTextWatcher()
    {
        if(filterChineseWatcher == null)
        {
            filterChineseWatcher = new TextWatcher()
            {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after)
                {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count)
                {
                    String editable = etInput.getText().toString();
                    String str = filterChinese(editable);
                    if(!editable.equals(str))
                    {
                        etInput.setText(str);
                        //设置新的光标所在位置
                        etInput.setSelection(str.length());
                    }
                }

                @Override
                public void afterTextChanged(Editable s)
                {
                    ivClear.setVisibility(s.toString().isEmpty() ? View.GONE : View.VISIBLE);
                }

            };
        }
        etInput.removeTextChangedListener(filterChineseWatcher);
        etInput.addTextChangedListener(filterChineseWatcher);
    }


    @Override
    public void setEnabled(boolean enabled)
    {
        super.setEnabled(enabled);
        etInput.setEnabled(isEnabled());
        ivClear.setEnabled(isEnabled());
        ibEye.setEnabled(isEnabled());
    }

    /**
     * 获取当前文本
     *
     * @return 当前输入的文本
     */
    public String getText()
    {
        return etInput.getText().toString();
    }

    /**
     * 设置文本
     *
     * @param text 要显示的文本
     */
    public void setText(String text)
    {
        etInput.setText(text);
    }

    /**
     * 设置文本
     *
     * @param id 字符串id
     */
    public void setText(@StringRes int id)
    {
        etInput.setText(id);
    }

    /**
     * 设置提示文本
     *
     * @param hint 要显示的提示文本
     */
    public void setHint(String hint)
    {
        etInput.setHint(hint);
    }

    /**
     * 设置提示文本
     *
     * @param id 要显示的提示文本字符串id
     */
    public void setHint(@StringRes int id)
    {
        etInput.setHint(id);
    }

    /**
     * 设置清除按钮图标
     *
     * @param drawable 要设置的图标
     */
    public void setClearIcon(Drawable drawable)
    {
        ivClear.setImageDrawable(drawable);
    }

    /**
     * 设置清除按钮图标
     *
     * @param id 图片资源id
     */
    public void setClearIcon(@DrawableRes int id)
    {
        ivClear.setImageResource(id);
    }

    /**
     * 添加文本变化监听器
     *
     * @param watcher {@link TextWatcher}
     */
    public void addTextChangedListener(TextWatcher watcher)
    {
        etInput.addTextChangedListener(watcher);
    }

    /**
     * 设置光标位置
     *
     * @param index 光标位置索引
     */
    public void setSelection(int index)
    {
        etInput.setSelection(index);
    }

    /**
     * 设置按键监听器
     *
     * @param keyListener {@link KeyListener}
     */
    public void setKeyListener(KeyListener keyListener)
    {
        etInput.setKeyListener(keyListener);
    }

    /**
     * 过滤中文的方法
     */
    protected String filterChinese(String str)
    {
        try
        {
            String regEx = "[^\u4E00-\u9FA5]";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(str);
            return m.replaceAll("").trim();
        } catch(Exception e)
        {
            return str;
        }
    }
}
