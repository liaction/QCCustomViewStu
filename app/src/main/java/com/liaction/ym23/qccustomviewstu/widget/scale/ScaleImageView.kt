package com.liaction.ym23.qccustomviewstu.widget.scale

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.graphics.withSave
import androidx.core.graphics.withScale
import com.liaction.ym23.qccustomviewstu.R
import com.liaction.ym23.qccustomviewstu.util.dpx
import com.liaction.ym23.qccustomviewstu.util.qcGetImageFromSource
import com.liaction.ym23.qccustomviewstu.util.qcLog
import kotlin.math.min

class ScaleImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bitmap = qcGetImageFromSource(150.dpx.toInt(), resources, R.drawable.mantou)

    private val offsetPoint = PointF()
    private val downPoint = PointF()
    private val currentPoint = PointF()
    private val lastPoint = PointF().apply {
        set(offsetPoint)
    }
    private var scaleMax = 1f
    private var scaleMin = 1f

    private val gestureDetector = GestureDetector(context, ScaleImageViewOnGestureListener())

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        offsetPoint.set((w - bitmap.width) / 2f, (h - bitmap.height) / 2f)
        val withScale = width / bitmap.width.toFloat()
        val heightScale = height / bitmap.height.toFloat()
        scaleMin = min(withScale, heightScale)
        scaleMax = min(withScale, heightScale)
        qcLog("scale : [$scaleMin, $scaleMax]")
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        gestureDetector.onTouchEvent(event)
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                // 按下时，记录位置
                downPoint.set(event.x, event.y)
                lastPoint.set(offsetPoint)
            }
            MotionEvent.ACTION_MOVE -> {
                currentPoint.set(event.x, event.y)
                offsetPoint.set(
                    currentPoint.x - downPoint.x + lastPoint.x,
                    currentPoint.y - downPoint.y + lastPoint.y
                )
                invalidate()
            }
        }

        return true
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawBitmap(bitmap, offsetPoint.x, offsetPoint.y, paint)
    }

    private inner class ScaleImageViewOnGestureListener :
        GestureDetector.SimpleOnGestureListener() {
        private val scaleWidth = width.toFloat() / bitmap.width
        override fun onDoubleTap(e: MotionEvent): Boolean {

            return true
        }

        override fun onDown(e: MotionEvent?): Boolean {
            return true
        }
    }
}