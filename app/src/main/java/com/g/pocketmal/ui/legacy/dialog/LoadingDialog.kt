package com.g.pocketmal.ui.legacy.dialog

import android.app.Dialog
import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.StyleRes
import com.g.pocketmal.R

class LoadingDialog(context: Context, @StyleRes themeResId: Int, message: String) : Dialog(context, themeResId) {
    init {
        setContentView(R.layout.dialog_progress)
        window?.setLayout(context.resources.getDimensionPixelSize(R.dimen.min_dialog_width), ViewGroup.LayoutParams.WRAP_CONTENT)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        findViewById<TextView>(R.id.tv_dialog_text).text = message
        setCancelable(false)
    }
}