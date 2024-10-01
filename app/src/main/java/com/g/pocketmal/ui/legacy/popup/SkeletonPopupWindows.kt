package com.g.pocketmal.ui.legacy.popup

import android.app.Activity
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.WindowManager
import android.widget.PopupWindow

open class SkeletonPopupWindows @JvmOverloads constructor(protected var context: Activity, private val ignoreOutside: Boolean = false) : PopupWindow() {

    protected var windowManager= context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    protected var inflater = context.layoutInflater
    protected var displayMetrics = DisplayMetrics()

    init {

        setOnDismissListener {

        }
        setTouchInterceptor { _, event ->
            if (event.action == MotionEvent.ACTION_OUTSIDE) {
                dismiss()
                return@setTouchInterceptor true
            }
            false
        }

        this.windowManager.defaultDisplay.getMetrics(displayMetrics)

    }

    protected fun prepare() {
        setBackgroundDrawable(ColorDrawable())
        width = WindowManager.LayoutParams.WRAP_CONTENT
        height = WindowManager.LayoutParams.WRAP_CONTENT
        isTouchable = true
        isFocusable = !ignoreOutside
        isOutsideTouchable = true
    }
}