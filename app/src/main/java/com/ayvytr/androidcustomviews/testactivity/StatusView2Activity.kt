package com.ayvytr.androidcustomviews.testactivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ayvytr.androidcustomviews.R
import com.ayvytr.baseadapter.CommonAdapter
import com.ayvytr.baseadapter.ViewHolder
import kotlinx.android.synthetic.main.activity_status_view2.*
import kotlin.concurrent.thread

class StatusView2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_status_view2)
        initView()
    }

    private fun initView() {
        val list = arrayOf("a", "b", "c", "d", "e", "f", "g")
        rv.layoutManager = LinearLayoutManager(this)
        statusView.showLoading()

        thread {
            Thread.sleep(3000)

            runOnUiThread {
                rv.adapter = object :
                    CommonAdapter<String>(this@StatusView2Activity, R.layout.item, list.toMutableList()) {
                    override fun onBindView(
                        holder: ViewHolder,
                        t: String,
                        position: Int,
                        payloads: List<Any>
                    ) {
                        holder.setText(R.id.tv, t)
                    }
                }
                statusView.showContent()
            }
        }
    }
}
