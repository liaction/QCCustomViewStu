package com.liaction.ym23.qccustomviewstu.widget.xferImage

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.liaction.ym23.qccustomviewstu.R
import com.liaction.ym23.qccustomviewstu.util.dpx
import com.liaction.ym23.qccustomviewstu.util.qcGetImageFromSource

class XferView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bitmap = qcGetImageFromSource(230.dpx.toInt(), resources, R.drawable.mantou)
    private val bitmapMatrix = Matrix()
    private val camera = Camera()
    private val imagePadding = 100.dpx
    private var imageRotate = 0f
    private var clipTopImagePath = Path()
    override fun onDraw(canvas: Canvas) {
//        canvas.save()
//        canvas.rotate(45f, bitmap.width / 2f, bitmap.height / 2f)
//        canvas.drawBitmap(bitmap, 0f, 0f, paint)
//        canvas.restore()
//        canvas.save()
//        canvas.rotate(45f, bitmap.width / 2f, bitmap.height / 2f)
//        canvas.translate(50.dpx, 0f)
//        canvas.drawBitmap(bitmap, 0f, 0f, paint)
//        canvas.restore()
//        canvas.save()
//        bitmapMatrix.postTranslate(50.dpx, 0f)
//        bitmapMatrix.postRotate(45f, bitmap.width / 2f, bitmap.height / 2f)
//        canvas.drawBitmap(bitmap, bitmapMatrix, paint)
//        canvas.restore()

        canvas.save()
        canvas.translate(
            bitmap.width / 2f + imagePadding,
            bitmap.height / 2f + imagePadding
        )
        canvas.rotate(-imageRotate)
        clipTopImagePath.reset()
        clipTopImagePath.addRoundRect(
            -bitmap.width.toFloat() / 2,
            -bitmap.height.toFloat() / 2,
            bitmap.width.toFloat() / 2,
            0f,
            floatArrayOf(23.dpx, 23.dpx, 10.dpx, 10.dpx, 0.dpx, 0.dpx, 0.dpx, 0.dpx),
            Path.Direction.CCW
        )
        canvas.clipPath(clipTopImagePath)
//        canvas.clipRect(
//            -bitmap.width.toFloat(),
//            -bitmap.height.toFloat(),
//            bitmap.width.toFloat(), 0f
//        )
        canvas.rotate(imageRotate)
        canvas.translate(
            -bitmap.width / 2f - imagePadding,
            -bitmap.height / 2f - imagePadding
        )
        canvas.drawBitmap(bitmap, imagePadding, imagePadding, paint)
        canvas.restore()

        canvas.save()
        canvas.translate(
            bitmap.width / 2f + imagePadding,
            bitmap.height / 2f + imagePadding
        )
        canvas.rotate(-imageRotate)
        camera.setLocation(0f, -0f, -16f)
        camera.rotateX(45f)
        camera.applyToCanvas(canvas)
        canvas.clipRect(
            -bitmap.width.toFloat(),
            0f,
            bitmap.width.toFloat(), bitmap.height.toFloat()
        )
        canvas.rotate(imageRotate)
        canvas.translate(
            -bitmap.width / 2f - imagePadding,
            -bitmap.height / 2f - imagePadding
        )
        canvas.drawBitmap(bitmap, imagePadding, imagePadding, paint)
        canvas.restore()

    }

}