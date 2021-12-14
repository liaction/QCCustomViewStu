package com.liaction.ym23.qccustomviewstu.widget.touch

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.text.TextPaint
import android.util.AttributeSet
import android.util.SparseArray
import android.view.MotionEvent
import android.view.View
import com.liaction.ym23.qccustomviewstu.util.csLog
import com.liaction.ym23.qccustomviewstu.util.dpx
import com.liaction.ym23.qccustomviewstu.util.spx

class MultiTouchView3 @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
        textSize = 18.spx
        color = Color.BLUE
    }
    private val pointerPathList = SparseArray<Path>()
    private val pointerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeWidth = 5.dpx
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
        style = Paint.Style.STROKE
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                val actionIndex = event.actionIndex
                val pointerId = event.getPointerId(actionIndex)
                pointerPathList.append(pointerId, QCRecyclePath.obtainPath().apply {
                    moveTo(event.getX(actionIndex), event.getY(actionIndex))
                })
            }
            MotionEvent.ACTION_MOVE -> {
                (0 until event.pointerCount).forEach {
                    val actionIndex = it
                    val pointerId = event.getPointerId(actionIndex)
                    pointerPathList.get(pointerId)
                        ?.lineTo(event.getX(actionIndex), event.getY(actionIndex))
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
                val actionIndex = event.actionIndex
                val pointerId = event.getPointerId(actionIndex)
                QCRecyclePath.recycle(pointerPathList.get(pointerId))
                pointerPathList.remove(pointerId)
            }
        }
        invalidate()
        return true
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawText("多点触控-互不干扰型", width / 2f, 0f + textPaint.fontSpacing, textPaint)
        (0 until pointerPathList.size()).forEach {
            canvas.drawPath(pointerPathList.valueAt(it), pointerPaint)
        }
    }

}

private object QCRecyclePath {
    private var initialCapacity: Int = 3
    private const val maxUnUsedCount = 10
    private val cachePathArray =
        MutableList(if (initialCapacity <= 0 || initialCapacity > 10) 3 else initialCapacity) {
            InnerPath()
        }
    private var canUseSize = cachePathArray.size

    fun obtainPath(): Path {
        csLog("obtainPath")
        val path: Path
        if (canUseSize == 0) {
            csLog("没有可用的了，新建一个使用")
            cachePathArray.add(InnerPath(canUse = false).apply {
                path = this.path
            })
        } else {
            csLog("还有可用的，拿一个")
            canUseSize--
            val canUsePathPair = cachePathArray.findLast { it.canUse }
            path = canUsePathPair!!.path
            canUsePathPair.canUse = false
        }
        return path
    }

    fun recycle(path: Path) {
        csLog("recycle path : $canUseSize")
        cachePathArray.firstOrNull { it.path == path }?.let {
            it.path.reset()
            it.canUse = true
            canUseSize++
            csLog("可用值增加 : $canUseSize")
        }
        if (canUseSize > maxUnUsedCount) {
            cachePathArray.filter { it.canUse }.takeLast(canUseSize - maxUnUsedCount).forEach {
                cachePathArray.remove(it)
            }
        }
    }

    private data class InnerPath(var canUse: Boolean = true, val path: Path = Path())
}