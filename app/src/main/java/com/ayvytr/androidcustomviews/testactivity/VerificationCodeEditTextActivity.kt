package com.ayvytr.androidcustomviews.testactivity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ayvytr.androidcustomviews.R
import com.ayvytr.customview.custom.text.VerificationCodeEditText
import kotlinx.android.synthetic.main.activity_verification_code_edittext.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast
import kotlin.random.Random

class VerificationCodeEditTextActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "VerificationCodeEditText"
        setContentView(R.layout.activity_verification_code_edittext)
        vc_et.setOnVerificationTextChangedListener(
            object : VerificationCodeEditText.OnVerificationCodeChangedListener {
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun onCompleted(s: CharSequence) {
                    toast(s)
                }
            }
        )

        btn_change_bg.onClick {
            vc_et.setTextBgType(VerificationCodeEditText.TextBgType.valueOf(Random.nextInt(3)))
        }
    }
}
