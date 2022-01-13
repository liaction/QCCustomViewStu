package com.liaction.ym23.qccustomviewstu.widget.tigerYear.previewImageView.qc

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.OverScroller
import androidx.core.animation.doOnEnd
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.GestureDetectorCompat
import com.liaction.ym23.qccustomviewstu.util.dpx
import com.liaction.ym23.qccustomviewstu.util.fling
import com.liaction.ym23.qccustomviewstu.util.isNotPositive
import com.liaction.ym23.qccustomviewstu.util.qcLog

class QCScaleImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var initOffsetX = 0f
    private var initOffsetY = 0f
    private val drawableWidth = 230.dpx.toInt()
    private val drawableHeight = 230.dpx.toInt()
    private val drawable: QCDrawable by lazy {
        QCDrawable().apply {
            setBounds(0, 0, drawableWidth, drawableHeight)
        }
    }

    private val bitmap by lazy {
        drawable.toBitmap(drawableWidth, drawableHeight)
    }

    private var scaleForMin = 1f
    private var scaleForMax = 1f
    private val scaleForSalt = 1.2f
    private var scaleForEnd = 1f
    private var scaleForStart = 1f

    private var offsetX = 0f
    private var offsetY = 0f
    private var lastOffsetX = offsetX
    private var lastOffsetY = offsetY

    private var scaleAnimatorRatio = 0f
        set(value) {
            field = value
            invalidate()
        }
    private val scaleAnimator: ObjectAnimator by lazy {
        ObjectAnimator.ofFloat(this, "scaleAnimatorRatio", 0f, 1f).apply {
            duration = 2300
        }
    }
    private var enableOverScroller = false
    private val overScroller: OverScroller by lazy {
        OverScroller(context)
    }
    private val overScrollerRunnable: Runnable by lazy {
        val runnable = Runnable {
            if (!overScroller.computeScrollOffset()){
                return@Runnable
            }
            offsetX = overScroller.currX.toFloat()
            offsetY = overScroller.currY.toFloat()
            fixOffset()
            postOnAnimation(overScrollerRunnable)
        }
        runnable
    }
    private val gestureDetector = GestureDetectorCompat(context,
        object : GestureDetector.SimpleOnGestureListener() {
            override fun onDown(e: MotionEvent?): Boolean {
                return true
            }

            override fun onDoubleTap(event: MotionEvent): Boolean {
                lastOffsetX = offsetX
                lastOffsetY = offsetY
                scaleForStart = scaleForEnd
                scaleForEnd = if (!checkIfScaleCurrentStateIsMin()) {
                    scaleForMin
                } else {
                    scaleForMax
                }
                // 计算偏差
                if (!checkIfScaleCurrentStateIsMin()){
                    offsetX = (1 - scaleForEnd) * (event.x - width / 2f)
                    offsetY = (1 - scaleForEnd) * (event.y - height / 2f)
                    fixOffset()
                }
                scaleAnimator.start()
                return false
            }

            override fun onFling(
                downEvent: MotionEvent,
                currentEvent: MotionEvent,
                velocityX: Float,
                velocityY: Float
            ): Boolean {
                if (!enableOverScroller){
                    return false
                }
                if (!checkIfScaleCurrentStateIsMin()){
                    val minX = -1f * (scaleForEnd * bitmap.width - width) / 2f
                    val maxX = -minX
                    val minY = -1f * (scaleForEnd * bitmap.height - height) / 2f
                    val maxY = -minY
                    overScroller.fling(offsetX, offsetY, velocityX, velocityY, minX, maxX, minY,
                        maxY, 100.dpx)
                    postOnAnimation(overScrollerRunnable)
                }
                return false
            }

            override fun onScroll(
                downEvent: MotionEvent,
                currentEvent: MotionEvent,
                distanceX: Float,
                distanceY: Float
            ): Boolean {
                if (!checkIfScaleCurrentStateIsMin()) {
                    offsetX -= distanceX
                    offsetY -= distanceY
                    fixOffset()
                    invalidate()
                }
                return false
            }
        })

    private fun fixOffset(){
        val minX = -1f * (scaleForEnd * bitmap.width - width) / 2f
        val minY = -1f * (scaleForEnd * bitmap.height - height) / 2f
        offsetX = offsetX.coerceAtLeast(minX).coerceAtMost(-minX)
        offsetY = offsetY.coerceAtLeast(minY).coerceAtMost(-minY)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        if (checkIfValid(w, h)) {
            return
        }
        val widthScale = w / bitmap.width.toFloat()
        val heightScale = h / bitmap.height.toFloat()
        if (widthScale > heightScale) {
            scaleForMax = widthScale * scaleForSalt
            scaleForMin = heightScale
        } else {
            scaleForMax = heightScale * scaleForSalt
            scaleForMin = widthScale
        }
        scaleForStart = scaleForMin
        scaleForEnd = scaleForStart
        initOffsetX = (w - bitmap.width) / 2f
        initOffsetY = (h - bitmap.height) / 2f
    }

    override fun onDraw(canvas: Canvas) {
        canvas.save()
        val percent = if (checkIfScaleCurrentStateIsMin()) (1 - scaleAnimatorRatio) else scaleAnimatorRatio
        canvas.translate(offsetX * percent, offsetY * percent)
        val scaleForCurrent = scaleForStart + (scaleForEnd - scaleForStart) * scaleAnimatorRatio
        canvas.scale(scaleForCurrent, scaleForCurrent, width / 2f, height / 2f)
        canvas.drawBitmap(bitmap, initOffsetX, initOffsetY, null)
        canvas.restore()
    }

    private fun checkIfValid(w: Int = width, h: Int = height): Boolean {
        return bitmap.width.isNotPositive || bitmap.height.isNotPositive || w.isNotPositive || h.isNotPositive
    }

    private fun checkIfScaleCurrentStateIsMin() = scaleForEnd == scaleForMin

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(event)
    }
}