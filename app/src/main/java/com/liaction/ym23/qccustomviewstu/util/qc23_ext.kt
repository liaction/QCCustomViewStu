package com.liaction.ym23.qccustomviewstu.util

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.OverScroller
import android.widget.Toast
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

fun Context.qcToast(message: String?) {
    Toast.makeText(this, "$message", Toast.LENGTH_SHORT).show()
}

fun View.qcToast(message: String?) = context.qcToast(message)
fun OverScroller.fling(
    startX: Float, startY: Float, velocityX: Float, velocityY: Float,
    minX: Float, maxX: Float, minY: Float, maxY: Float, overX: Float = 0f, overY: Float = overX
) {
    fling(
        startX.toInt(), startY.toInt(), velocityX.toInt(),
        velocityY.toInt(), minX.toInt(), maxX.toInt(), minY.toInt(), maxY.toInt(),
        overX.toInt(), overY.toInt()
    )
}

fun View.layout(rect: Rect){
    layout(rect.left, rect.top, rect.right, rect.bottom)
}

/**
 * 中国所有省份、自治区、直辖市
 */
val qcChinaProvince =
    "北京市,天津市,上海市,重庆市,河北省,山西省,辽宁省,吉林省,黑龙江省,江苏省,浙江省,安徽省,福建省,江西省,山东省,河南省,湖北省,湖南省,广东省,海南省,四川省,贵州省,云南省,陕西省,甘肃省,青海省,台湾省,内蒙古自治区,广西壮族自治区,西藏自治区,宁夏回族自治区,新疆维吾尔自治区,香港特别行政区,澳门特别行政区"
        .split(Regex(","))