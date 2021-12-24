package com.liaction.ym23.qccustomviewstu.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.SystemClock
import androidx.appcompat.app.AppCompatActivity
import com.liaction.ym23.qccustomviewstu.R
import com.liaction.ym23.qccustomviewstu.util.qcLog
import kotlinx.android.synthetic.main.activity_view_main.*
import kotlinx.android.synthetic.main.qc_pull_layout.*
import kotlin.concurrent.thread

@SuppressLint("SetTextI18n")
class ViewMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_main)
        showWidth("setContentView after")
        thread {
//            SystemClock.sleep(2000)
            textView.text = "Change"
        }
        changeBtn.setOnClickListener {
            showWidth("on btn click")
            it.requestLayout()
            thread {
                SystemClock.sleep(2000)
                textView.text = "btn click change"
            }
        }
        textView.post {
            showWidth("post")
        }
        textView.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            showWidth(" layout change listener")
        }
        textView.viewTreeObserver.addOnGlobalLayoutListener {
            showWidth("on global layout")
        }
    }

    override fun onStart() {
        showWidth("onStart before")
        super.onStart()
        showWidth("onStart after")
    }

    override fun onResume() {
        showWidth("onResume before")
        super.onResume()
        showWidth("onResume after")
    }

    private fun showWidth(tag:String){
        qcLog("$tag : width= ${textView.width} ok")
    }
}