package com.ayvytr.androidcustomviews.testactivity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.ayvytr.androidcustomviews.R
import com.ayvytr.customview.loading.StatusView
import com.ayvytr.easykotlin.context.getColor2
import com.ayvytr.easykotlin.context.toast
import com.ayvytr.easykotlin.ui.getContext
import kotlinx.android.synthetic.main.activity_status_view.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.sdk25.coroutines.onClick

class StatusViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_status_view)
        initView()
    }

    private fun initView() {
        btnShowLoading.onClick { statusView.showLoading() }
        btnShowError.onClick { statusView.showError() }
        btnShowEmpty.onClick { statusView.showEmpty() }
        btnShowLoadingWithText.onClick { statusView.showLoading("自定义的Loading文字") }
        btnShowErrorWithText.onClick { statusView.showError("自定义的Error文字") }
        btnShowEmptyWithText.onClick { statusView.showEmpty("自定义的Empty文字") }
        btnResetMsg.onClick { statusView.resetDefaultMsg() }
        btnChangeView.onClick {
            val loadingView = View(getContext())
            loadingView.backgroundColor = getColor2(R.color.pink)
            statusView.setLoadingView(loadingView)

            val errorView = View(getContext())
            errorView.backgroundColor = getColor2(R.color.red)
            statusView.setErrorView(errorView)

            val emptyView = View(getContext())
            emptyView.backgroundColor = getColor2(R.color.purple)
            statusView.setEmptyView(emptyView)
        }
        btnShowContent.onClick { statusView.showContent() }

        statusView.setOnStatusClickListener(object : StatusView.OnStatusClickListener {
            override fun onLoading(statusView: StatusView?) {
                toast("加载中")
            }

            override fun onError(statusView: StatusView?) {
                toast("加载出错")
            }

            override fun onEmpty(statusView: StatusView?) {
                toast("加载为空数据")
            }
        })
    }
}
