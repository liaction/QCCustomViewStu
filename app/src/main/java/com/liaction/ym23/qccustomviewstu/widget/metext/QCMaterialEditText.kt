package com.liaction.ym23.qccustomviewstu.widget.metext

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.liaction.ym23.qccustomviewstu.R
import com.liaction.ym23.qccustomviewstu.util.qcLog

@SuppressLint("CustomViewStyleable")
class QCMaterialEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {
    private val qcMetAttrMap = mutableMapOf<Int, String>()
    private val qcMetAttrList = QCMetAttr.values().map { it.name }.dropLast(1)

    private enum class QCMetAttr {
        QC_helperText,
        QC_useFloatLabel,
        QC_floatLabelText
    }

    var useFloatLabel = false
    var floatLabelText = ""
    var helperText = ""

    private fun obtainString(attrIndex: Int, default: String = ""): String {
        return qcMetAttrMap[attrIndex] ?: default
    }

    private fun obtainBoolean(attrIndex: Int, default: Boolean): Boolean {
        val attrValue = qcMetAttrMap[attrIndex] ?: return default
        return attrValue.toBoolean()
    }

    init {
        attrs?.let {
            for (index in 0 until it.attributeCount) {
                val attributeName = it.getAttributeName(index)
                val attributeValue = it.getAttributeValue(index)
//                qcLog("name: $attributeName, value: $attributeValue")
                if (attributeName in qcMetAttrList) {
                    val attrIndex = QCMetAttr.valueOf(attributeName).ordinal
                    qcMetAttrMap[attrIndex] = attributeValue
                }
            }
        }
        useFloatLabel = obtainBoolean(QCMetAttr.QC_useFloatLabel.ordinal, false)
        floatLabelText = obtainString(QCMetAttr.QC_floatLabelText.ordinal, "floatLabelText23")
        helperText = obtainString(QCMetAttr.QC_helperText.ordinal, "helperText23")
        qcLog("useFloatLabel: $useFloatLabel")
        qcLog("floatLabelText: $floatLabelText")
        qcLog("helperText: $helperText")

        qcLog("-----------------------23----------------------")
        var typeArray = context.obtainStyledAttributes(attrs, R.styleable.QCMET)
        useFloatLabel = typeArray.getBoolean(R.styleable.QCMET_QC_useFloatLabel, false)
        floatLabelText = typeArray.getString(R.styleable.QCMET_QC_floatLabelText) ?: "floatLabelText23"
        helperText = typeArray.getString(R.styleable.QCMET_QC_helperText) ?: "helperText23"
        qcLog("useFloatLabel: $useFloatLabel")
        qcLog("floatLabelText: $floatLabelText")
        qcLog("helperText: $helperText")
        typeArray.recycle()

    }
}