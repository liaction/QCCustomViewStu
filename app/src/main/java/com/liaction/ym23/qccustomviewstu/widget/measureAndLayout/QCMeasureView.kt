package com.liaction.ym23.qccustomviewstu.widget.measureAndLayout

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.liaction.ym23.qccustomviewstu.util.csLog
import com.liaction.ym23.qccustomviewstu.util.dpx
import com.liaction.ym23.qccustomviewstu.util.qcLog
import com.liaction.ym23.qccustomviewstu.util.qcShowMeasureSpecInfo

private val radius = 100.dpx
private val qcWidth = radius * 2
private val qcHeight = radius * 2

class QCMeasureView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLUE
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        qcShowMeasureSpecInfo(widthMeasureSpec, tipExtraMessageString = "child 宽")
        val resultWidth = resolveSize(qcWidth.toInt(), widthMeasureSpec)
        qcLog("child qc width : ${qcWidth.toInt()} , result width : $resultWidth")
        qcShowMeasureSpecInfo(heightMeasureSpec, tipExtraMessageString = "child 高")
        val resultHeight = resolveSize(qcHeight.toInt(), heightMeasureSpec)
        qcLog("child qc height : ${qcHeight.toInt()} , result height : $resultHeight")
        setMeasuredDimension(resultWidth, resultHeight)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawColor(Color.RED)
        canvas.drawCircle(width / 2f, height / 2f, 100.dpx, paint)
    }

}