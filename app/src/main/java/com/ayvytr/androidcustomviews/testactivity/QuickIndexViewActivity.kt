package com.ayvytr.androidcustomviews.testactivity

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ayvytr.androidcustomviews.R
import com.ayvytr.easykotlin.android.context.dp2px
import com.ayvytr.easykotlin.android.context.getDrawable2
import com.ayvytr.easykotlin.android.context.getStringArray
import com.ayvytr.easykotlin.android.context.toast
import kotlinx.android.synthetic.main.activity_quick_index_view.*
import java.util.*

class QuickIndexViewActivity : AppCompatActivity()
{

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quick_index_view)
        initView()
    }

    private val random = Random()

    private val drawables = intArrayOf(0, android.R.drawable.ic_menu_search, android.R.drawable.ic_menu_add, android.R.drawable.ic_menu_call, android.R.drawable.ic_menu_camera, android.R.drawable.ic_menu_day)

    val randomDrawable: Drawable?
        get()
        {
            val i = random.nextInt(drawables.size)
            if (i == 0)
            {
                return null
            }

            return getDrawable2(drawables[i])
        }


    fun initView()
    {
        quickIndexView?.setOnLetterChangeListener { position, text, quickIndexView ->
            toast(text)
        }
        btnTextColor?.setOnClickListener {
            val rgb = randomColor
            quickIndexView?.textColor = rgb
        }
        btnTextSize.setOnClickListener {
            quickIndexView.textSize = random.nextInt(100)
        }
        btnBackground.setOnClickListener {
            quickIndexView.setBackgroundColor(randomColor)
        }
        btnNullData.setOnClickListener {
            quickIndexView.clearIndexList()
        }
        btnRestoreData.setOnClickListener {
            quickIndexView.setIndexArray(getStringArray(R.array.defaultQuickIndexViewLetters))
        }
    }

    private val randomColor: Int
        get() = Color.rgb(random.nextInt(0xff), random.nextInt(0xff), random.nextInt(0xff))

    private val randomToastPx: Int
        get()
        {
            var i = random.nextInt(1000)
            while (i < dp2px(50))
            {
                i = random.nextInt(1000)
            }

            return i
        }
}
