package com.liaction.ym23.qccustomviewstu.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.core.view.children
import com.liaction.ym23.qccustomviewstu.util.dpx

private val swipeActionWidth = 230.dpx

/**
 * todo : 待修复
 */
class SwipeActionLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {
    private var downX = 0f
    private var originOffsetX = 0f
    private var offsetX = 0f

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        children.forEachIndexed { index, child ->
            if (index == 0){
                child.measure(MeasureSpec.makeMeasureSpec(swipeActionWidth.toInt(),MeasureSpec.EXACTLY),heightMeasureSpec)
            }else{
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0)
            }
        }
        super.onMeasure(MeasureSpec.makeMeasureSpec(swipeActionWidth.toInt() + MeasureSpec.getSize(widthMeasureSpec),MeasureSpec.EXACTLY), heightMeasureSpec)
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var left = -(r - width)
        var right = 0
        children.forEach { child ->
            child.layout((left + offsetX).toInt(), 0, right, height)
            left += right
            right += width
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when(event.actionMasked){
            MotionEvent.ACTION_DOWN->{
                downX = event.x
                originOffsetX = offsetX
            }
            MotionEvent.ACTION_MOVE->{
                offsetX = event.x - downX + originOffsetX
            }
            MotionEvent.ACTION_UP ->{
                originOffsetX = offsetX
            }
        }
        requestLayout()
        return true
    }
}