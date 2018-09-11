package com.ayvytr.androidcustomviews.testactivity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View

import com.ayvytr.androidcustomviews.R
import kotlinx.android.synthetic.main.activity_password_edit_text.*

class PasswordEditTextActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_edit_text)
    }

    fun onSwitchDrawable(view: View) {
        et.isShowDrawableNoFocus = !et.isShowDrawableNoFocus
        et2.isShowDrawableNoFocus = !et2.isShowDrawableNoFocus
    }
}
