package com.liaction.ym23.qccustomviewstu.widget.tigerYear.previewImageView

import android.animation.ObjectAnimator
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
import com.liaction.ym23.qccustomviewstu.R
import com.liaction.ym23.qccustomviewstu.util.dpx
import com.liaction.ym23.qccustomviewstu.util.isPositive
import com.liaction.ym23.qccustomviewstu.util.qcGetImageFromSource

/**
 * Scale Image View
 */
@Deprecated("废弃", level = DeprecationLevel.ERROR, replaceWith = ReplaceWith("QCScaleImageView",
    "com.liaction.ym23.qccustomviewstu.widget.tigerYear.previewImageView.qc.QCScaleImageView"))
class QCScaleImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val bitmapWidth = 150.dpx.toInt()
    private var bitmapHeight = 0
    private val bitmap = qcGetImageFromSource(bitmapWidth, resources, R.drawable.mantou).apply {
        bitmapHeight = height
        bitmapCenterX = width / 2f
        bitmapCenterY = bitmapHeight / 2f
    }
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
    }
    private var bitmapScaleRatioForMin = 1f
    private var bitmapScaleRatioForMax = 1f
    private var bitmapScaleRationSalt = 1.5f
    private var bitmapScaleRatioForCurrent = 1f
    private var bitmapResultScaleRation = 1f
    private var bitmapScaleDelta = 0f

    private var bitmapOffsetX = 0f
    private var bitmapOffsetY = 0f

    private var viewCenterX = 0f
    private var viewCenterY = 0f
    private var bitmapCenterX = 0f
    private var bitmapCenterY = 0f

    private var bitmapAnimatorFromMinToMax = true
    private var bitmapLastScrollX = 0f
    private var bitmapLastScrollY = 0f
    private var bitmapScaleCenterX = 0f
    private var bitmapScaleCenterY = 0f

    var useScroll = true

    private var bitmapScalePercent = 0f
        set(value) {
            field = value
            bitmapScaleRatioForCurrent = bitmapScaleRatioForMin + bitmapScaleDelta * value
            if (!bitmapAnimatorFromMinToMax) {
                if (useScroll) {
                    val currentScrollX = bitmapLastScrollX * value
                    val currentScrollY = bitmapLastScrollY * value
                    scrollTo(currentScrollX.toInt(), currentScrollY.toInt())
                }
            }else{
                if (useScroll){
                    val currentScrollX = (bitmapLastScrollX + bitmapScaleCenterX)
                        .coerceAtLeast(-(bitmapResultScaleRation * bitmapWidth - width) / 2f)
                        .coerceAtMost((bitmapResultScaleRation * bitmapWidth - width) / 2f)
                    val currentScrollY = (bitmapLastScrollY + bitmapScaleCenterY)
                        .coerceAtLeast(-(bitmapResultScaleRation * bitmapHeight - height) / 2f)
                        .coerceAtMost((bitmapResultScaleRation * bitmapHeight - height) / 2f)
                    scrollTo((currentScrollX * value).toInt(), (currentScrollY * value).toInt())
                }
            }
            invalidate()
        }
    private val bitmapScaleAnimation: ObjectAnimator by lazy {
        ObjectAnimator.ofFloat(this, "bitmapScalePercent", 0f, 1f)
    }

    //    region 尺寸改变时初始化处理
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (!resolveIfWidthOrHeightValid()) {
            return
        }
        resolveRatio(w, h)
        viewCenterX = w / 2f
        viewCenterY = h / 2f
    }

    /**
     * 处理放缩比率
     */
    private fun resolveRatio(w: Int, h: Int) {
        val widthRatio = 1f * w / bitmapWidth
        val heightRatio = 1f * h / bitmapHeight
        if (widthRatio > heightRatio) {
            bitmapScaleRatioForMin = heightRatio
            bitmapScaleRatioForMax = widthRatio * bitmapScaleRationSalt
        } else {
            bitmapScaleRatioForMin = widthRatio
            bitmapScaleRatioForMax = heightRatio * bitmapScaleRationSalt
        }
        bitmapScaleDelta = bitmapScaleRatioForMax - bitmapScaleRatioForMin
        // 默认最小比例贴边填充
        bitmapScaleRatioForCurrent = bitmapScaleRatioForMin
        bitmapResultScaleRation = bitmapScaleRatioForCurrent
    }

    /**
     * 检查宽高是否有效
     */
    private fun resolveIfWidthOrHeightValid(): Boolean {
        return bitmapWidth.isPositive && bitmapHeight.isPositive && width.isPositive && height
            .isPositive
    }

