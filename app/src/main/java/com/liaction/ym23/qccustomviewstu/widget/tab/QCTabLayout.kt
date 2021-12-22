package com.liaction.ym23.qccustomviewstu.widget.tab

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import com.liaction.ym23.qccustomviewstu.util.dpx
import com.liaction.ym23.qccustomviewstu.util.qcLog
import kotlin.math.max

class QCTabLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    init {
        setBackgroundColor(Color.TRANSPARENT)
    }

    private var downEventY = 0f
    private var lastScrollY = 0
    private val childrenRectList = mutableListOf<Rect>()
    private var selfWidth = 0
    private var selfHeight = 0

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        qcMeasureChildren(widthMeasureSpec, heightMeasureSpec)
    }

    private fun qcMeasureChildren(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSpecMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSpecSize = MeasureSpec.getSize(widthMeasureSpec)
        var widthUsed = 0
        var heightUsed = 0
        var lineWidthUsed = 0
        var lineHeightUsed = 0

        children.forEachIndexed { index, view ->
            var layoutParamsOld = view.layoutParams
            if (layoutParamsOld !is MarginLayoutParams) {
                view.layoutParams = MarginLayoutParams(layoutParamsOld)
            }
            var layoutParamsNew = view.layoutParams as MarginLayoutParams
            // 测量
            measureChildWithMargins(
                view,
                widthMeasureSpec,
                0,
                heightMeasureSpec,
                0
            )
            if (widthSpecMode != MeasureSpec.UNSPECIFIED && lineWidthUsed + view.measuredWidth > widthSpecSize) {
                lineWidthUsed = 0
                heightUsed += lineHeightUsed
                lineHeightUsed = 0
                measureChildWithMargins(
                    view,
                    widthMeasureSpec,
                    0,
                    heightMeasureSpec,
                    0
                )
            }

            // 设置位置
            childrenRectList.elementAtOrElse(index) {
                val rect = Rect()
                childrenRectList.add(rect)
                rect
            }.apply {
                set(
                    lineWidthUsed,
                    heightUsed,
                    lineWidthUsed + view.measuredWidth,
                    heightUsed + view.measuredHeight
                )
            }
            lineWidthUsed += view.measuredWidth
            widthUsed = max(widthUsed, lineWidthUsed)
            lineHeightUsed = max(lineHeightUsed, view.measuredHeight)
        }
        selfWidth = widthUsed
        selfHeight = (heightUsed + lineHeightUsed)
        qcLog(" measure : width = $selfWidth, height = $selfHeight")
        setMeasuredDimension(selfWidth, selfHeight)
    }

    private fun resolveMeasure(
        view: View,
        widthMeasureSpec: Int,
        heightMeasureSpec: Int,
        widthUsed: Int,
        heightUsed: Int
    ) {
        val widthSpecMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSpecSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSpecMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSpecSize = MeasureSpec.getSize(heightMeasureSpec)
        val layoutParams = view.layoutParams
        // 处理 width
        var childWidthSpecMode = 0
        var childWidthSpecSize = 0
        when (layoutParams.width) {
            LayoutParams.MATCH_PARENT -> {
                when (widthSpecMode) {
                    MeasureSpec.EXACTLY -> {
                        childWidthSpecMode = MeasureSpec.EXACTLY
                        childWidthSpecSize = widthSpecSize - widthUsed
                    }
                    MeasureSpec.AT_MOST -> {
                        childWidthSpecMode = MeasureSpec.AT_MOST
                        childWidthSpecSize = widthSpecSize - widthUsed
                    }
                    MeasureSpec.UNSPECIFIED -> {
                        childWidthSpecMode = MeasureSpec.UNSPECIFIED
                        childWidthSpecSize = 0
                    }
                }
            }
            LayoutParams.WRAP_CONTENT -> {
                when (widthSpecMode) {
                    MeasureSpec.EXACTLY -> {
                        childWidthSpecMode = MeasureSpec.AT_MOST
                        childWidthSpecSize = widthSpecSize - widthUsed
                    }
                    MeasureSpec.AT_MOST -> {
                        childWidthSpecMode = MeasureSpec.AT_MOST
                        childWidthSpecSize = widthSpecSize - widthUsed
                    }
                    MeasureSpec.UNSPECIFIED -> {
                        childWidthSpecMode = MeasureSpec.UNSPECIFIED
                        childWidthSpecSize = 0
                    }
                }
            }
            else -> {
                childWidthSpecMode = MeasureSpec.EXACTLY
                childWidthSpecSize = layoutParams.width
            }
        }
        // 处理 height
        var childHeightSpecMode = 0
        var childHeightSpecSize = 0
        when (layoutParams.height) {
            LayoutParams.MATCH_PARENT -> {
                when (heightSpecMode) {
                    MeasureSpec.EXACTLY -> {
                        childHeightSpecMode = MeasureSpec.EXACTLY
                        childHeightSpecSize = heightSpecSize - heightUsed
                    }
                    MeasureSpec.AT_MOST -> {
                        childHeightSpecMode = MeasureSpec.AT_MOST
                        childHeightSpecSize = heightSpecSize - heightUsed
                    }
                    MeasureSpec.UNSPECIFIED -> {
                        childHeightSpecMode = MeasureSpec.UNSPECIFIED
                        childHeightSpecSize = 0
                    }
                }
            }
            LayoutParams.WRAP_CONTENT -> {
                when (heightSpecMode) {
                    MeasureSpec.EXACTLY -> {
                        childHeightSpecMode = MeasureSpec.AT_MOST
                        childHeightSpecSize = heightSpecSize - heightUsed
                    }
                    MeasureSpec.AT_MOST -> {
                        childHeightSpecMode = MeasureSpec.AT_MOST
                        childHeightSpecSize = heightSpecSize - heightUsed
                    }
                    MeasureSpec.UNSPECIFIED -> {
                        childHeightSpecMode = MeasureSpec.UNSPECIFIED
                        childHeightSpecSize = 0
                    }
                }
            }
            else -> {
                childHeightSpecMode = MeasureSpec.EXACTLY
                childHeightSpecSize = layoutParams.height
            }
        }

        // 子view进行测量
        view.measure(
            MeasureSpec.makeMeasureSpec(childWidthSpecSize, childWidthSpecMode),
            MeasureSpec.makeMeasureSpec(childHeightSpecSize, childHeightSpecMode)
        )
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                downEventY = event.y
                lastScrollY = scrollY
                parent.requestDisallowInterceptTouchEvent(true)
            }
        }
        return true
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                downEventY = event.y
                lastScrollY = scrollY
            }
            MotionEvent.ACTION_MOVE -> {
                val dy = (downEventY - event.y + lastScrollY).coerceAtLeast(0f)
                    .coerceAtMost((selfHeight - height).toFloat())
                scrollTo(0, dy.toInt())
            }
        }
        return true
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
        strokeWidth = 5.dpx
        style = Paint.Style.STROKE
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRect(0f, 0f, selfWidth.toFloat(), selfHeight.toFloat(), paint)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        qcLog(" onLayout : width = ${r - l}, height = ${b - t}")
        children.forEachIndexed { index, view ->
            val childRect = childrenRectList[index]
            view.layout(childRect.left, childRect.top, childRect.right, childRect.bottom)
        }
    }
}