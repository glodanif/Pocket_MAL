package com.g.pocketmal.ui.legacy.dialog

import android.app.Dialog
import android.graphics.Paint
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.NumberPicker
import androidx.annotation.StyleRes
import com.g.pocketmal.R
import com.g.pocketmal.getThemeColor
import com.g.pocketmal.ui.legacy.SkeletonActivity

class ScoreDialog(context: SkeletonActivity, @StyleRes themeResId: Int, scoreListener: (Int) -> Unit, cancelListener: () -> Unit) : Dialog(context, themeResId) {

    init {
        setContentView(R.layout.dialog_rate)
        window?.setLayout(context.resources
                .getDimensionPixelSize(R.dimen.min_dialog_width), ViewGroup.LayoutParams.WRAP_CONTENT)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        setCancelable(false)
        val scores = arrayOf(
                context.getString(R.string.noScore),
                context.getString(R.string.score10),
                context.getString(R.string.score9),
                context.getString(R.string.score8),
                context.getString(R.string.score7),
                context.getString(R.string.score6),
                context.getString(R.string.score5),
                context.getString(R.string.score4),
                context.getString(R.string.score3),
                context.getString(R.string.score2),
                context.getString(R.string.score1)
        )
        val scorePicker = findViewById<NumberPicker>(R.id.np_score).apply {
            minValue = 0
            maxValue = 10
            displayedValues = scores
        }
        setNumberPickerTextColor(scorePicker, context.getThemeColor(android.R.attr.textColorPrimary))
        findViewById<View>(R.id.tv_ok_button).setOnClickListener {
            scoreListener.invoke(if (scorePicker.value == 0) 0 else scores.size - scorePicker.value)
            dismiss()
        }
        findViewById<View>(R.id.tv_cancel_button).setOnClickListener {
            cancelListener.invoke()
            dismiss()
        }
    }

    private fun setNumberPickerTextColor(numberPicker: NumberPicker, color: Int): Boolean {
        val count = numberPicker.childCount
        for (i in 0 until count) {
            val child = numberPicker.getChildAt(i)
            if (child is EditText) {
                try {
                    val selectorWheelPaintField = numberPicker.javaClass.getDeclaredField("mSelectorWheelPaint")
                    selectorWheelPaintField.isAccessible = true
                    (selectorWheelPaintField[numberPicker] as Paint).color = color
                    child.setTextColor(color)
                    numberPicker.invalidate()
                    return true
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        return false
    }
}