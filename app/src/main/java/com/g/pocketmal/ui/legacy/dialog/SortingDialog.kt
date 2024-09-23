package com.g.pocketmal.ui.legacy.dialog

import android.app.Activity
import android.app.Dialog
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.RadioButton
import androidx.annotation.StyleRes

import com.g.pocketmal.R
import com.g.pocketmal.domain.SortingType

class SortingDialog(activity: Activity, @StyleRes themeResId: Int, sortingType: SortingType, reverse: Boolean, onSortType: (SortingType, Boolean) -> Unit) : Dialog(activity, themeResId) {

    init {

        setContentView(R.layout.dialog_sorting)
        window?.setLayout(activity.resources.getDimensionPixelSize(R.dimen.min_dialog_width), ViewGroup.LayoutParams.WRAP_CONTENT)
        window?.setBackgroundDrawableResource(android.R.color.transparent)

        val sortReverse = findViewById<View>(R.id.cb_reverse) as CheckBox

        val sortTitle = findViewById<View>(R.id.rb_title) as RadioButton
        val sortProgress = findViewById<View>(R.id.rb_progress) as RadioButton
        val sortScore = findViewById<View>(R.id.rb_score) as RadioButton
        val sortType = findViewById<View>(R.id.rb_type) as RadioButton
        val sortLastUpdated = findViewById<View>(R.id.rb_last_updated) as RadioButton

        sortReverse.isChecked = reverse

        when (sortingType) {
            SortingType.TITLE -> sortTitle.isChecked = true
            SortingType.PROGRESS -> sortProgress.isChecked = true
            SortingType.SCORE -> sortScore.isChecked = true
            SortingType.TYPE -> sortType.isChecked = true
            SortingType.LAST_UPDATED -> sortLastUpdated.isChecked = true
        }

        findViewById<View>(R.id.tv_sort_button).setOnClickListener {

            var type = SortingType.TITLE

            if (sortTitle.isChecked) {
                type = SortingType.TITLE
            }
            if (sortProgress.isChecked) {
                type = SortingType.PROGRESS
            }
            if (sortScore.isChecked) {
                type = SortingType.SCORE
            }
            if (sortType.isChecked) {
                type = SortingType.TYPE
            }
            if (sortLastUpdated.isChecked) {
                type = SortingType.LAST_UPDATED
            }

            onSortType.invoke(type, sortReverse.isChecked)
            dismiss()
        }

        findViewById<View>(R.id.tv_cancel_button).setOnClickListener { dismiss() }
    }
}
