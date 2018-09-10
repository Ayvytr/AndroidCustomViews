package com.ayvytr.androidcustomviews.testactivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ayvytr.androidcustomviews.R;
import com.ayvytr.customview.custom.text.PasswordEditText;

public class PasswordEditTextActivity extends AppCompatActivity {

    private PasswordEditText et;
    private PasswordEditText et2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_edit_text);
        initView();
    }

    private void initView() {
        et = findViewById(R.id.et);
        et2 = findViewById(R.id.et2);
    }

    public void onSwitchDrawable(View view) {
        et.setShowDrawableNoFocus(!et.isShowDrawableNoFocus());
    }
}
