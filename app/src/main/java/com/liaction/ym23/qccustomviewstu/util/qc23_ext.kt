package com.liaction.ym23.qccustomviewstu.util

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.util.TypedValue
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.NonNull

val Number.dpx: Float
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    )

val Number.spx: Float
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    )


fun csLog(message: String?) {
    Log.e("QC23", "-----> 【$message】")
}

fun qcLog(message: String?) {
    csLog(message)
}

fun qcGetImageFromSource(
    width: Int,
    @NonNull resources: Resources,
    @DrawableRes drawableResId: Int
): Bitmap {
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    BitmapFactory.decodeResource(resources, drawableResId, options)
    options.inJustDecodeBounds = false
    options.inDensity = options.outWidth
    options.inTargetDensity = width
    return BitmapFactory.decodeResource(resources, drawableResId, options)
}

fun qcShowMeasureSpecInfo(measureSpec: Int, tipExtraMessageString: String = "qc23") {
    qcLog("-------show measure spec info 【$tipExtraMessageString】 start -------")
    val mode = View.MeasureSpec.getMode(measureSpec)
    val size = View.MeasureSpec.getSize(measureSpec)
    when (mode) {
        View.MeasureSpec.EXACTLY -> {
            csLog("mode : MeasureSpec.EXACTLY【$mode】, size = $size")
        }
        View.MeasureSpec.AT_MOST -> {
            csLog("mode : MeasureSpec.AT_MOST【$mode】, size = $size")
        }
        View.MeasureSpec.UNSPECIFIED -> {
            csLog("mode : MeasureSpec.UNSPECIFIED【$mode】, size = $size")
        }
    }
    qcLog("-------show measure spec info 【$tipExtraMessageString】 end -------")
}