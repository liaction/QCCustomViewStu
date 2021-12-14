package com.liaction.ym23.qccustomviewstu.widget.pie

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.liaction.ym23.qccustomviewstu.util.dpx
import kotlin.math.cos
import kotlin.math.sin

class QCPieView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private val qcPadding = 50.dpx
    private val qcOutLength = 23.dpx
    private val pieAngleData = floatArrayOf(60f, 120f, 45f, 105f, 30f)
    private val pieAngleColorData = listOf(
        Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.CYAN
    )

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLUE
    }

    override fun onDraw(canvas: Canvas) {
        var startAngle = 0f
        val shouldOutIndex = 2
        pieAngleData.forEachIndexed { index, angle ->
            paint.color = pieAngleColorData[index]
            if (index == shouldOutIndex) {
                canvas.save()
                canvas.translate(
                    (qcOutLength * cos(Math.toRadians(startAngle + (angle / 2).toDouble()))).toFloat(),
                    (qcOutLength * sin(Math.toRadians(startAngle + (angle / 2).toDouble()))).toFloat()
                )
            }
            canvas.drawArc(
                qcPadding,
                qcPadding,
                width.toFloat() - qcPadding,
                height.toFloat() - qcPadding,
                startAngle,
                angle,
                true,
                paint
            )
            startAngle += angle
            if (index == shouldOutIndex) {
                canvas.restore()
            }
        }
    }
}