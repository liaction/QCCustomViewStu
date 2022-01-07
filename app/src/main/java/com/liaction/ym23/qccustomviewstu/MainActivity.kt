package com.liaction.ym23.qccustomviewstu

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.setPadding
import com.liaction.ym23.qccustomviewstu.util.dpx
import com.liaction.ym23.qccustomviewstu.util.qcChinaProvince
import com.liaction.ym23.qccustomviewstu.widget.tab.QCTabLayout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.qc_camera_view)
        title = "QCCameraView"
    }

    @SuppressLint("SetTextI18n")
    private fun doSomethingAfterInflateLayout() {
        val colorList = listOf(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.CYAN)
        val qcTabLayout = findViewById<QCTabLayout>(R.id.qcTabLayout)
        qcChinaProvince.forEachIndexed { index, province ->
            val textViewCompat = AppCompatTextView(this)
            with(textViewCompat) {
                text = "[${index + 1}] $province"
                setPadding(((index + 1) * 2.dpx).toInt())
                setTextColor(Color.BLACK)
                textSize = 16f
                setBackgroundColor(colorList.random())
            }
            qcTabLayout.addView(
                textViewCompat,
                ViewGroup.MarginLayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
//                    setMargins(8.dpx.toInt())
//                    setMargins(10.dpx.toInt(), 10.dpx.toInt(), 23.dpx.toInt(), 0)
                }
            )
        }
    }
}