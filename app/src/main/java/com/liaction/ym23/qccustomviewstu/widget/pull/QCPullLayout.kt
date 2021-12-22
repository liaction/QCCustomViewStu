package com.liaction.ym23.qccustomviewstu.widget.pull

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.core.view.children
import com.liaction.ym23.qccustomviewstu.util.layout
import com.liaction.ym23.qccustomviewstu.util.qcLog

class QCPullLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    private val childrenRectList = mutableListOf<Rect>()
    private var actionHeight = 0
    private var downEventY = 0f
    private var lastScrollY = 0
    private var triggerRefreshBottomTipEvent = false
    private var triggerRefreshTopTipEvent = false
    private var refreshing = false


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        measureChildren(widthMeasureSpec, heightMeasureSpec)
        var offsetTop = 0
        children.forEachIndexed { index, view ->
            if (index == 0) {
                offsetTop = -view.measuredHeight
                actionHeight = -offsetTop
            }
            childrenRectList.getOrElse(index) {
                val rect = Rect()
                childrenRectList.add(rect)
                rect
            }.apply {
                set(0, offsetTop, view.measuredWidth, offsetTop + view.measuredHeight)
                offsetTop += view.measuredHeight
            }
        }
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
                if (!refreshing) {
                    val dy = (downEventY - event.y + lastScrollY).toInt().coerceAtMost(0)
                        .coerceAtLeast(-actionHeight)
                    if (dy > -actionHeight / 2 && dy < 0) {
                        triggerRefreshTopTipEvent = false
                        if (!triggerRefreshBottomTipEvent) {
                            triggerRefreshBottomTipEvent = true
                            qcLog("继续下拉可刷新")
                        }
                    } else if (dy <= -actionHeight / 2) {
                        triggerRefreshBottomTipEvent = false
                        if (!triggerRefreshTopTipEvent) {
                            triggerRefreshTopTipEvent = true
                            qcLog("松手进行刷新")
                        }
                    }
                    scrollTo(0, dy)
                } else {
                    qcLog("正在刷新中，不进行处理")
                }
            }
            MotionEvent.ACTION_UP -> {
                if (!refreshing) {
                    if (scrollY.toFloat() > -actionHeight / 2f) {
                        scrollTo(0, 0)
                        refreshing = false
                        if (triggerRefreshBottomTipEvent){
                            qcLog("放弃刷新")
                        }
                    } else {
                        scrollTo(0, -actionHeight)
                        if (triggerRefreshTopTipEvent){
                            qcLog("可以刷新")
                        }
                        refreshing = true
                        doRefresh()
                    }
                    triggerRefreshBottomTipEvent = false
                    triggerRefreshTopTipEvent = false
                }
            }
        }
        return true
    }

    private fun doRefresh() {
        if (refreshing){
            return
        }
        postDelayed({
            scrollTo(0, 0)
            refreshing = false
            qcLog("刷新结束")
        }, 5000)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        children.forEachIndexed { index, view ->
            view.layout(childrenRectList[index])
        }
    }
}