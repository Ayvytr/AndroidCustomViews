package com.ayvytr.androidcustomviews

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ayvytr.androidcustomviews.testactivity.QuickIndexViewActivity
import com.ayvytr.androidcustomviews.testactivity.SuperEditTextActivity
import com.ayvytr.androidcustomviews.testactivity.numberpickerview.NumberPickerActivity
import com.ayvytr.easykotlin.android.ui.getContext
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity()
{

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView()
    {
        btnQuickIndexView.setOnClickListener { startActivity(Intent(getContext(), QuickIndexViewActivity::class.java)) }
        btnNumberPickerView.setOnClickListener { startActivity(Intent(getContext(), NumberPickerActivity::class.java)) }
        btnSuperEditText.setOnClickListener { startActivity(Intent(getContext(), SuperEditTextActivity::class.java)) }
    }
}
