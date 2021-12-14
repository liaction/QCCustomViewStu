package com.liaction.ym23.qccustomviewstu.widget.spot

import android.content.Context
import android.graphics.*
import android.os.Build
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi
import com.liaction.ym23.qccustomviewstu.util.dpx
import kotlin.math.min

class SpotView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val innerPadding = 23.dpx
    private val spotRectF = RectF()
    private val centerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeWidth = 6.dpx
        color = Color.CYAN
    }
    private val textContentPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        textSize = 100.dpx
        textAlign = Paint.Align.CENTER
    }
    private val fontMetrics = Paint.FontMetrics().apply {
        textContentPaint.getFontMetrics(this)
    }
    private val fixTextOffset = (fontMetrics.ascent + fontMetrics.descent) / 2f
    private val outlinePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeWidth = 23.dpx
        style = Paint.Style.STROKE
        color = Color.BLUE
    }
    private val innerLinePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeWidth = 23.dpx
        style = Paint.Style.STROKE
        color = Color.RED
        strokeCap = Paint.Cap.ROUND
    }

    private var tipContentText = "ab国ap"
    private var bounds = Rect()


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        val minLength = min(w, h)
        spotRectF.set(
            innerPadding,
            innerPadding,
            -innerPadding + minLength,
            -innerPadding + minLength
        )
    }

    override fun onDraw(canvas: Canvas) {
        drawTopAndLeft(canvas)
        drawTopAndRight(canvas)
        drawBottomAndLeft(canvas)
        drawBottomAndRight(canvas)
        drawSpotView(canvas)
    }


    private fun drawBottomAndLeft(canvas: Canvas){
        tipContentText = "a左下p"
        textContentPaint.color = Color.BLUE
        textContentPaint.textAlign = Paint.Align.LEFT
        textContentPaint.textSize = 100.dpx
        textContentPaint.getTextBounds(tipContentText, 0, tipContentText.length, bounds)
        canvas.drawText(tipContentText, 0f - bounds.left, height.toFloat() - bounds.bottom, textContentPaint)
        textContentPaint.textAlign = Paint.Align.LEFT
        textContentPaint.textSize = 15.dpx
        textContentPaint.getTextBounds(tipContentText, 0, tipContentText.length, bounds)
        canvas.drawText(tipContentText, 0f - bounds.left, height.toFloat() - bounds.bottom, textContentPaint)
    }
    private fun drawBottomAndRight(canvas: Canvas){
        tipContentText = "a右下p"
        textContentPaint.color = Color.RED
        textContentPaint.textAlign = Paint.Align.RIGHT
        textContentPaint.textSize = 100.dpx
        textContentPaint.getTextBounds(tipContentText, 0, tipContentText.length, bounds)
        canvas.drawText(tipContentText, width.toFloat() + bounds.left, height.toFloat() - bounds.bottom, textContentPaint)
        textContentPaint.textAlign = Paint.Align.RIGHT
        textContentPaint.textSize = 15.dpx
        textContentPaint.getTextBounds(tipContentText, 0, tipContentText.length, bounds)
        canvas.drawText(tipContentText, width.toFloat() + bounds.left, height.toFloat() - bounds.bottom, textContentPaint)
    }
    private fun drawTopAndRight(canvas: Canvas){
        tipContentText = "a右上p"
        textContentPaint.color = Color.GREEN
        textContentPaint.textAlign = Paint.Align.RIGHT
        textContentPaint.textSize = 100.dpx
        textContentPaint.getTextBounds(tipContentText, 0, tipContentText.length, bounds)
        canvas.drawText(tipContentText, width.toFloat() + bounds.left, 0f - bounds.top, textContentPaint)
        textContentPaint.textAlign = Paint.Align.RIGHT
        textContentPaint.textSize = 15.dpx
        textContentPaint.getTextBounds(tipContentText, 0, tipContentText.length, bounds)
        canvas.drawText(tipContentText, width.toFloat() + bounds.left, 0f - bounds.top, textContentPaint)
    }
    private fun drawTopAndLeft(canvas: Canvas){
        tipContentText = "a左上p"
        textContentPaint.color = Color.MAGENTA
        textContentPaint.textAlign = Paint.Align.LEFT
        textContentPaint.textSize = 100.dpx
        textContentPaint.getTextBounds(tipContentText, 0, tipContentText.length, bounds)
        canvas.drawText(tipContentText, 0f - bounds.left, 0f - bounds.top, textContentPaint)
        textContentPaint.textAlign = Paint.Align.LEFT
        textContentPaint.textSize = 15.dpx
        textContentPaint.getTextBounds(tipContentText, 0, tipContentText.length, bounds)
        canvas.drawText(tipContentText, 0f - bounds.left, 0f - bounds.top, textContentPaint)
    }

    private fun drawSpotView(canvas: Canvas) {
        tipContentText = "ab国ap"
        textContentPaint.color = Color.BLACK
        textContentPaint.textAlign = Paint.Align.CENTER
        textContentPaint.textSize = 100.dpx
        canvas.drawOval(spotRectF, outlinePaint)
        canvas.drawArc(spotRectF, -30f, 180f, false, innerLinePaint)
        canvas.drawLine(
            innerPadding, spotRectF.centerY(), spotRectF.width() + innerPadding,
            spotRectF.centerY(), centerPaint
        )
        canvas.drawLine(
            spotRectF.centerX(), innerPadding,
            spotRectF.centerX(), spotRectF.height() + innerPadding, centerPaint
        )
        // draw text
        canvas.drawText(
            tipContentText, spotRectF.centerX(),
            spotRectF.centerY() - fixTextOffset, textContentPaint
        )
    }

}