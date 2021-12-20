package com.liaction.ym23.qccustomviewstu.widget.scale

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.OverScroller
import androidx.core.graphics.withSave
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.ViewCompat
import com.liaction.ym23.qccustomviewstu.R
import com.liaction.ym23.qccustomviewstu.util.dpx
import com.liaction.ym23.qccustomviewstu.util.fling
import com.liaction.ym23.qccustomviewstu.util.qcGetImageFromSource
import com.liaction.ym23.qccustomviewstu.util.qcLog
import kotlin.math.max
import kotlin.math.min

class QCScaleImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var extraFractor = 1.5f
    private var overScrollerX = 23.dpx
    private var overScrollerY = overScrollerX
    private val bitmap = qcGetImageFromSource(230.dpx.toInt(), resources, R.drawable.mantou)
    private val bitmapPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val outRectPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
        strokeWidth = 2.dpx
        style = Paint.Style.STROKE
    }
    private var originalOffsetLeft = 0f
    private var originalOffsetTop = 0f
    private var offsetX = 0f
    private var offsetY = 0f
    private var viewCenterX = 0f
    private var viewCenterY = 0f
    private val scalePair = QCPair(1f, 2f)
    private val scaleWidthPair = QCPair(0f, 0f)
    private val scaleHeightPair = QCPair(0f, 0f)
    private var scaleFractionForCurrent = 1f
    private var scaleFractionForStart = scaleFractionForCurrent
    var scaleFractionPresent = 0f
        set(value) {
            field = value
            invalidate()
        }
    private val scaleAnimator by lazy {
        ObjectAnimator.ofFloat(this, "scaleFractionPresent", 0f, 1f)
    }
    private val overScroller by lazy {
        OverScroller(context)
    }

    private val overScrollerRunnable by lazy {
        object : Runnable {
            override fun run() {
                if (overScroller.computeScrollOffset()) {
                    offsetX = overScroller.currX.toFloat()
                    offsetY = overScroller.currY.toFloat()
                    ViewCompat.postOnAnimation(this@QCScaleImageView, this)
                }
            }
        }
    }

    private val gestureDetector =
        GestureDetectorCompat(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onFling(
                e1: MotionEvent?,
                e2: MotionEvent?,
                velocityX: Float,
                velocityY: Float
            ): Boolean {
                if (scaleFractionForCurrent == scalePair.left) {
                    return true
                }
                overScroller.fling(
                    offsetX,
                    offsetY,
                    velocityX,
                    velocityY,
                    scaleWidthPair.left,
                    scaleWidthPair.right,
                    scaleHeightPair.left,
                    scaleHeightPair.right,
                    overScrollerX,
                    overScrollerY
                )
                ViewCompat.postOnAnimation(this@QCScaleImageView, overScrollerRunnable)
                return super.onFling(e1, e2, velocityX, velocityY)
            }

            override fun onDown(e: MotionEvent?) = true
            override fun onDoubleTap(e: MotionEvent): Boolean {
                scaleFractionForStart = scaleFractionForCurrent
                scaleFractionForCurrent =
                    if (scaleFractionForStart == scalePair.left) scalePair.right else scalePair.left
                resolveScalePair()
                fixScaleOffset(e)
                scaleAnimator.start()
                return super.onDoubleTap(e)
            }

            override fun onScroll(
                downEvent: MotionEvent,
                currentEvent: MotionEvent,
                distanceX: Float,
                distanceY: Float
            ): Boolean {
                if (scaleFractionForCurrent == scalePair.left) {
                    return true
                }
                offsetX -= distanceX
                offsetY -= distanceY
                fixOffset()
                invalidate()
                return super.onScroll(downEvent, currentEvent, distanceX, distanceY)
            }
        })

    private fun fixScaleOffset(e: MotionEvent) {
        if (scaleFractionForStart == scalePair.left) {
            val tapX = e.x
            val tapY = e.y
            val scaleOffsetResult = (1 - scaleFractionForCurrent / scaleFractionForStart)
            offsetX =
                (tapX - viewCenterX) * scaleOffsetResult
            offsetY =
                (tapY - viewCenterY) * scaleOffsetResult
            fixOffset()
        }
    }

    private val scaleGestureDetector by lazy {
        ScaleGestureDetector(context, object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                scaleFractionForCurrent = detector.scaleFactor * scaleFractionForStart
                qcLog("$scaleFractionForCurrent, ${detector.scaleFactor}")
                invalidate()
                return false
            }
        })
    }

    private fun fixOffset() {
        offsetX = offsetX.coerceAtLeast(scaleWidthPair.left).coerceAtMost(scaleWidthPair.right)
        offsetY = offsetY.coerceAtLeast(scaleHeightPair.left).coerceAtMost(scaleHeightPair.right)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.withSave {
            val scaleFractionPresentResult =
                if (scaleFractionForCurrent == scalePair.left) 1 - scaleFractionPresent else scaleFractionPresent
            canvas.translate(
                offsetX * scaleFractionPresentResult,
                offsetY * scaleFractionPresentResult
            )
            val scaleFractionResult =
                scaleFractionForStart + (scaleFractionForCurrent - scaleFractionForStart) * scaleFractionPresent
            canvas.scale(
                scaleFractionResult,
                scaleFractionResult,
                viewCenterX,
                viewCenterY
            )
            canvas.drawBitmap(bitmap, originalOffsetLeft, originalOffsetTop, bitmapPaint)
        }
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), outRectPaint)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        viewCenterX = w / 2f
        viewCenterY = h / 2f
        originalOffsetLeft = (w - bitmap.width) / 2f
        originalOffsetTop = (h - bitmap.height) / 2f
        val widthScale = if (bitmap.width == 0 || w == 0) 1f else w.toFloat() / bitmap.width
        val heightScale = if (bitmap.height == 0 || h == 0) 1f else h.toFloat() / bitmap.height
        scalePair.left = min(widthScale, heightScale)
        scalePair.right = max(widthScale, heightScale) * extraFractor
        // TODO 每次尺寸改变后，都重置了，是否可以保留状态呢?
        scaleFractionForCurrent = scalePair.left
        scaleFractionForStart = scaleFractionForCurrent
        resolveScalePair()
    }

    private fun resolveScalePair() {
        val widthHalfResult = (bitmap.width * scaleFractionForCurrent - width) / 2f
        scaleWidthPair.left = -widthHalfResult
        scaleWidthPair.right = widthHalfResult
        val heightHalfResult = (bitmap.height * scaleFractionForCurrent - height) / 2f
        scaleHeightPair.left = -heightHalfResult
        scaleHeightPair.right = heightHalfResult
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
//        return gestureDetector.onTouchEvent(event)
        return scaleGestureDetector.onTouchEvent(event)
    }
}

private data class QCPair<T>(var left: T, var right: T)