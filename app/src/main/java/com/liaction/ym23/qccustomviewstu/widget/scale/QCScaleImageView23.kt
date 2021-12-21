@file:Suppress("MemberVisibilityCanBePrivate")

package com.liaction.ym23.qccustomviewstu.widget.scale

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
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

class QCScaleImageView23 @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val bitmapPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val contentBitmap by lazy {
        qcGetImageFromSource(
            width = 230.dpx.toInt(),
            resources = resources,
            drawableResId = R.drawable.mantou
        )
    }
    private var centerOffsetX = 0f
    private var centerOffsetY = centerOffsetX
    private var centerX = 0f
    private var centerY = centerOffsetX

    private val scaleExtraSalt = 1.2f
    private var scaleMinFraction = 1f
    private var scaleMaxFraction = scaleMinFraction
    var scaleCurrentFraction = scaleMinFraction
        set(value) {
            field = value
            invalidate()
        }
    private var scaleLastFraction = scaleCurrentFraction
    private var scaleDistanceFraction = 1f

    private var scaleOffsetX = 0f
    private var scaleOffsetY = scaleOffsetX

    private var scaleBitmapWidth = 0f
    private var scaleBitmapHeight = scaleBitmapWidth

    private val scaleAnimator =
        ObjectAnimator.ofFloat(this, "scaleCurrentFraction", scaleMinFraction, scaleMaxFraction)

    private val bitmapOverScroller = OverScroller(context)
    private val bitmapOverScrollerRunnable = object : Runnable {
        override fun run() {
            if (bitmapOverScroller.computeScrollOffset()) {
                scaleOffsetX = bitmapOverScroller.currX.toFloat()
                scaleOffsetY = bitmapOverScroller.currY.toFloat()
                fixScaleOffsetDistance()
                invalidate()
                ViewCompat.postOnAnimation(this@QCScaleImageView23, this)
            }
        }
    }

    private val scaleGestureDetector = ScaleGestureDetector(context,
        object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                val scaleFactor = detector.scaleFactor
                scaleCurrentFraction = (scaleLastFraction * scaleFactor).coerceAtLeast(scaleMinFraction).coerceAtMost(scaleMaxFraction)
                return false
            }

            override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
                scaleLastFraction = scaleCurrentFraction
                fixScaleOffset(detector.focusX, detector.focusY)
                fixScaleOffsetDistance()
                return true
            }
        })

    private val gestureDetector: GestureDetectorCompat = GestureDetectorCompat(context,
        object : GestureDetector.SimpleOnGestureListener() {
            override fun onDown(e: MotionEvent?): Boolean {
                return true
            }

            override fun onDoubleTap(event: MotionEvent): Boolean {
                resolveWhenDoubleTap(event)
                return false
            }

            override fun onFling(
                downEvent: MotionEvent,
                currentEvent: MotionEvent,
                velocityX: Float,
                velocityY: Float
            ): Boolean {
                if (scaleCurrentFraction == scaleMinFraction) {
                    return true
                }
                bitmapOverScroller.fling(
                    scaleOffsetX,
                    scaleOffsetY,
                    velocityX,
                    velocityY,
                    -scaleBitmapWidth,
                    scaleBitmapWidth,
                    -scaleBitmapHeight,
                    scaleBitmapHeight,
                    23.dpx
                )
                ViewCompat.postOnAnimation(this@QCScaleImageView23, bitmapOverScrollerRunnable)
                return super.onFling(downEvent, currentEvent, velocityX, velocityY)
            }

            override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
                performClick()
                return super.onSingleTapConfirmed(e)
            }

            override fun onScroll(
                downEvent: MotionEvent,
                currentEvent: MotionEvent,
                distanceX: Float,
                distanceY: Float
            ): Boolean {
                if (scaleCurrentFraction == scaleMinFraction) {
                    return true
                }
                resolveWhenMove(downEvent, currentEvent, -distanceX, -distanceY)
                return super.onScroll(downEvent, currentEvent, distanceX, distanceY)
            }
        })

    private fun resolveWhenMove(
        downEvent: MotionEvent,
        currentEvent: MotionEvent,
        distanceX: Float,
        distanceY: Float
    ) {
        scaleOffsetX += distanceX
        scaleOffsetY += distanceY
        fixScaleOffsetDistance()
        invalidate()
    }

    private fun fixScaleOffsetDistance() {
        scaleOffsetX = scaleOffsetX.coerceAtLeast(-scaleBitmapWidth).coerceAtMost(scaleBitmapWidth)
        scaleOffsetY =
            scaleOffsetY.coerceAtMost(scaleBitmapHeight).coerceAtLeast(-scaleBitmapHeight)
    }

    private fun resolveWhenDoubleTap(event: MotionEvent) {
        scaleLastFraction = scaleCurrentFraction
        scaleCurrentFraction = if (scaleLastFraction == scaleMinFraction) {
            scaleMaxFraction
        } else {
            scaleMinFraction
        }
        // 处理放大后的偏移
        if (scaleLastFraction == scaleMinFraction) {
            fixScaleOffset(event.x, event.y)
            fixScaleOffsetDistance()
            scaleAnimator.start()
        } else {
            scaleAnimator.reverse()
        }
    }

    private fun fixScaleBitmapRange(){
        scaleBitmapWidth = (contentBitmap.width * scaleMaxFraction - width) / 2f
        scaleBitmapHeight = (contentBitmap.height * scaleMaxFraction - height) / 2f
    }

    private fun fixScaleOffset(focusX: Float, focusY: Float) {
        val offsetBeforeScaleX = focusX - centerX
        val offsetBeforeScaleY = focusY - centerY
        val scaleOffsetFraction = 1 - scaleCurrentFraction
        scaleOffsetX = offsetBeforeScaleX * scaleOffsetFraction
        scaleOffsetY = offsetBeforeScaleY * scaleOffsetFraction
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        centerX = w / 2f
        centerY = h / 2f
        centerOffsetX = (w - contentBitmap.width) / 2f
        centerOffsetY = (h - contentBitmap.height) / 2f
        val widthScale = if (contentBitmap.width == 0) 1f else w.toFloat() / contentBitmap.width
        val heightScale = if (contentBitmap.height == 0) 1f else h.toFloat() / contentBitmap.height
        if (widthScale < heightScale) {
            scaleMinFraction = widthScale
            scaleMaxFraction = heightScale
        } else {
            scaleMinFraction = heightScale
            scaleMaxFraction = widthScale
        }
        scaleMaxFraction *= scaleExtraSalt
        scaleCurrentFraction = scaleMinFraction
        scaleLastFraction = scaleCurrentFraction
        scaleDistanceFraction = scaleMaxFraction - scaleMinFraction
        scaleDistanceFraction = if (scaleDistanceFraction == 0f) 1f else scaleDistanceFraction
        scaleAnimator.setFloatValues(scaleMinFraction, scaleMaxFraction)
        fixScaleBitmapRange()
    }

    override fun onDraw(canvas: Canvas) {
        canvas.withSave {
            val scaleOffsetFraction =
                (scaleCurrentFraction - scaleMinFraction) / scaleDistanceFraction
            canvas.translate(scaleOffsetX * scaleOffsetFraction, scaleOffsetY * scaleOffsetFraction)
            canvas.scale(scaleCurrentFraction, scaleCurrentFraction, centerX, centerY)
            canvas.drawBitmap(contentBitmap, centerOffsetX, centerOffsetY, bitmapPaint)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        scaleGestureDetector.onTouchEvent(event)
        if (!scaleGestureDetector.isInProgress){
            gestureDetector.onTouchEvent(event)
        }
        return true
    }
}