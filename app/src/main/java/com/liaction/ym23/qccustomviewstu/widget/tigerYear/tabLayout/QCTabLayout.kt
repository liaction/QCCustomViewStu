package com.liaction.ym23.qccustomviewstu.widget.tigerYear.tabLayout

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.children
import com.liaction.ym23.qccustomviewstu.util.dpx
import com.liaction.ym23.qccustomviewstu.util.layout
import com.liaction.ym23.qccustomviewstu.util.qcLog

class QCTabLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {
    private var tabLayoutHeight = 0
    private val gestureDetector = GestureDetectorCompat(context, object :
        GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent?): Boolean {
            return true
        }

        override fun onScroll(
            downEvent: MotionEvent,
            currentEvent: MotionEvent,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            val resultScrollY =
                (scrollY + distanceY).coerceAtMost(
                    tabLayoutHeight -
                            height.toFloat() + 0.dpx
                )
                    .coerceAtLeast(0f)
//            scrollBy(0, (resultScrollY - scrollY).toInt())
            scrollTo(0, resultScrollY.toInt())
            return false
        }
    }).apply {
        setIsLongpressEnabled(false)
    }

    override fun shouldDelayChildPressedState(): Boolean {
        return height < tabLayoutHeight
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMeasureMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthMeasureSize = MeasureSpec.getSize(widthMeasureSpec)
        var needWidth = 0
        val needHeight: Int

        var lineWidthUsed = 0
        var lineHeightMax = 0
        var heightUsed = 0

        children.forEach { child ->
            // 设置MarginLayoutParams
            val childLayoutParams = child.layoutParams
            if (childLayoutParams !is MarginLayoutParams) {
                child.layoutParams = MarginLayoutParams(childLayoutParams)
            }
            val childMarginLayoutParams = child.layoutParams as MarginLayoutParams
            // 设置tag
            val childTag = child.tag
            if (childTag !is QCViewTag) {
                child.tag = QCViewTag(originData = childTag)
            }

            // 测量&处理
            resolveChildBound(
                child,
                widthMeasureSpec,
                heightMeasureSpec,
                lineWidthUsed,
                heightUsed
            ).let { resolveChildBound ->
                lineWidthUsed = (resolveChildBound.right + childMarginLayoutParams.rightMargin)
            }

            // 如果宽度不够用，需要换行了
            if (widthMeasureMode != MeasureSpec.UNSPECIFIED && lineWidthUsed > widthMeasureSize) {
                lineWidthUsed = 0
                heightUsed += lineHeightMax
                lineHeightMax = 0
                resolveChildBound(
                    child,
                    widthMeasureSpec,
                    heightMeasureSpec,
                    lineWidthUsed,
                    heightUsed
                ).let { resolveChildBound ->
                    lineWidthUsed = (resolveChildBound.right
                            + childMarginLayoutParams.rightMargin)
                }
            }
            lineHeightMax =
                lineHeightMax.coerceAtLeast(
                    child.measuredHeight
                            + childMarginLayoutParams.bottomMargin + childMarginLayoutParams.topMargin
                )
            needWidth = needWidth.coerceAtLeast(lineWidthUsed)
        }

        needHeight = (heightUsed + lineHeightMax)

        val resultWidth = resolveSize(needWidth, widthMeasureSpec)
        val resultHeight = resolveSize(needHeight, heightMeasureSpec)
        tabLayoutHeight = needHeight
        setMeasuredDimension(resultWidth, resultHeight)
    }

    private fun resolveChildBound(
        child: View,
        widthMeasureSpec: Int,
        heightMeasureSpec: Int,
        lineWidthUsed: Int,
        heightUsed: Int
    ): Rect {
        val childMarginLayoutParams = child.layoutParams as MarginLayoutParams
        // 测量child
        measureChildWithMargins(
            child,
            widthMeasureSpec,
            0,
            heightMeasureSpec,
            0
        )

        // 处理逻辑
        val childResultTag = child.tag as QCViewTag
        val childBound = childResultTag.bound
        childBound.set(
            lineWidthUsed + childMarginLayoutParams.leftMargin,
            heightUsed + childMarginLayoutParams.topMargin,
            lineWidthUsed + child.measuredWidth + childMarginLayoutParams.leftMargin,
            heightUsed + child.measuredHeight + childMarginLayoutParams.topMargin
        )
        if (child is TextView) {
            val text = child.text
            qcLog("$text : $childBound : [${childMarginLayoutParams.leftMargin}," +
                    "${childMarginLayoutParams.topMargin}," +
                    "${childMarginLayoutParams.rightMargin}," +
                    "${childMarginLayoutParams.bottomMargin}]")
        }
        return childBound
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        children.forEachIndexed { _, child ->
            (child.tag as? QCViewTag)?.let { viewTag ->
                child.tag = viewTag.originData
                child.layout(viewTag.bound)
            }
        }
    }

    private data class QCViewTag(val originData: Any?, val bound: Rect = Rect())
}