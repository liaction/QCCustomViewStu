package com.liaction.ym23.qccustomviewstu.widget.circleImage

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.liaction.ym23.qccustomviewstu.R
import com.liaction.ym23.qccustomviewstu.util.dpx

class QCircleImage(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private val imageWidth = 180.dpx
    private val circleRadius = 90.dpx
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLUE
    }
    private val outPadding = 10.dpx

    private val xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)

    override fun onDraw(canvas: Canvas) {
        val saveLayer = canvas.saveLayer(
            outPadding,outPadding, outPadding + imageWidth, outPadding+ imageWidth, paint
        )
        canvas.drawOval(outPadding,outPadding, outPadding + imageWidth, outPadding+ imageWidth,paint)
        paint.xfermode = xfermode
        getDrawImageFromSource(imageWidth.toInt())?.let {
            canvas.drawBitmap(
                it,
                outPadding,
                outPadding,
                paint
            )
            canvas.restoreToCount(saveLayer)
        }
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 5.dpx
        canvas.drawOval(outPadding,outPadding, outPadding + imageWidth, outPadding+ imageWidth,paint)
    }

    private fun getDrawImageFromSource(width: Int): Bitmap? {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(resources, R.drawable.mantou, options)
        options.inJustDecodeBounds = false
        options.inDensity = options.outWidth
        options.inTargetDensity = width
        options.outWidth = width
        options.outHeight = width
        return BitmapFactory.decodeResource(resources, R.drawable.mantou, options)
    }
}