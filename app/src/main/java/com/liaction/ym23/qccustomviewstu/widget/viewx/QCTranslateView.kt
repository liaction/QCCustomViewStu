package com.liaction.ym23.qccustomviewstu.widget.viewx

import android.content.Context
import android.graphics.Canvas
import android.graphics.Matrix
import android.util.AttributeSet
import android.view.View
import com.liaction.ym23.qccustomviewstu.R
import com.liaction.ym23.qccustomviewstu.util.dpx
import com.liaction.ym23.qccustomviewstu.util.qcGetImageFromSource

class QCTranslateView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val bitmap = qcGetImageFromSource(230.dpx.toInt(),resources, R.drawable.mantou)
    private val bitmapMatrix = Matrix()
    override fun onDraw(canvas: Canvas) {
//        canvas.translate(200.dpx, 0f)
        bitmapMatrix.reset()
        bitmapMatrix.postTranslate(100.dpx, 0f)
        canvas.setMatrix(bitmapMatrix)
        canvas.drawBitmap(bitmap, 0f, 0f, null)
    }
}