package com.liaction.ym23.qccustomviewstu.widget.tigerYear.previewImageView

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.OverScroller
import androidx.core.view.GestureDetectorCompat
import com.liaction.ym23.qccustomviewstu.R
import com.liaction.ym23.qccustomviewstu.util.dpx
import com.liaction.ym23.qccustomviewstu.util.qcGetImageFromSource
import kotlin.math.min

@Deprecated("废弃", level = DeprecationLevel.ERROR, replaceWith = ReplaceWith("QCScaleImageView",
    "com.liaction.ym23.qccustomviewstu.widget.tigerYear.previewImageView.QCScaleImageView"))
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
    private var bitmapLastOffsetX = 0f
    private var bitmapLastOffsetY = 0f

    private var bitmapOverScroller = OverScroller(context)

    private val bitmapOverScrollerRunnable = Runnable {

    }

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
                bitmapLastOffsetX = bitmapOffsetX
                bitmapLastOffsetY = bitmapOffsetY
                return true
            }

            override fun onDoubleTap(event: MotionEvent): Boolean {
                bitmapScaleCenterX = event.x
                bitmapScaleCenterY = event.y
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

            override fun onScroll(
                e1: MotionEvent,
                e2: MotionEvent,
                distanceX: Float,
                distanceY: Float
            ): Boolean {
//                qcLog("[$distanceX,${e1.x-e2.y}],$distanceY, [${e1.x},${e1.y}], [${e2.x},${e2.y}]")
                if (bitmapScaleRationForCurrent != bitmapScaleRationForSmall) {
                    bitmapOffsetX = e2.x - e1.x + bitmapLastOffsetX
                    bitmapOffsetY = e2.y - e1.y + bitmapLastOffsetY
                    fixBitmapRange()
                    invalidate()
                }
                return false
            }

            override fun onFling(
                e1: MotionEvent,
                e2: MotionEvent,
                velocityX: Float,
                velocityY: Float
            ): Boolean {
                val overScrollerMaxX =
                    ((bitmapWidth * bitmapScaleRationForCurrent - viewWidth) / 2f).toInt()
                val overScrollerMaxY =
                    ((bitmapHeight * bitmapScaleRationForCurrent - viewHeight) / 2f)
                        .toInt()
                bitmapOverScroller.fling(
                    bitmapOffsetX.toInt(), bitmapOffsetY.toInt(), velocityX.toInt(),
                    velocityY.toInt(),
                    -overScrollerMaxX, overScrollerMaxX,
                    -overScrollerMaxY, overScrollerMaxY
                )
//                doWhenFling()
                return false
            }
        })

    private fun fixBitmapRange(){
        val overScrollerMaxX =
            ((bitmapWidth * bitmapScaleRationForCurrent - viewWidth) / 2f)
        val overScrollerMaxY =
            ((bitmapHeight * bitmapScaleRationForCurrent - viewHeight) / 2f)
        bitmapOffsetX = bitmapOffsetX.coerceAtMost(overScrollerMaxX).coerceAtLeast(-overScrollerMaxX)
        bitmapOffsetY = bitmapOffsetY.coerceAtMost(overScrollerMaxY).coerceAtLeast(-overScrollerMaxY)
    }

    private fun doWhenFling() {
        postOnAnimation(bitmapOverScrollerRunnable)
    }

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
        bitmapScaleRationForCurrent = bitmapScaleRationForSmall
        bitmapScaleRationDelta = bitmapScaleRationForBig - bitmapScaleRationForSmall
    }

    override fun onDraw(canvas: Canvas) {
        canvas.save()
        canvas.translate(bitmapOffsetX * bitmapScaleAnimatorPercent, bitmapOffsetY * bitmapScaleAnimatorPercent)
        canvas.scale(
            bitmapScaleRationForCurrent,
            bitmapScaleRationForCurrent,
            bitmapScaleCenterX,
            bitmapScaleCenterY
        )
        canvas.drawBitmap(
            bitmap, bitmapCenterOffsetX,
            bitmapCenterOffsetY, null
        )
        canvas.restore()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

}