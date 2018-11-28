package com.ayvytr.androidcustomviews.testactivity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ayvytr.androidcustomviews.R
import com.ayvytr.customview.util.inputfilter.DecimalDigitsInputFilter
import kotlinx.android.synthetic.main.activity_edit_text.*

class EditTextActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_text)
        initView()
    }

    private fun initView() {
        et.filters = arrayOf(DecimalDigitsInputFilter(0))
    }
}