//    endregion

    //    region onDraw
    override fun onDraw(canvas: Canvas) {
        if (!resolveIfWidthOrHeightValid()) {
            return
        }
        canvas.save()
        if (!useScroll) {
            canvas.translate(bitmapOffsetX, bitmapOffsetY)
        }
        // 移动到中心 (152 447 477 04509 烧被子识别码)
        canvas.translate(viewCenterX, viewCenterY)
        // 缩放
        canvas.scale(bitmapScaleRatioForCurrent, bitmapScaleRatioForCurrent)
        // 绘制bitmap 中心
        canvas.drawBitmap(bitmap, -bitmapCenterX, -bitmapCenterY, null)
        canvas.restore()
        canvas.drawCircle(viewCenterX, viewCenterY, 23.dpx, paint)
    }

//    endregion

    //    region 触摸事件处理
    private val gestureDetector = GestureDetectorCompat(context,
        object : GestureDetector.SimpleOnGestureListener() {

            override fun onScroll(
                e1: MotionEvent,
                e2: MotionEvent,
                distanceX: Float,
                distanceY: Float
            ): Boolean {
                if (!checkIfBitmapIsTheMinRation()) {
                    if (useScroll) {
                        val resultScrollX = (scrollX + distanceX)
                            .coerceAtLeast(-(bitmapScaleRatioForCurrent * bitmapWidth - width) / 2f)
                            .coerceAtMost((bitmapScaleRatioForCurrent * bitmapWidth - width) / 2f)
                        val resultScrollY = (scrollY + distanceY)
                            .coerceAtLeast(-(bitmapScaleRatioForCurrent * bitmapHeight - height) / 2f)
                            .coerceAtMost((bitmapScaleRatioForCurrent * bitmapHeight - height) / 2f)
                        scrollTo((resultScrollX).toInt(), (resultScrollY).toInt())
                    }
                }
                return false
            }

            override fun onDoubleTap(event: MotionEvent): Boolean {
                bitmapScaleCenterX = ((event.x - viewCenterX) * bitmapScaleRatioForCurrent)
                bitmapScaleCenterY = ((event.y - viewCenterY)* bitmapScaleRatioForCurrent)
                if (useScroll) {
                    bitmapLastScrollX = scrollX.toFloat()
                    bitmapLastScrollY = scrollY.toFloat()
                }
                if (checkIfBitmapIsTheMaxRation()) {
                    bitmapResultScaleRation = bitmapScaleRatioForMin
                    bitmapAnimatorFromMinToMax = false
                    bitmapScaleAnimation.reverse()
                } else {
                    bitmapResultScaleRation = bitmapScaleRatioForMax
                    bitmapAnimatorFromMinToMax = true
                    bitmapScaleAnimation.start()
                }
                return false
            }

            override fun onDown(e: MotionEvent?): Boolean {
                return true
            }
        })

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(event)
    }
//    endregion

    //    region 其他辅助方法
    private fun checkIfBitmapIsTheMaxRation() = bitmapScaleRatioForCurrent ==
            bitmapScaleRatioForMax

    private fun checkIfBitmapIsTheMinRation() = bitmapScaleRatioForCurrent ==
            bitmapScaleRatioForMin
//    endregion
}