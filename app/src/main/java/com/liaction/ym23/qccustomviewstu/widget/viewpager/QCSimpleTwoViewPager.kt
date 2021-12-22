package com.liaction.ym23.qccustomviewstu.widget.viewpager

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.widget.OverScroller
import androidx.core.view.children
import com.liaction.ym23.qccustomviewstu.util.qcLog
import kotlin.math.abs

class QCSimpleTwoViewPager @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {
    private var downEventX = 0f
    private var isScrolling = false
    private var downScrollX = 0f
    private val viewConfiguration = ViewConfiguration.get(context)
    private val velocityTracker = VelocityTracker.obtain()
    private val overScroller = OverScroller(context)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        measureChildren(widthMeasureSpec, heightMeasureSpec)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        var intercept = false
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                velocityTracker.clear()
                velocityTracker.addMovement(event)
                downEventX = event.x
                isScrolling = false
                downScrollX = scrollX.toFloat()
            }
            MotionEvent.ACTION_MOVE -> {
                velocityTracker.addMovement(event)
                if (!isScrolling) {
                    val distanceX = event.x - downEventX
                    if (abs(distanceX) > viewConfiguration.scaledPagingTouchSlop) {
                        intercept = true
                        parent.requestDisallowInterceptTouchEvent(true)
                        isScrolling = true
                    }
                }
            }
        }
        return intercept
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                velocityTracker.clear()
                velocityTracker.addMovement(event)
                downEventX = event.x
                isScrolling = false
                downScrollX = scrollX.toFloat()
            }
            MotionEvent.ACTION_MOVE -> {
                velocityTracker.addMovement(event)
                val dx = (downEventX - event.x + downScrollX).toInt()
                    .coerceAtMost(width)
                    .coerceAtLeast(0)
                scrollTo(dx, 0)
            }
            MotionEvent.ACTION_UP -> {
                velocityTracker.computeCurrentVelocity(
                    1000,
                    viewConfiguration.scaledMaximumFlingVelocity.toFloat()
                )
                val destDx = if (velocityTracker.xVelocity > viewConfiguration.scaledMinimumFlingVelocity){
                    if (velocityTracker.xVelocity < 0){
                        width - scrollX
                    }else{
                        -scrollX
                    }
                }else {
                    if (scrollX >= width / 2f) {
                        width - scrollX
                    } else {
                        -scrollX
                    }
                }
                overScroller.startScroll(scrollX, 0, destDx, 0)
                postInvalidateOnAnimation()
            }
        }
        return true
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        children.forEachIndexed { index, view ->
            view.layout(
                0 + width * index, 0,
                width * (index + 1), height
            )
        }
    }

    override fun computeScroll() {
        if (overScroller.computeScrollOffset()) {
            scrollTo(overScroller.currX, overScroller.currY)
            postInvalidateOnAnimation()
        }
    }
}