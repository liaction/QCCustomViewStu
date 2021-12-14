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

class MultiTouchView1 @JvmOverloads constructor(
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

    private var actionId = 0

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                val actionIndex = event.actionIndex
                actionId = event.getPointerId(actionIndex)
                downOffsetX = event.getX(actionIndex)
                downOffsetY = event.getY(actionIndex)
                lastOffsetX = bitmapOffsetX
                lastOffsetY = bitmapOffsetY
            }
            MotionEvent.ACTION_POINTER_UP -> {
                // 抬起时，看看是否要更换actionId, 如果抬起的是actionId,则需要更换
                val actionIndex = event.actionIndex
                val pointerId = event.getPointerId(actionIndex)
                if (pointerId == actionId){
                    // 假设这里取最后落下的手指
                    val newActionIndex = if (actionIndex == event.pointerCount - 1){
                        event.pointerCount - 2
                    }else{
                        event.pointerCount - 1
                    }
                    actionId = event.getPointerId(newActionIndex)
                    downOffsetX = event.getX(newActionIndex)
                    downOffsetY = event.getY(newActionIndex)
                    lastOffsetX = bitmapOffsetX
                    lastOffsetY = bitmapOffsetY
                }
            }
            MotionEvent.ACTION_MOVE -> {
                val actionIndex = event.findPointerIndex(actionId)
                bitmapOffsetX = event.getX(actionIndex) - downOffsetX + lastOffsetX
                bitmapOffsetY = event.getY(actionIndex) - downOffsetY + lastOffsetY
            }
        }
        invalidate()
        return true
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawText("多点触控-接力型", width / 2f, 0f + textPaint.fontSpacing, textPaint)
        canvas.drawBitmap(bitmap, bitmapOffsetX, bitmapOffsetY, bitmapPaint)
    }

}