package com.liaction.ym23.qccustomviewstu.widget.viewx

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.liaction.ym23.qccustomviewstu.R
import com.liaction.ym23.qccustomviewstu.util.dpx
import com.liaction.ym23.qccustomviewstu.util.qcGetImageFromSource

class QCCameraView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val bitmap = qcGetImageFromSource(180.dpx.toInt(), resources, R.drawable.mantou)
    private val camera = Camera()
    private val helperPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeWidth = 3.dpx
        style = Paint.Style.STROKE
        color = Color.BLUE
    }
    override fun onDraw(canvas: Canvas) {
        canvas.save()
        canvas.clipRect(
            width / 2f - bitmap.width / 2f,
            height / 2f - bitmap.height / 2f, width / 2f + bitmap.width / 2f,
            height / 2f
        )
        canvas.drawBitmap(
            bitmap,
            width / 2f - bitmap.width / 2f,
            height / 2f - bitmap.height / 2f,
            null
        )
        canvas.restore()

        canvas.save()
        canvas.translate(width / 2f, height / 2f)
        camera.rotateX(45f)
        camera.applyToCanvas(canvas)
        canvas.clipRect(
            -bitmap.width / 2f,
            0f, bitmap.width / 2f,
            bitmap.height / 2f
        )
        canvas.translate(-width / 2f, -height / 2f)
        canvas.drawBitmap(
            bitmap,
            width / 2f - bitmap.width / 2f,
            height / 2f - bitmap.height / 2f,
            null
        )
        canvas.restore()
        canvas.drawLine(0f, height/2f, width.toFloat(), height/2f, helperPaint)
    }
}