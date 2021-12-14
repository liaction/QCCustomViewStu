package com.liaction.ym23.qccustomviewstu.widget.dash

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.liaction.ym23.qccustomviewstu.util.dpx
import kotlin.math.cos
import kotlin.math.sin

private const val ANGLE = 120
private const val START_ANGLE = 90 + ANGLE / 2f
private const val SWEEP_ANGLE = 360 - ANGLE

class QCDashView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val arcPaint: Paint = Paint().apply {
        strokeWidth = 3.dpx
        style = Paint.Style.STROKE
    }
    private val dashWidth = 3.dpx
    private val dashHeight = 10.dpx
    private val linePaint: Paint = Paint().apply {
        strokeWidth = dashWidth
        color = Color.BLUE
    }
    private val arcPath = Path()
    private lateinit var arcPathMeasure: PathMeasure
    private var dashLineCount = 23
    private val dashLinePathPaint = Paint()
    private val dashLinePathPaintWithoutPathEffect = Paint().apply {
        strokeWidth = dashWidth
    }
    private lateinit var dashPathEffect: PathDashPathEffect
    private val dashPath = Path().apply {
        addRect(RectF(0f, 0f, dashWidth, dashHeight), Path.Direction.CW)
    }
    private val radius = 100.dpx
    private lateinit var centerRect: RectF
    private var lineLength = 0f

    private var lineIndex = 8

    private var enableDashPathEffect = false

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        val centerX = w / 2f
        val centerY = h / 2f
        centerRect = RectF(
            centerX - radius,
            centerY - radius,
            centerX + radius,
            centerY + radius
        )
        lineLength = radius * 3 / 4f
        arcPath.reset()
        arcPath.addArc(centerRect, START_ANGLE, SWEEP_ANGLE.toFloat())
        arcPathMeasure = PathMeasure(arcPath, false)
        dashPathEffect = PathDashPathEffect(
            dashPath,
            (arcPathMeasure.length - dashWidth) / dashLineCount,
            0f,
            PathDashPathEffect.Style.MORPH
        )
        dashLinePathPaint.pathEffect = dashPathEffect
    }

    override fun onDraw(canvas: Canvas) {
        // 画弧
        canvas.drawPath(arcPath, arcPaint)
        // 画刻度
        if (enableDashPathEffect) {
            canvas.drawPath(arcPath, dashLinePathPaint)
        } else {
            drawArcLine(canvas)
        }
        // 画指针
        canvas.drawLine(
            centerRect.centerX(),
            centerRect.centerY(),
            resolveLineX(lineIndex, lineLength),
            resolveLineY(lineIndex, lineLength),
            linePaint
        )
    }

    private fun drawArcLine(canvas: Canvas) {
        // todo 圆弧上点到中心的距离，需要计算，这里还没计算，所以显示就有问题
        val arcRadiusLength = radius
        val everyAngle = SWEEP_ANGLE / dashLineCount.toDouble()
        for (index in 0 .. dashLineCount) {
            val offsetX = cos(Math.toRadians(START_ANGLE + everyAngle * index)) * arcRadiusLength
            val archX = (offsetX + centerRect.centerX()).toFloat()
            val offsetY = sin(Math.toRadians(START_ANGLE + everyAngle * index)) * arcRadiusLength
            val archY =  (offsetY + centerRect.centerY()).toFloat()
            canvas.drawLine(
                archX,
                archY,
                resolveLineX(index, lineLength + dashHeight),
                resolveLineY(index, lineLength + dashHeight),
                dashLinePathPaintWithoutPathEffect
            )
        }
    }

    private fun resolveLineX(lineIndex: Int, lineLength: Float): Float {
        val everyAngle = SWEEP_ANGLE / dashLineCount.toDouble()
        val offsetX = cos(Math.toRadians(START_ANGLE + everyAngle * lineIndex)) * lineLength
        return (offsetX + centerRect.centerX()).toFloat()
    }

    private fun resolveLineY(lineIndex: Int, lineLength: Float): Float {
        val everyAngle = SWEEP_ANGLE / dashLineCount.toDouble()
        val offsetY = sin(Math.toRadians(START_ANGLE + everyAngle * lineIndex)) * lineLength
        return (offsetY + centerRect.centerY()).toFloat()
    }

}