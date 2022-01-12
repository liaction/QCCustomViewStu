package com.liaction.ym23.qccustomviewstu.widget.tigerYear.previewImageView.qc

import android.graphics.*
import android.graphics.drawable.Drawable
import android.text.TextPaint
import com.liaction.ym23.qccustomviewstu.util.dpx

class QCDrawable : Drawable() {

    private val drawablePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val rectPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLUE
        strokeWidth = 3.dpx
        style = Paint.Style.STROKE
    }
    private val roundRadius = 4.dpx
    private val circleRadius = 23.dpx
    private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val tipFontMetrics = Paint.FontMetrics()
    private var tipFontCenter = 0f
    private val tipPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textAlign = Paint.Align.CENTER
        textSize = 18.dpx
        getFontMetrics(tipFontMetrics)
        tipFontCenter = (tipFontMetrics.descent +
                tipFontMetrics.ascent)/2f
    }

    override fun draw(canvas: Canvas) {
        canvas.save()
        canvas.drawLine(
            bounds.left.toFloat(),
            bounds.top.toFloat(),
            bounds.right.toFloat(),
            bounds.bottom.toFloat(),
            rectPaint
        )
        canvas.drawLine(
            bounds.left.toFloat(),
            bounds.bottom.toFloat(),
            bounds.right.toFloat(),
            bounds.top.toFloat(),
            rectPaint
        )
        canvas.drawLine(
            bounds.left.toFloat(),
            bounds.centerY().toFloat(),
            bounds.right.toFloat(),
            bounds.centerY().toFloat(),
            rectPaint
        )
        canvas.drawLine(
            bounds.centerX().toFloat(),
            bounds.top.toFloat(),
            bounds.centerX().toFloat(),
            bounds.bottom.toFloat(),
            rectPaint
        )
        canvas.drawRoundRect(
            bounds.left.toFloat(),
            bounds.top.toFloat(),
            bounds.right.toFloat(),
            bounds.bottom.toFloat(),
            roundRadius,
            roundRadius,
            rectPaint
        )
        circlePaint.color = Color.RED
        canvas.drawCircle(
            bounds.exactCenterX(),
            bounds.exactCenterY(),
            circleRadius,
            circlePaint
        )
        canvas.drawText("0", bounds.exactCenterX() , bounds.exactCenterY() - tipFontCenter,
            tipPaint)

        circlePaint.color = Color.GREEN
        canvas.drawCircle(
            bounds.left + circleRadius,
            bounds.top + circleRadius,
            circleRadius,
            circlePaint
        )
        canvas.drawText("1", bounds.left + circleRadius , bounds.top + circleRadius - tipFontCenter, tipPaint)

        circlePaint.color = Color.MAGENTA
        canvas.drawCircle(
            bounds.right - circleRadius,
            bounds.top + circleRadius,
            circleRadius,
            circlePaint
        )
        canvas.drawText("2", bounds.right - circleRadius , bounds.top + circleRadius - tipFontCenter, tipPaint)

        circlePaint.color = Color.MAGENTA
        canvas.drawCircle(
            bounds.left + circleRadius,
            bounds.bottom - circleRadius,
            circleRadius,
            circlePaint
        )
        canvas.drawText("3", bounds.left + circleRadius , bounds.bottom - circleRadius -
                tipFontCenter, tipPaint)

        circlePaint.color = Color.GREEN
        canvas.drawCircle(
            bounds.right - circleRadius,
            bounds.bottom - circleRadius,
            circleRadius,
            circlePaint
        )
        canvas.drawText("4", bounds.right - circleRadius , bounds.bottom - circleRadius -
                tipFontCenter, tipPaint)
        canvas.restore()
    }

    override fun setAlpha(alpha: Int) {
        drawablePaint.alpha = alpha
    }

    override fun getAlpha(): Int {
        return drawablePaint.alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        drawablePaint.colorFilter = colorFilter
    }

    override fun getColorFilter(): ColorFilter? {
        return drawablePaint.colorFilter
    }

    override fun getOpacity(): Int {
        return when (drawablePaint.alpha) {
            0 -> PixelFormat.TRANSPARENT
            0xFF -> PixelFormat.OPAQUE
            else -> PixelFormat.TRANSLUCENT
        }
    }
}