package com.g.pocketmal.ui.dialog

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.StyleRes

import com.g.pocketmal.R

class YesNoDialog(context: Context, @StyleRes themeResId: Int, message: String, yesClick: () -> Unit, noClick: (() -> Unit)? = null) : Dialog(context, themeResId) {

    init {

        setContentView(R.layout.dialog_yes_no)
        window?.setLayout(context.resources.getDimensionPixelSize(R.dimen.min_dialog_width), ViewGroup.LayoutParams.WRAP_CONTENT)
        window?.setBackgroundDrawableResource(android.R.color.transparent)

        findViewById<TextView>(R.id.tv_dialog_text).text = message

        findViewById<View>(R.id.tv_yes_button).setOnClickListener {
            yesClick.invoke()
            dismiss()
        }

        findViewById<View>(R.id.tv_no_button).setOnClickListener {
            noClick?.invoke()
            dismiss()
        }
    }
}
