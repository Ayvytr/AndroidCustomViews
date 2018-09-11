package com.ayvytr.androidcustomviews.testactivity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View

import com.ayvytr.androidcustomviews.R
import com.ayvytr.customview.custom.text.PasswordEditText
import kotlinx.android.synthetic.main.activity_password_edit_text.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class PasswordEditTextActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_edit_text)
        initView()
    }

    private fun initView() {
        title = PasswordEditText::class.java.simpleName
        btnChangeDrawable.onClick {
            et.setShowPasswordDrawable(android.R.drawable.ic_menu_search)
            et.setHidePasswordDrawable(android.R.drawable.ic_menu_save)
        }
    }

    fun onSwitchDrawable(view: View) {
        et.isShowDrawableNoFocus = !et.isShowDrawableNoFocus
        et2.isShowDrawableNoFocus = !et2.isShowDrawableNoFocus
    }
}
