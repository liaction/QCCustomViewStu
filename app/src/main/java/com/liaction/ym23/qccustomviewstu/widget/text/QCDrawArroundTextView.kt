package com.liaction.ym23.qccustomviewstu.widget.text

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import com.liaction.ym23.qccustomviewstu.R
import com.liaction.ym23.qccustomviewstu.util.dpx
import com.liaction.ym23.qccustomviewstu.util.qcGetImageFromSource
import com.liaction.ym23.qccustomviewstu.util.spx

class QCDrawArroundTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val bitmapPositionTop = 100.dpx
    private val bitmapPositionRight = 100.dpx
    private val bitmapPadding = QCPadding(10.dpx, 20.dpx, 30.dpx, 40.dpx)
    private val text =
        "Mauris laoreet orci ut urna pulvinar, at commodo dolor elementum. Sed dignissim nulla neque, eu dapibus orci laoreet a. Praesent venenatis est consectetur, accumsan magna dignissim, interdum est. Curabitur congue nulla non tincidunt molestie. Quisque ut semper elit. Curabitur sollicitudin arcu volutpat risus sollicitudin dictum. Quisque eu eros mollis, vulputate metus eu, porta ante. Proin pellentesque ex in nisl feugiat auctor ac efficitur ex. Pellentesque vestibulum tincidunt rutrum. Maecenas placerat libero in urna sagittis aliquet. Duis ac velit volutpat, laoreet nibh suscipit, dictum eros. Nullam elementum, ex ac luctus consectetur, justo nunc posuere nibh, ut semper eros nisi vel orci. Suspendisse sit amet enim ultrices, luctus enim vel, aliquet metus."
    private val bitmap = qcGetImageFromSource(150.dpx.toInt(), resources, R.drawable.mantou)
    private val bitmapPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 18.spx
    }
    private val measureWidth = floatArrayOf(0f)
    private val fontMetrics = Paint.FontMetrics()
    private val rectPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeWidth = 2.dpx
        style = Paint.Style.STROKE
        color = Color.RED
    }
    override fun onDraw(canvas: Canvas) {
        canvas.drawBitmap(
            bitmap,
            width - bitmapPositionRight - bitmap.width,
            bitmapPositionTop + bitmapPadding.paddingTop,
            bitmapPaint
        )
        canvas.drawRect(
            width - bitmapPositionRight - bitmap.width - bitmapPadding.paddingLeft,
            bitmapPositionTop,
            width - bitmapPositionRight - bitmap.width + bitmap.width + bitmapPadding.paddingRight,
            bitmapPositionTop + bitmap.height + bitmapPadding.paddingTop + bitmapPadding.paddingBottom,
            rectPaint
        )
        var start = 0
        var count: Int
        var offsetY = 0f + textPaint.fontSpacing
        textPaint.getFontMetrics(fontMetrics)
        while (start < text.length) {
            var haveBreak = false
            val breakWidth =
                if (offsetY + fontMetrics.bottom <= bitmapPositionTop
                    || offsetY + fontMetrics.top >= bitmapPositionTop + bitmap.height +
                    bitmapPadding.paddingTop + bitmapPadding.paddingBottom
                ) {
                    width.toFloat()
                } else {
                    haveBreak = true
                    width.toFloat() - bitmap.width - bitmapPositionRight - bitmapPadding.paddingLeft
                }
            count = textPaint.breakText(text, start, text.length, true, breakWidth, measureWidth)
            canvas.drawText(text, start, start + count, 0f, offsetY, textPaint)
            start += count
            if (haveBreak && start < text.length) {
                count = textPaint.breakText(
                    text,
                    start,
                    text.length,
                    true,
                    bitmapPositionRight - bitmapPadding.paddingRight,
                    measureWidth
                )
                canvas.drawText(
                    text,
                    start,
                    start + count,
                    width - bitmapPositionRight + bitmapPadding.paddingRight,
                    offsetY,
                    textPaint
                )
                start += count
            }
            offsetY += textPaint.fontSpacing
        }
    }

    private data class QCPadding(
        val paddingLeft: Float,
        val paddingTop: Float,
        val paddingRight: Float,
        val paddingBottom: Float
    )
}