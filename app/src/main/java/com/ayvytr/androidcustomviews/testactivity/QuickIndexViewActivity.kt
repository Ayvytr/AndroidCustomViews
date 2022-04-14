package com.ayvytr.androidcustomviews.testactivity

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Gravity
import androidx.appcompat.app.AppCompatActivity
import com.ayvytr.androidcustomviews.R
import com.ayvytr.customview.custom.index.QuickIndexView
import com.ayvytr.customview.util.DensityUtil.dp2px
import com.ayvytr.ktx.context.dp
import com.ayvytr.ktx.context.getDrawable2
import com.ayvytr.ktx.context.getStringArray
import com.ayvytr.ktx.context.toast
import kotlinx.android.synthetic.main.activity_quick_index_view.*
import java.util.*

class QuickIndexViewActivity : AppCompatActivity()
{

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setTitle(QuickIndexView::class.java.simpleName)
        setContentView(R.layout.activity_quick_index_view)
        initView()
    }

    private val random = Random()

    private val drawables = intArrayOf(0, android.R.drawable.ic_menu_search, android.R.drawable.ic_menu_add, android.R.drawable.ic_menu_call, android.R.drawable.ic_menu_camera, android.R.drawable.ic_menu_day)

    private var isCenter = true

    val randomDrawable: Drawable?
        get()
        {
            val i = random.nextInt(drawables.size)
            if (i == 0)
            {
                return null
            }

            return resources.getDrawable(drawables[i])
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
        btnLineSpacing.setOnClickListener {
            quickIndexView.lineSpacing = random.nextInt(30)
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
        btnGravity.setOnClickListener {
            isCenter = !isCenter
            quickIndexView.gravity = if(isCenter == true) Gravity.CENTER else Gravity.TOP
        }
    }

    private val randomColor: Int
        get() = Color.rgb(random.nextInt(0xff), random.nextInt(0xff), random.nextInt(0xff))

    private val randomToastPx: Int
        get()
        {
            var i = random.nextInt(1000)
            while (i < 50.dp)
            {
                i = random.nextInt(1000)
            }

            return i
        }
}
