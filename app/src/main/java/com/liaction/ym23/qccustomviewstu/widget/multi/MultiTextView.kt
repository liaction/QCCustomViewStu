package com.liaction.ym23.qccustomviewstu.widget.multi

import android.content.Context
import android.graphics.*
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import com.liaction.ym23.qccustomviewstu.R
import com.liaction.ym23.qccustomviewstu.util.dpx

class MultiTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val multiContentText = """
        Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut varius magna quis mattis venenatis.  a pellentesque mauris tempor. Donec a suscipit ex. Cras fermentum mi ac urna dictum, molestie varius justo consectetur. Duis porttitor sed metus nec cursus. Aliquam quis volutpat risus. Maecenas sollicitudin ipsum sit amet eros viverra, in scelerisque ipsum tempor. Duis augue nulla, lobortis at consectetur eu, hendrerit vel mi. Maecenas feugiat ligula ut ex viverra, vel venenatis dui ultrices. Morbi id volutpat magna.
        中间随便加点文字看看环绕效果Duis cursus dolor a turpis lobortis interdum. In auctor turpis id orci molestie,
        Sed finibus, dui faucibus ullamcorper rhoncus, mauris nisl cursus orci, ac consequat augue mauris at risus. Curabitur laoreet in tellus facilisis imperdiet. Suspendisse eu nisi neque. Donec orci erat, aliquam sit amet justo nec, congue lacinia eros. Duis consectetur augue metus, in tempor quam semper eget. Suspendisse rutrum quam velit, a egestas lacus luctus id. Aenean blandit magna purus, quis pellentesque nisl tristique eget. Donec ac volutpat tortor. Quisque ultrices sapien nibh, eget tincidunt ipsum aliquet sit amet. Quisque tincidunt suscipit consectetur. Maecenas in tortor eget massa porta aliquam ut vel ex.结束
    """.trimIndent()
    private val multiPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        textSize = 16.dpx
    }

    private val imageWidth = 160.dpx
    private val imagePadding = 10.dpx
    private var imageTop = 50.dpx
    private var imageLeft = 0.dpx
    private val imagePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val fontMetrics = Paint.FontMetrics().apply {
        multiPaint.getFontMetrics(this)
    }
    private val measuredWidth = floatArrayOf(0f, 0f, 0f, 0f)
    private val bitmap = getDrawImageFromSource(imageWidth.toInt())

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        imageLeft = (w - bitmap.width) / 2f
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawBitmap(
            bitmap,
            imageLeft + imagePadding,
            imageTop + imagePadding, imagePaint
        )
        drawMultiLine(canvas)
    }

    private fun drawMultiLine(canvas: Canvas) {
        var textCount = 0
        var line = 1
        while (textCount < multiContentText.length) {
            val height =  multiPaint.fontSpacing * line
            var maxWidth = width
            var imageInside = false
            if ((height > imageTop) && (height < imageTop + imagePadding + bitmap.height - fontMetrics.top)) {
                imageInside = true
                maxWidth = imageLeft.toInt()
            }
            var count = multiPaint.breakText(
                multiContentText, textCount,
                multiContentText.length, true,
                maxWidth.toFloat(), measuredWidth
            )
            canvas.drawText(
                multiContentText, textCount, textCount + count, 0f,
                0f + height, multiPaint
            )
            textCount += count
            if (imageInside) {
                maxWidth = (width - imageLeft - imagePadding * 2 - bitmap.width).toInt()
                count = multiPaint.breakText(
                    multiContentText, textCount,
                    multiContentText.length, true,
                    maxWidth.toFloat(), measuredWidth
                )
                canvas.drawText(
                    multiContentText,
                    textCount,
                    textCount + count,
                    0f + imageLeft + imagePadding * 2 + bitmap.width,
                    0f + height,
                    multiPaint
                )
                textCount += count
            }
            line++
        }
    }

    private fun drawMultiLineUseStaticLayout(canvas: Canvas) {
        val staticLayout =
            StaticLayout(
                multiContentText,
                multiPaint, width,
                Layout.Alignment.ALIGN_NORMAL, 1f, 0f, true
            )
        staticLayout.draw(canvas)
    }

    private fun getDrawImageFromSource(width: Int): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(resources, R.drawable.mantou, options)
        options.inJustDecodeBounds = false
        options.inDensity = options.outWidth
        options.inTargetDensity = width
        return BitmapFactory.decodeResource(resources, R.drawable.mantou, options)
    }
}