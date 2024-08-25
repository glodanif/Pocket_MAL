package com.g.pocketmal.data.platform

import android.content.ClipData
import android.content.Context
typealias SystemClipboardManager = android.content.ClipboardManager

class ClipboardManagerImpl(private val context: Context) : ClipboardManager {

    override fun copyToClipboard(text: String) {
        val manager = context.getSystemService(Context.CLIPBOARD_SERVICE) as SystemClipboardManager
        manager.setPrimaryClip(ClipData.newPlainText(text, text))
    }
}
