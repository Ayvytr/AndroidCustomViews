package com.ayvytr.androidcustomviews.testactivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ayvytr.androidcustomviews.R
import com.ayvytr.customview.util.inputfilter.DecimalDigitsInputFilter
import kotlinx.android.synthetic.main.activity_input_filter.*

class InputFilterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_filter)
        initView()
    }

    private fun initView() {
        et.filters = arrayOf(DecimalDigitsInputFilter(5))
    }
}
