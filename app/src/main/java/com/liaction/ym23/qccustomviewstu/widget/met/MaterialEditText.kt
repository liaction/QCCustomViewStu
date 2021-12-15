package com.liaction.ym23.qccustomviewstu.widget.met

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.TextPaint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.liaction.ym23.qccustomviewstu.R
import com.liaction.ym23.qccustomviewstu.util.dpx

private val FLOAT_LABEL_HEIGHT = 12.dpx
private val TIP_MESSAGE_HEIGHT = 12.dpx
private val PADDING = 8.dpx
private val FLOAT_LABEL_OFFSET = 16.dpx

class MaterialEditText @JvmOverloads constructor(
    context: Context,val attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {
    private val floatLabelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = FLOAT_LABEL_HEIGHT
    }
    private val underlinePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.GRAY
        strokeWidth = 2.dpx
    }
    private val tipMessagePaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = TIP_MESSAGE_HEIGHT
        color = Color.RED
    }

    private var floatLabelShowing = false
    var useFloatLabel = false
        set(value) {
            if (field != value) {
                floatLabelShowing = false
                if (field) {
                    floatLabelAnimator.reverse()
                    removeFloatLabelPadding()
                }
                field = value
                if (field) {
                    checkFloatLabelState(text)
                }
                invalidate()
            }
        }
    var floatLabelText: String? = ""
        set(value) {
            if (value != field) {
                field = value?.trim()
                invalidate()
            }
        }
    var tipText: String? = null
        set(value) {
            if (value != field) {
                if (field.isNullOrEmpty()){
                    setPadding(paddingLeft,paddingTop,paddingRight,
                        (paddingBottom + TIP_MESSAGE_HEIGHT + PADDING).toInt()
                    )
                }
                field = value?.trim()
                if (field.isNullOrEmpty()){
                    setPadding(paddingLeft,paddingTop,paddingRight,
                        (paddingBottom - TIP_MESSAGE_HEIGHT - PADDING).toInt()
                    )
                }
                invalidate()
            }
        }
    var floatLabelFraction = 0f
        set(value) {
            field = value
            invalidate()
        }
    private val floatLabelAnimator by lazy {
        ObjectAnimator.ofFloat(this, "floatLabelFraction", 0f, 1f)
    }

    private val extraPaddingRight by lazy {
        (paddingRight+ PADDING*2).toInt()
    }

    init {
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.MaterialEditText)
        useFloatLabel = typeArray.getBoolean(R.styleable.MaterialEditText_useFloatLabel, true)
        floatLabelText = typeArray.getNonResourceString(R.styleable.MaterialEditText_floatLabelText)
        tipText = typeArray.getNonResourceString(R.styleable.MaterialEditText_tipText)
        typeArray.recycle()
        background = null
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val fixHeight = if (checkTipMessageText()) TIP_MESSAGE_HEIGHT * 0 else 0f
        setMeasuredDimension(measuredWidth, (measuredHeight + fixHeight).toInt())
    }

    private fun checkFloatLabelText() =
        !floatLabelText.isNullOrEmpty() && floatLabelText!!.trim().isNotBlank()

    private fun checkTipMessageText() =
        !tipText.isNullOrEmpty() && tipText!!.trim().isNotBlank()

    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        if (!useFloatLabel) {
            return
        }
        checkFloatLabelState(text)
    }

    private fun checkFloatLabelState(text: CharSequence?) {
        val haveContent = !text.isNullOrEmpty()
        if (floatLabelShowing) {
            if (haveContent) {
                return
            }
            floatLabelAnimator.reverse()
            floatLabelShowing = false
            removeFloatLabelPadding()
            return
        }
        if (haveContent) {
            floatLabelAnimator.start()
            floatLabelShowing = true
            addFloatLabelPadding()
        }
    }

    private fun removeFloatLabelPadding() {
        val offset =
            (FLOAT_LABEL_HEIGHT + PADDING)
        setPadding(
            paddingLeft,
            (paddingTop - offset).toInt(),
            extraPaddingRight,
            paddingBottom
        )
    }

    private fun addFloatLabelPadding() {
        val offset = (FLOAT_LABEL_HEIGHT + PADDING)
        setPadding(
            paddingLeft,
            (paddingTop + offset).toInt(),
            extraPaddingRight,
            paddingBottom
        )
    }

    override fun onDraw(canvas: Canvas) {
        if (checkFloatLabelText()) {
            floatLabelPaint.alpha = (0xFF * floatLabelFraction).toInt()
            canvas.drawText(
                floatLabelText!!.trim(),
                paddingLeft.toFloat(),
                paddingTop.toFloat() - PADDING - FLOAT_LABEL_OFFSET * (1 - floatLabelFraction),
                floatLabelPaint
            )
        }
        if (checkTipMessageText()) {
            canvas.drawText(
                tipText!!.trim(), paddingLeft.toFloat(),
                (height - PADDING), tipMessagePaint
            )
        }
        if (hasFocus()){
            underlinePaint.color = Color.BLUE
        }else{
            underlinePaint.color = Color.GRAY
        }
        canvas.drawLine(paddingLeft.toFloat(),
            (height - paddingBottom + PADDING),
            (width - paddingRight).toFloat(), (height-paddingBottom + PADDING),underlinePaint)
        super.onDraw(canvas)
    }

}