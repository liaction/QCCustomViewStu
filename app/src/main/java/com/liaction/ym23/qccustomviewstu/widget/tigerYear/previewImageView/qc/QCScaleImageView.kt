package com.liaction.ym23.qccustomviewstu.widget.tigerYear.previewImageView.qc

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.drawable.toBitmap
import com.liaction.ym23.qccustomviewstu.util.dpx
import com.liaction.ym23.qccustomviewstu.util.isNotPositive

class QCScaleImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val drawableWidth = 230.dpx.toInt()
    private val drawableHeight = 230.dpx.toInt()
    private val drawable by lazy {
        QCDrawable().apply {
            setBounds(0, 0, drawableWidth, drawableHeight)
        }
    }

    private val bitmap by lazy {
        drawable.toBitmap(drawableWidth, drawableHeight)
    }

    private var scaleForMin = 1f
    private var scaleForMax = 1f
    private val scaleForSalt = 1.5f
    private var scaleForCurrent = 1f

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        if (checkIfValid(w, h)) {
            return
        }
        val widthScale = w / bitmap.width.toFloat()
        val heightScale = h / bitmap.height.toFloat()
        if (widthScale > heightScale){
            scaleForMax = widthScale * scaleForSalt
            scaleForMin = heightScale
        }else{
            scaleForMax = heightScale * scaleForSalt
            scaleForMin = widthScale
        }
        scaleForCurrent = scaleForMin
    }

    override fun onDraw(canvas: Canvas) {
        canvas.save()
        canvas.scale(scaleForCurrent, scaleForCurrent, width / 2f, height / 2f)
        canvas.drawBitmap(
            bitmap, width / 2f - bitmap.width / 2f, height / 2f - bitmap.height /
                    2f, null
        )
        canvas.restore()
    }

    private fun checkIfValid(w: Int = width, h: Int = height): Boolean {
        return bitmap.width.isNotPositive || bitmap.height.isNotPositive || w.isNotPositive || h.isNotPositive
    }
}