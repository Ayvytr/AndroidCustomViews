package com.ayvytr.androidcustomviews.testactivity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.ayvytr.androidcustomviews.R
import com.ayvytr.customview.custom.text.ClearableEditText
import kotlinx.android.synthetic.main.activity_clearable_edit_text.*

class ClearableEditTextActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clearable_edit_text)
        initView()
    }

    private fun initView() {
        setTitle(ClearableEditText::class.java.simpleName)
        et.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus -> tvFocus.text = "是否获得焦点:$hasFocus" }
        btnChangeDrawable.setOnClickListener {
            et.setClearTextDrawable(android.R.drawable.ic_menu_search)
        }
    }
}
