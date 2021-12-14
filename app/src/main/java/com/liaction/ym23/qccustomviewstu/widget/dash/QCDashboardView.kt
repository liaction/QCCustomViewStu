package com.liaction.ym23.qccustomviewstu.widget.dash

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.liaction.ym23.qccustomviewstu.util.dpx
import kotlin.math.cos
import kotlin.math.sin

private val START_PADDING = 50.dpx
private const val ANGLE = 100
private val DASH_WIDTH = 3.dpx
private val DASH_HEIGHT = 8.dpx
private const val DASH_COUNT = 20

/**
 * todo 刻度和指针对不齐
 */
class QCDashboardView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val path = Path()
    private val dashPath = Path()
    private val pathMeasure = PathMeasure()
    private lateinit var pathDashPathEffect: PathDashPathEffect
    private val dashHeight by lazy {
        width - START_PADDING * 2
    }
    private val dashPointerLength by lazy {
        (dashHeight / 2) * ( 3 / 4.0)
    }
    private val dashboardRadius by lazy {
        dashHeight / 2
    }

    init {
        paint.color = Color.BLUE
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 3.dpx
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        path.reset()
        path.addArc(
            START_PADDING, START_PADDING, width - START_PADDING,
            dashHeight + START_PADDING,
            ((90 + ANGLE / 2).toFloat()), ((360 - ANGLE).toFloat())
        )
        pathMeasure.setPath(path, false)

        dashPath.reset()
        dashPath.addRect(0f, 0f, DASH_WIDTH, DASH_HEIGHT, Path.Direction.CCW)
        pathDashPathEffect =
            PathDashPathEffect(
                dashPath,
                (pathMeasure.length - DASH_WIDTH) / (DASH_COUNT),
                0f,
                PathDashPathEffect.Style.ROTATE
            )
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawPath(path, paint)
        paint.pathEffect = pathDashPathEffect
        canvas.drawPath(path, paint)
        paint.pathEffect = null
        val startX = (width) / 2f
        val startY = (dashHeight) / 2 + START_PADDING
        val pointerIndex = 5
        val startAngle = 90 + ANGLE / 2.0
        paint.color = Color.RED
        canvas.drawLine(
            startX, startY,
            (startX + dashPointerLength * (cos(Math.toRadians(startAngle + ((360 - ANGLE) / DASH_COUNT) * pointerIndex)))).toFloat(),
            (startY + dashPointerLength * (sin(Math.toRadians(startAngle + ((360 - ANGLE) / DASH_COUNT) * pointerIndex)))).toFloat(),
            paint
        )
    }

}