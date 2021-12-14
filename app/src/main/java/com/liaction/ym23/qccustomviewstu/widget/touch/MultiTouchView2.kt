package com.liaction.ym23.qccustomviewstu.widget.touch

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.TextPaint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.liaction.ym23.qccustomviewstu.R
import com.liaction.ym23.qccustomviewstu.util.dpx
import com.liaction.ym23.qccustomviewstu.util.qcGetImageFromSource
import com.liaction.ym23.qccustomviewstu.util.spx

class MultiTouchView2 @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val bitmapPaint = Paint()
    private val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
        textSize = 18.spx
        color = Color.BLUE
    }

    private val bitmap = qcGetImageFromSource(150.dpx.toInt(), resources, R.drawable.mantou)

    private var bitmapOffsetX = 0f
    private var bitmapOffsetY = textPaint.fontSpacing + 10.dpx
    private var downOffsetX = 0f
    private var downOffsetY = 0f
    private var lastOffsetX = bitmapOffsetX
    private var lastOffsetY = bitmapOffsetY

    private var centerOffsetX = 0f
    private var centerOffsetY = 0f

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        var sumX = 0f
        var sumY = 0f
        // 有多点触控手指抬起，此时不应当计算要抬起的手指
        val hasPointerUp = event.actionMasked == MotionEvent.ACTION_POINTER_UP

        for (index in 0 until event.pointerCount) {
            if (hasPointerUp && event.actionIndex == index) continue
            sumX += event.getX(index)
            sumY += event.getY(index)
        }
        val realPointCount = if (hasPointerUp) event.pointerCount - 1 else event.pointerCount
        centerOffsetX = sumX / realPointCount
        centerOffsetY = sumY / realPointCount

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN, MotionEvent.ACTION_POINTER_UP -> {
                downOffsetX = centerOffsetX
                downOffsetY = centerOffsetY
                lastOffsetX = bitmapOffsetX
                lastOffsetY = bitmapOffsetY
            }
            MotionEvent.ACTION_MOVE -> {
                bitmapOffsetX = centerOffsetX - downOffsetX + lastOffsetX
                bitmapOffsetY = centerOffsetY - downOffsetY + lastOffsetY
            }
        }
        invalidate()
        return true
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawText("多点触控-组合型", width / 2f, 0f + textPaint.fontSpacing, textPaint)
        canvas.drawBitmap(bitmap, bitmapOffsetX, bitmapOffsetY, bitmapPaint)
    }

}