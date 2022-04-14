package com.ayvytr.cardviewpager.cardviewpager

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ayvytr.cardviewpager.R
import kotlinx.android.synthetic.main.activity_card_view_pager.*

class CardViewPagerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_view_pager)
        initView()
    }

    private fun initView() {
        title = "CardViewPager"

        val list = arrayListOf<String>(
        "http://pic21.photophoto.cn/20111106/0020032891433708_b.jpg",
        "http://pic5.photophoto.cn/20071228/0034034901778224_b.jpg",
        "http://pic9.photophoto.cn/20081128/0033033999061521_b.jpg")
        vp.adapter = CardBannerAdapter(list)
    }
}
