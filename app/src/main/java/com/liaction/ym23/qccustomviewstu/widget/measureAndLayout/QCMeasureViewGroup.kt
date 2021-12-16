package com.liaction.ym23.qccustomviewstu.widget.measureAndLayout

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.util.AttributeSet
import android.util.SparseArray
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.children
import com.liaction.ym23.qccustomviewstu.util.qcLog
import com.liaction.ym23.qccustomviewstu.util.qcToast

class QCMeasureViewGroup @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    private val childrenBoundSparseArray = SparseArray<Rect>()

    init {
        setBackgroundColor(Color.CYAN)
    }

    private var maxHeight = 0
    private var canScrollYMaxDistance = 0
    private var downOffsetY = 0f
    private var lastScrollY = 0
    private var haveScrollBottom = false
    private var haveScrollTop = false

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                downOffsetY = event.y
                lastScrollY = scrollY
            }
            MotionEvent.ACTION_MOVE -> {
                val moveDistance = (event.y - downOffsetY).toInt()
                if (((scrollY == 0 && lastScrollY == 0) || (scrollY == canScrollYMaxDistance && lastScrollY == canScrollYMaxDistance)) && moveDistance != 0) {
                    downOffsetY = event.y
                    lastScrollY = scrollY
                }
                qcLog("moveDistance : $moveDistance, scrollY: $scrollY , lastScrollY: $lastScrollY , canScrollYMaxDistance: $canScrollYMaxDistance")
                val canContinueScroll = scrollY in 0..canScrollYMaxDistance
                if (canContinueScroll) {
                    scrollTo(0, -moveDistance + lastScrollY)
                }
                if (scrollY < 0) {
                    scrollTo(0, 0)
                } else if (scrollY > canScrollYMaxDistance) {
                    scrollTo(0, canScrollYMaxDistance)
                }
                if (scrollY == 0 && moveDistance > 0) {
                    if (!haveScrollTop){
                        haveScrollTop = true
                        qcLog("到顶了.......")
                        qcToast("到顶了")
                    }
                } else if (scrollY == canScrollYMaxDistance && moveDistance < 0) {
                    if (!haveScrollBottom){
                        haveScrollBottom = true
                        qcLog("到底了.......")
                        qcToast("到底了")
                    }
                }else{
                    haveScrollTop = false
                    haveScrollBottom = false
                }
            }
            MotionEvent.ACTION_UP->{
                haveScrollTop = false
                haveScrollBottom = false
            }
        }
        return true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
//        qcShowMeasureSpecInfo(widthMeasureSpec, tipExtraMessageString = "parent 宽")
//        qcShowMeasureSpecInfo(heightMeasureSpec, tipExtraMessageString = "parent 高")
        measureChildren(widthMeasureSpec, heightMeasureSpec)
        // 假如垂直排列
        var startHeight = 0
        children.forEachIndexed { index, child ->
            if (childrenBoundSparseArray.indexOfKey(index) < 0) {
                // 这里创建Rect对象是没有问题的，因为只创建一遍，后续就不会再创建了
                childrenBoundSparseArray.put(index, Rect())
            }
            val rect = childrenBoundSparseArray[index]
            rect.set(0, startHeight, child.measuredWidth, startHeight + child.measuredHeight)
            startHeight += child.measuredHeight
        }
        maxHeight = startHeight
        canScrollYMaxDistance = (maxHeight - measuredHeight)
        setMeasuredDimension(measuredWidth, measuredHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        children.forEachIndexed { index, child ->
            val childBound = childrenBoundSparseArray.get(index)
            child.layout(childBound.left, childBound.top, childBound.right, childBound.bottom)
        }
    }

}