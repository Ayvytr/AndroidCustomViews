package com.ayvytr.androidcustomviews.testactivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.ayvytr.androidcustomviews.R;
import com.ayvytr.customview.custom.text.ClearableEditText;

public class ClearableEditTextActivity extends AppCompatActivity {

    private ClearableEditText et;
    private TextView tvFocus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clearable_edit_text);
        initView();
    }

    private void initView() {
        et = findViewById(R.id.et);
        tvFocus = findViewById(R.id.tvFocus);
        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                tvFocus.setText("是否获得焦点:" + hasFocus);
            }
        });
    }
}
