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

class SimpleTouchView @JvmOverloads constructor(
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

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                val actionIndex = event.actionIndex
                downOffsetX = event.getX(actionIndex)
                downOffsetY = event.getY(actionIndex)
                lastOffsetX = bitmapOffsetX
                lastOffsetY = bitmapOffsetY
            }
            MotionEvent.ACTION_MOVE -> {
                bitmapOffsetX = event.getX(0) - downOffsetX + lastOffsetX
                bitmapOffsetY = event.getY(0) - downOffsetY + lastOffsetY
            }
        }
        invalidate()
        return true
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawText("单点触控", width / 2f, 0f + textPaint.fontSpacing, textPaint)
        canvas.drawBitmap(bitmap, bitmapOffsetX, bitmapOffsetY, bitmapPaint)
    }

}