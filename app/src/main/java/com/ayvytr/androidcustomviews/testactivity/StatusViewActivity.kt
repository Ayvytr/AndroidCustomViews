package com.ayvytr.androidcustomviews.testactivity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ayvytr.androidcustomviews.R
import com.ayvytr.customview.loading.StatusView
import com.ayvytr.easykotlin.context.toast
import kotlinx.android.synthetic.main.activity_status_view.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.startActivity

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
            statusView.setLoadingView(R.layout.test_loading)
            statusView.setErrorView(R.layout.test_error)
            statusView.setEmptyView(R.layout.test_empty)
        }
        btnShowContent.onClick { statusView.showContent() }
        btnListDemo.onClick { startActivity<StatusView2Activity>() }

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
