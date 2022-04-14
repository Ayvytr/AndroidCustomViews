package com.ayvytr.androidcustomviews

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ayvytr.androidcustomviews.testactivity.*
import com.ayvytr.androidcustomviews.testactivity.numberpickerview.NumberPickerActivity
import com.ayvytr.ktx.ui.getContext
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        btnQuickIndexView.setOnClickListener { startActivity(Intent(getContext(), QuickIndexViewActivity::class.java)) }
        btnNumberPickerView.setOnClickListener { startActivity(Intent(getContext(), NumberPickerActivity::class.java)) }
        btnSuperEditText.setOnClickListener { startActivity(Intent(getContext(), SuperEditTextActivity::class.java)) }

        btnClearableEditText.setOnClickListener { startActivity<ClearableEditTextActivity>() }
        btnPasswordEditText.setOnClickListener { startActivity<PasswordEditTextActivity>() }
        btnLoadingView.setOnClickListener { startActivity<StatusViewActivity>() }
        btnInputFilter.setOnClickListener { startActivity<InputFilterActivity>() }
        btnVerificationEditText.setOnClickListener { startActivity<VerificationCodeEditTextActivity>() }
    }
}
