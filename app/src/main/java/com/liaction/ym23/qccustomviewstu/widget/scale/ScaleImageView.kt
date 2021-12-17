package com.liaction.ym23.qccustomviewstu.widget.scale

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import androidx.core.graphics.withScale
import androidx.core.view.GestureDetectorCompat
import com.liaction.ym23.qccustomviewstu.R
import com.liaction.ym23.qccustomviewstu.util.dpx
import com.liaction.ym23.qccustomviewstu.util.qcGetImageFromSource
import com.liaction.ym23.qccustomviewstu.util.qcLog
import kotlin.math.max
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

    // 当前最终缩放比
    private var currentScaleFraction = 1f
    // 上一次缩放比
    private var lastScaleFraction = currentScaleFraction
    private val scaleCenterPoint = PointF()

    private val gestureDetector = GestureDetectorCompat(context, ScaleImageViewOnGestureListener())
    @Suppress("MemberVisibilityCanBePrivate")
    var scaleFractionValue = 0f
        set(value) {
            field = value
            invalidate()
        }
    private val scaleAnimator = ObjectAnimator.ofFloat(this, "scaleFractionValue", 0f, 1f)

    private val scaleGestureDetector = ScaleGestureDetector(context, ScaleImageViewOnScaleGestureListener())

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        offsetPoint.set((w - bitmap.width) / 2f, (h - bitmap.height) / 2f)
        scaleCenterPoint.set(offsetPoint.x + bitmap.width / 2f, offsetPoint.y + bitmap.height / 2f)
        val withScale = width / bitmap.width.toFloat()
        val heightScale = height / bitmap.height.toFloat()
        scaleMin = min(withScale, heightScale)
        scaleMax = max(withScale, heightScale)
        qcLog("scale : [$scaleMin, $scaleMax]")
        qcLog("width-height : [$w, $h]")
        qcLog("width/2-height/2 : [${w / 2f}, ${h / 2f}]")
        qcLog("offsetPoint: $offsetPoint, scaleCenterPoint: $scaleCenterPoint")
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
//        when (event.actionMasked) {
//            MotionEvent.ACTION_DOWN -> {
//                // 按下时，记录位置
//                downPoint.set(event.x, event.y)
//                lastPoint.set(offsetPoint)
//            }
//            MotionEvent.ACTION_MOVE -> {
//                currentPoint.set(event.x, event.y)
//                offsetPoint.set(
//                    (currentPoint.x - downPoint.x + lastPoint.x)
//                        .coerceAtLeast(0f)
//                        .coerceAtMost(width.toFloat() - bitmap.width * currentScaleFraction),
//                    (currentPoint.y - downPoint.y + lastPoint.y)
//                        .coerceAtLeast(0f)
//                        .coerceAtMost(height.toFloat() - bitmap.height * currentScaleFraction)
//                )
//                qcLog("$offsetPoint")
//                invalidate()
//            }
//        }
//        return scaleGestureDetector.onTouchEvent(event)
        return gestureDetector.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas) {
        val scaleResult =
            lastScaleFraction + (currentScaleFraction - lastScaleFraction) * scaleFractionValue
        canvas.withScale(
            scaleResult,
            scaleResult,
            scaleCenterPoint.x,
            scaleCenterPoint.y
        ) {
            canvas.drawBitmap(bitmap, offsetPoint.x, offsetPoint.y, paint)
        }
    }

    private inner class ScaleImageViewOnGestureListener :
        GestureDetector.SimpleOnGestureListener() {

        override fun onDoubleTap(e: MotionEvent?): Boolean {
            lastScaleFraction = currentScaleFraction
            currentScaleFraction = when (lastScaleFraction) {
                1f -> scaleMin
                scaleMin -> scaleMax
                else -> 1f
            }
            scaleAnimator.start()
            return true
        }

        override fun onDown(e: MotionEvent?): Boolean {
            return true
        }
    }

    private inner class ScaleImageViewOnScaleGestureListener : ScaleGestureDetector.SimpleOnScaleGestureListener(){
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            val scaleFactor = detector.scaleFactor
            qcLog("scaleFactor: $scaleFactor")
            return true
        }
    }
}