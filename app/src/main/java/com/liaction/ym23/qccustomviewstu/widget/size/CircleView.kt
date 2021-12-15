package com.liaction.ym23.qccustomviewstu.widget.size

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.liaction.ym23.qccustomviewstu.util.dpx

private val IMAGE_PADDING = 23.dpx
private val RADIUS = 100.dpx

class CircleView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private val paint: Paint = Paint()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val size = ((IMAGE_PADDING + RADIUS) * 2).toInt()
        val height = resolveSize(size, widthMeasureSpec)
        val width = resolveSize(size, heightMeasureSpec)
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawCircle(IMAGE_PADDING + RADIUS, IMAGE_PADDING + RADIUS, RADIUS, paint)
    }

}