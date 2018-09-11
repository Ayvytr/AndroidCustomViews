package com.ayvytr.androidcustomviews.testactivity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.ayvytr.androidcustomviews.R
import kotlinx.android.synthetic.main.activity_clearable_edit_text.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class ClearableEditTextActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clearable_edit_text)
        initView()
    }

    private fun initView() {
        et.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus -> tvFocus.text = "是否获得焦点:$hasFocus" }
        btnChangeDrawable.onClick {
            et.setClearTextDrawable(android.R.drawable.ic_menu_search)
        }
    }
}
