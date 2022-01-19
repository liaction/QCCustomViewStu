package com.liaction.ym23.qccustomviewstu.widget.tigerYear.previewImageView.qc

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import androidx.core.graphics.withSave
import androidx.core.view.GestureDetectorCompat
import com.liaction.ym23.qccustomviewstu.util.dpx
import com.liaction.ym23.qccustomviewstu.util.qcLog

class QCScaleImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var qcValid = true
    var imageWidth = 230.dpx.toInt()
        set(value) {
            field = value
            checkIfValid()
            resolveImagePositionInCenterOfViewOffset()
            resolveViewScaleFactorRange()
            invalidate()
        }

    var imageHeight = 230.dpx.toInt()
        set(value) {
            field = value
            checkIfValid()
            resolveImagePositionInCenterOfViewOffset()
            resolveViewScaleFactorRange()
            invalidate()
        }
    var viewWidth = 0
        set(value) {
            field = value
            viewCenterX = field / 2f
            checkIfValid()
            resolveImagePositionInCenterOfViewOffset()
            resolveViewScaleFactorRange()
            invalidate()
        }
    var viewHeight = 0
        set(value) {
            field = value
            viewCenterY = field / 2f
            checkIfValid()
            resolveImagePositionInCenterOfViewOffset()
            resolveViewScaleFactorRange()
            invalidate()
        }
    private val drawable: Drawable by lazy {
        QCDrawable().apply {
            setBounds(0, 0, imageWidth, imageHeight)
        }
    }
    private var imagePositionInCenterOfViewOffsetX = 0f
    private var imagePositionInCenterOfViewOffsetY = 0f
    private var viewCenterX = 0f
    private var viewCenterY = 0f
    private var viewScaleRange = 1f
    private var viewScaleFactorMax = 1f
    private var viewScaleFactorMin = 1f
    private var viewScaleFactorCurrent = 1f
        set(value) {
            field = value
            invalidate()
        }
    private var viewScaleFactorSalt = 1.5f
    private var viewOffsetX = 0f
    private var viewOffsetY = 0f
    private var viewOffsetFingerPercent = 1f

    private val viewAnimator = ObjectAnimator.ofFloat(
        this,
        "viewScaleFactorCurrent",
        viewScaleFactorMin,
        viewScaleFactorMax
    )

    private val gestureDetector by lazy {
        GestureDetectorCompat(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onDown(event: MotionEvent): Boolean {
                return true
            }

            override fun onDoubleTap(event: MotionEvent): Boolean {
                if (!qcValid) {
                    return false
                }
                // 处理双击，规则：0.处于min，则max；1.处于max，则min；2.处于min和max之间，则min
                if (viewScaleFactorCurrent == viewScaleFactorMin) {
                    // 跟手
                    viewOffsetX = (event.x - viewCenterX) * viewOffsetFingerPercent
                    viewOffsetY = (event.y - viewCenterY) * viewOffsetFingerPercent
                    viewAnimator.start()
                } else {
                    viewAnimator.reverse()
                }
                return true
            }

            override fun onScroll(
                downEvent: MotionEvent,
                currentEvent: MotionEvent,
                distanceX: Float,
                distanceY: Float
            ): Boolean {
                if (!qcValid) {
                    return false
                }
                if (viewScaleFactorCurrent == viewScaleFactorMin) {
                    return false
                }
                viewOffsetX -= distanceX
                viewOffsetY -= distanceY
                invalidate()
                return false
            }
        }).apply {
            setIsLongpressEnabled(false)
        }
    }

    private val scaleGestureDetector = ScaleGestureDetector(context, object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            viewOffsetX = (detector.focusX - viewCenterX) * viewOffsetFingerPercent
            viewOffsetY = (detector.focusY - viewCenterY) * viewOffsetFingerPercent
            val viewScaleFactorCurrentTemp = viewScaleFactorCurrent *  detector.scaleFactor
            viewScaleFactorCurrent = viewScaleFactorCurrentTemp.coerceAtLeast(viewScaleFactorMin)
            if (viewScaleFactorCurrent == viewScaleFactorMin){
                invalidate()
                return false
            }
            viewScaleFactorCurrent = viewScaleFactorCurrent.coerceAtMost(viewScaleFactorMax)
            if (viewScaleFactorCurrent == viewScaleFactorMax){
                invalidate()
                return false
            }
            invalidate()
            return true
        }

        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
            return true
        }
    })

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        viewWidth = w
        viewHeight = h
    }

    override fun onDraw(canvas: Canvas) {
        if (!qcValid) {
            qcLog("宽或高无效，不再继续绘制")
            return
        }
        canvas.withSave {
            // 移动位置
            val percent = (viewScaleFactorCurrent - viewScaleFactorMin) / viewScaleRange
            canvas.translate(viewOffsetX * percent, viewOffsetY * percent)
            // 缩放到最小比例
            canvas.scale(viewScaleFactorCurrent, viewScaleFactorCurrent, viewCenterX, viewCenterY)
            // 移动到中心
            canvas.translate(imagePositionInCenterOfViewOffsetX, imagePositionInCenterOfViewOffsetY)
            drawable.draw(canvas)
        }
    }

    private fun resolveViewScaleFactorRange() {
        if (!qcValid) {
            return
        }
        val widthFactor = viewWidth / imageWidth.toFloat()
        val heightFactor = viewHeight / imageHeight.toFloat()
        if (widthFactor > heightFactor) {
            viewScaleFactorMin = heightFactor
            viewScaleFactorMax = widthFactor
        } else {
            viewScaleFactorMin = widthFactor
            viewScaleFactorMax = heightFactor
        }
        viewScaleFactorMax *= viewScaleFactorSalt
        viewScaleFactorCurrent = viewScaleFactorMin
        viewScaleRange = (viewScaleFactorMax - viewScaleFactorMin)
        viewOffsetFingerPercent = (1 - viewScaleFactorMax / viewScaleFactorMin)
        viewAnimator.setFloatValues(viewScaleFactorMin, viewScaleFactorMax)
    }

    private fun checkIfValid(): Boolean {
        qcValid = imageWidth > 0 && imageHeight > 0 && viewWidth > 0 && viewHeight > 0
        return qcValid
    }

    private fun resolveImagePositionInCenterOfViewOffset() {
        if (!qcValid) {
            return
        }
        imagePositionInCenterOfViewOffsetX = (viewWidth - imageWidth) / 2f
        imagePositionInCenterOfViewOffsetY = (viewHeight - imageHeight) / 2f
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        scaleGestureDetector.onTouchEvent(event)
        if (!scaleGestureDetector.isInProgress){
            gestureDetector.onTouchEvent(event)
        }
        return true
    }
}