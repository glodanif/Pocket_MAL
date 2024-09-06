package com.g.pocketmal.ui.legacy.dialog

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.StyleRes
import com.g.pocketmal.R

class MessageDialog(context: Context, @StyleRes themeResId: Int, message: String, onClick: ((Int) -> Unit)? = null) : Dialog(context, themeResId) {

    companion object {
        fun getOfflineDialog(context: Context, @StyleRes themeResId: Int): MessageDialog {
            return MessageDialog(context, themeResId, context.getString(R.string.notAvailableOffline))
        }
    }

    init {
        setContentView(R.layout.dialog_alert)
        window?.setLayout(context.resources.getDimensionPixelSize(R.dimen.min_dialog_width), ViewGroup.LayoutParams.WRAP_CONTENT)
        window?.setBackgroundDrawableResource(android.R.color.transparent)

        val dialogTitle = findViewById<TextView>(R.id.tv_dialog_text)
        dialogTitle.text = message
        findViewById<View>(R.id.tv_ok_button).setOnClickListener { v: View ->
            dismiss()
            onClick?.invoke(v.id)
        }
    }
}
