package com.liaction.ym23.qccustomviewstu.widget.tigerYear.previewImageView

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.view.GestureDetectorCompat
import com.liaction.ym23.qccustomviewstu.R
import com.liaction.ym23.qccustomviewstu.util.dpx
import com.liaction.ym23.qccustomviewstu.util.qcGetImageFromSource
import kotlin.math.min

class QCPreviewImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var viewWidth = 0f
    private var viewHeight = 0f
    private var viewCenterX = 0f
    private var viewCenterY = 0f
    private var bitmapWidth = 0f
    private var bitmapHeight = 0f
    private var bitmapCenterX = 0f
    private var bitmapCenterY = 0f
    private var bitmapCenterOffsetX = 0f
    private var bitmapCenterOffsetY = 0f

    private var bitmapScaleRationForSmall = 1f
    private var bitmapScaleRationForBig = 1f
    private var bitmapScaleRationForSalt = 1.5f
    private var bitmapScaleRationForCurrent = 1f
    private var bitmapScaleRationDelta = 0f

    private var bitmapScaleCenterX = 0f
    private var bitmapScaleCenterY = 0f

    private var bitmapOffsetX = 0f
    private var bitmapOffsetY = 0f

    private var bitmapScaleAnimatorPercent = 0f
        set(value) {
            field = value
            bitmapScaleRationForCurrent = bitmapScaleRationForSmall + bitmapScaleRationDelta * value
            invalidate()
        }
    private val bitmapScaleAnimator by lazy {
        ObjectAnimator.ofFloat(this@QCPreviewImageView, "bitmapScaleAnimatorPercent", 0f, 1f)
    }

    private val gestureDetector = GestureDetectorCompat(context,
        object : GestureDetector.SimpleOnGestureListener() {
            override fun onDown(e: MotionEvent?): Boolean {
                return true
            }

            override fun onDoubleTap(e: MotionEvent?): Boolean {
                if (bitmapScaleRationForCurrent !=
                    bitmapScaleRationForBig
                ) {
                    // 放大到最大
                    bitmapScaleAnimator.start()
                } else {
                    // 缩小到最小
                    bitmapScaleAnimator.reverse()
                }
                return true
            }
        })

    private val bitmap = qcGetImageFromSource(230.dpx.toInt(), resources, R.drawable.mantou).apply {
        if (!isRecycled) {
            bitmapCenterX = width / 2f
            bitmapCenterY = height / 2f
            bitmapWidth = width.toFloat()
            bitmapHeight = height.toFloat()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        viewCenterX = w / 2f
        viewCenterY = h / 2f
        bitmapScaleCenterX = viewCenterX
        bitmapScaleCenterY = viewCenterY
        viewWidth = w.toFloat()
        viewHeight = h.toFloat()
        bitmapCenterOffsetX = (viewWidth - bitmapWidth) / 2f
        bitmapCenterOffsetY = (viewHeight - bitmapHeight) / 2f
        val widthRation = if (bitmapWidth == 0f) 1f else width / bitmapWidth
        val heightRation = if (bitmapHeight == 0f) 1f else height / bitmapHeight
        bitmapScaleRationForSmall = min(widthRation, heightRation)
        bitmapScaleRationForBig =
            (if (bitmapScaleRationForSmall == widthRation) heightRation else widthRation)
        bitmapScaleRationForBig *= bitmapScaleRationForSalt
        bitmapScaleRationForCurrent = bitmapScaleRationForBig
        bitmapScaleRationDelta = bitmapScaleRationForBig - bitmapScaleRationForSmall
    }

    override fun onDraw(canvas: Canvas) {
        canvas.save()
        canvas.scale(
            bitmapScaleRationForCurrent,
            bitmapScaleRationForCurrent,
            viewCenterX,
            viewCenterY
        )
        canvas.drawBitmap(
            bitmap, bitmapCenterOffsetX,
            bitmapCenterOffsetY, null
        )
        canvas.restore()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

}