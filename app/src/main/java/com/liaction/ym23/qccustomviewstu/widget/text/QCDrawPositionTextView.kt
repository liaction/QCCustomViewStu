package com.liaction.ym23.qccustomviewstu.widget.text

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import com.liaction.ym23.qccustomviewstu.util.dpx
import com.liaction.ym23.qccustomviewstu.util.spx

class QCDrawPositionTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 50.spx
        color = Color.BLUE
    }
    private val helperPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeWidth = 2.dpx
        color = Color.RED
        style = Paint.Style.STROKE
    }
    private val contentText = "ab国p"
    private val fontMetrics = Paint.FontMetrics()
    private val contentBounds = Rect()

    override fun onDraw(canvas: Canvas) {
        helperPaint.color = Color.RED
        canvas.drawLine(0f, height / 2f, width.toFloat(), height / 2f, helperPaint)
        canvas.drawLine(width / 2f, 0f, width / 2f, height.toFloat(), helperPaint)
        // center
        textPaint.textSize = 50.spx
        textPaint.color = Color.BLUE
        textPaint.textAlign = Paint.Align.CENTER
        textPaint.getFontMetrics(fontMetrics)
        canvas.drawText(
            contentText,
            width / 2f,
            height / 2f - (fontMetrics.descent + fontMetrics.ascent) / 2f,
            textPaint
        )
        // left top
        textPaint.textSize = 20.spx
        textPaint.color = Color.BLUE
        textPaint.textAlign = Paint.Align.LEFT
        canvas.drawText(contentText, 0f, 0f + textPaint.fontSpacing, textPaint)

        textPaint.textSize = 50.spx
        textPaint.color = Color.GREEN
        textPaint.textAlign = Paint.Align.LEFT
        canvas.drawText(contentText, 0f, 0f + textPaint.fontSpacing, textPaint)

        textPaint.textSize = 50.spx
        textPaint.color = Color.RED
        textPaint.textAlign = Paint.Align.LEFT
        textPaint.getTextBounds(contentText, 0, 1, contentBounds)
        canvas.drawText(contentText, 0f - contentBounds.left, 0f + textPaint.fontSpacing, textPaint)

        // right top
        textPaint.textSize = 20.spx
        textPaint.color = Color.BLUE
        textPaint.textAlign = Paint.Align.RIGHT
        canvas.drawText(contentText, width.toFloat(), 0f + textPaint.fontSpacing, textPaint)

        textPaint.textSize = 50.spx
        textPaint.color = Color.GREEN
        textPaint.textAlign = Paint.Align.RIGHT
        canvas.drawText(contentText, width.toFloat(), 0f + textPaint.fontSpacing, textPaint)

        textPaint.textSize = 50.spx
        textPaint.color = Color.RED
        textPaint.textAlign = Paint.Align.RIGHT
        textPaint.getTextBounds(contentText, 0, 1, contentBounds)
        canvas.drawText(
            contentText,
            width.toFloat() + contentBounds.left,
            0f + textPaint.fontSpacing,
            textPaint
        )

        // left bottom
        textPaint.textSize = 20.spx
        textPaint.color = Color.BLUE
        textPaint.textAlign = Paint.Align.LEFT
        textPaint.getFontMetrics(fontMetrics)
        canvas.drawText(contentText, 0f, height.toFloat() - fontMetrics.bottom, textPaint)

        textPaint.textSize = 50.spx
        textPaint.color = Color.GREEN
        textPaint.textAlign = Paint.Align.LEFT
        textPaint.getFontMetrics(fontMetrics)
        canvas.drawText(contentText, 0f, height.toFloat() - fontMetrics.bottom, textPaint)

        textPaint.textSize = 50.spx
        textPaint.color = Color.RED
        textPaint.textAlign = Paint.Align.LEFT
        textPaint.getFontMetrics(fontMetrics)
        textPaint.getTextBounds(contentText, 0, 1, contentBounds)
        canvas.drawText(
            contentText,
            0f - contentBounds.left,
            height.toFloat() - fontMetrics.bottom,
            textPaint
        )
        // right bottom
        textPaint.textSize = 20.spx
        textPaint.color = Color.BLUE
        textPaint.textAlign = Paint.Align.RIGHT
        canvas.drawText(contentText, width.toFloat(), height.toFloat() - fontMetrics.bottom, textPaint)

        textPaint.textSize = 50.spx
        textPaint.color = Color.GREEN
        textPaint.textAlign = Paint.Align.RIGHT
        canvas.drawText(contentText, width.toFloat(), height.toFloat() - fontMetrics.bottom, textPaint)

        textPaint.textSize = 50.spx
        textPaint.color = Color.RED
        textPaint.textAlign = Paint.Align.RIGHT
        textPaint.getTextBounds(contentText, 0, 1, contentBounds)
        canvas.drawText(
            contentText,
            width.toFloat() + contentBounds.left,
            height.toFloat() - fontMetrics.bottom,
            textPaint
        )

    }
}