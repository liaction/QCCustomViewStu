package com.liaction.ym23.qccustomviewstu.widget.childParent

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.view.GestureDetectorCompat
import com.liaction.ym23.qccustomviewstu.util.dpx
import com.liaction.ym23.qccustomviewstu.util.qcLog

class QCChildView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
    }
    private val paint4Stroke = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLUE
        strokeWidth = 5.dpx
        style = Paint.Style.STROKE
    }
    private val maxScrollDistance = 50.dpx
    private var offsetY = 0f
    private var resultOffsetY = 0f
    private var haveUsedOffset = 0f
    private var canGiveParentOffset = 0f

    private val gestureDetectorCompat = GestureDetectorCompat(context,
        object : GestureDetector.SimpleOnGestureListener() {
            override fun onDown(e: MotionEvent): Boolean {
                haveUsedOffset = 0f
                canGiveParentOffset = 0f
                resultOffsetY = offsetY
                return true
            }

            override fun onScroll(
                downEvent: MotionEvent,
                currentEvent: MotionEvent,
                distanceX: Float,
                distanceY: Float
            ): Boolean {
                resultOffsetY -= distanceY
                var canInvalidate = false
                if (resultOffsetY > -maxScrollDistance.toDouble() && resultOffsetY < maxScrollDistance.toDouble()) {
                    haveUsedOffset = -distanceY
                    offsetY -= distanceY
                    canInvalidate = true
                } else {
                    canGiveParentOffset = -distanceY - haveUsedOffset
                }
//                qcLog("canGiveParentOffset: $canGiveParentOffset")
                if (canInvalidate){
                    invalidate()
                }else{
                    (parent as? QCParentView)?.onScrollAfterChildScroll(canGiveParentOffset)
                }
                return super.onScroll(downEvent, currentEvent, distanceX, distanceY)
            }
        })

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return gestureDetectorCompat.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawRect(
            0f + maxScrollDistance,
            0f + maxScrollDistance + offsetY,
            width.toFloat() - maxScrollDistance,
            height.toFloat() - maxScrollDistance + offsetY,
            paint
        )
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint4Stroke)
    }

}