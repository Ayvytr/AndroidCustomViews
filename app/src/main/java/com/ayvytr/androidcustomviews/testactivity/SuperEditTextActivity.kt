package com.ayvytr.androidcustomviews.testactivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ayvytr.androidcustomviews.R
import com.ayvytr.customview.custom.text.SuperEditText

class SuperEditTextActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setTitle(SuperEditText::class.java.simpleName)
        setContentView(R.layout.activity_super_edit_text)
    }
}
