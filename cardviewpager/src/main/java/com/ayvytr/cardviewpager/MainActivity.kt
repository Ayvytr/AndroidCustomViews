package com.ayvytr.cardviewpager

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ayvytr.cardviewpager.cardviewpager.CardViewPagerActivity
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
        btnCardViewPager.onClick { startActivity<CardViewPagerActivity>() }
    }
}
