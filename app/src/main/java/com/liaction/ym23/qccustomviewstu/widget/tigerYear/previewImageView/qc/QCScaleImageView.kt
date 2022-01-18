package com.liaction.ym23.qccustomviewstu.widget.tigerYear.previewImageView.qc

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.withSave
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
    private var viewScaleFactorMax = 1f
    private var viewScaleFactorMin = 1f
    private var viewScaleFactorCurrent = 1f
    private var viewScaleFactorSalt = 1.5f

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
            // 缩放到最小比例
            canvas.scale(viewScaleFactorCurrent, viewScaleFactorCurrent, viewCenterX, viewCenterY)
            // 移动到中心
            canvas.translate(imagePositionInCenterOfViewOffsetX, imagePositionInCenterOfViewOffsetY)
            drawable.draw(canvas)
        }
    }

    private fun resolveViewScaleFactorRange() {
        if (!qcValid){
            return
        }
        val widthFactor = viewWidth / imageWidth.toFloat()
        val heightFactor = viewHeight / imageHeight.toFloat()
        if (widthFactor > heightFactor){
            viewScaleFactorMin = heightFactor
            viewScaleFactorMax = widthFactor
        }else{
            viewScaleFactorMin = widthFactor
            viewScaleFactorMax = heightFactor
        }
        viewScaleFactorMax *= viewScaleFactorSalt
        viewScaleFactorCurrent = viewScaleFactorMin
    }

    private fun checkIfValid(): Boolean {
        qcValid = imageWidth > 0 && imageHeight > 0 && viewWidth > 0 && viewHeight > 0
        return qcValid
    }

    private fun resolveImagePositionInCenterOfViewOffset(){
        if (!qcValid){
            return
        }
        imagePositionInCenterOfViewOffsetX = (viewWidth - imageWidth) / 2f
        imagePositionInCenterOfViewOffsetY = (viewHeight - imageHeight) / 2f
    }
}