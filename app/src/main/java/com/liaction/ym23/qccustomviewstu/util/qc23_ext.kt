package com.liaction.ym23.qccustomviewstu.util

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.util.TypedValue
import androidx.annotation.DrawableRes
import androidx.annotation.NonNull

val Number.dpx: Float
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,this.toFloat(), Resources.getSystem().displayMetrics)

val Number.spx: Float
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,this.toFloat(), Resources.getSystem().displayMetrics)


fun csLog(message:String?){
    Log.e("QC23","-----> 【$message】")
}
fun qcGetImageFromSource(width: Int, @NonNull resources: Resources, @DrawableRes drawableResId: Int) : Bitmap{
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    BitmapFactory.decodeResource(resources, drawableResId, options)
    options.inJustDecodeBounds = false
    options.inDensity = options.outWidth
    options.inTargetDensity = width
    return BitmapFactory.decodeResource(resources, drawableResId, options)
}