package com.liaction.ym23.qccustomviewstu

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import com.liaction.ym23.qccustomviewstu.util.dpx
import com.liaction.ym23.qccustomviewstu.widget.met.MaterialEditText

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_view)
        title = "自定义View"
        val metTextView = findViewById<MaterialEditText>(R.id.qcTextView)
        findViewById<View>(R.id.showButton)?.let {
            it.setOnClickListener {
                metTextView?.useFloatLabel = true
            }
        }
        findViewById<View>(R.id.hideButton)?.let {
            it.setOnClickListener {
                metTextView?.useFloatLabel = false
            }
        }
        findViewById<View>(R.id.setLabelUserName)?.let {
            it.setOnClickListener {
                metTextView?.floatLabelText = "用户名"
                metTextView?.useFloatLabel = true
            }
        }
        findViewById<View>(R.id.setLabelPassword)?.let {
            it.setOnClickListener {
                metTextView?.floatLabelText = "密码"
                metTextView?.useFloatLabel = true
            }
        }
        findViewById<View>(R.id.setLabelNull)?.let {
            it.setOnClickListener {
                metTextView?.floatLabelText = "    "
                metTextView?.useFloatLabel = false
            }
        }
    }
}