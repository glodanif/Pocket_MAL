package com.g.pocketmal.ui.legacy.popup

import android.annotation.SuppressLint
import android.graphics.Rect
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.TextView
import com.g.pocketmal.R
import com.g.pocketmal.data.util.TitleType
import com.g.pocketmal.data.common.Status
import com.g.pocketmal.ui.legacy.SkeletonActivity

class ListPopup<T>(context: SkeletonActivity, items: ArrayList<ListPopupItem<T>>) : SkeletonPopupWindows(context) {

    private lateinit var rootView: View
    private lateinit var popupHolder: ViewGroup
    private val padding: Int =
            context.resources.getDimensionPixelSize(R.dimen.list_popup_padding)
    private val frameWidth = context.resources.getDimensionPixelSize(R.dimen.list_popup_width)
    private var insertPosition = 0

    var clickListener: ((T) -> Unit)? = null

    @SuppressLint("InflateParams")
    private fun setupRootView() {
        rootView = inflater.inflate(R.layout.popup_list, null, false)
        popupHolder = rootView.findViewById(R.id.ll_list_popup_holder)
        rootView.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        contentView = rootView
    }

    @SuppressLint("InflateParams")
    fun addActionItem(action: ListPopupItem<T>) {
        val label = inflater.inflate(R.layout.item_list_action, null) as TextView
        label.text = action.title
        label.isFocusable = true
        label.isClickable = true
        label.setOnClickListener {
            dismiss()
            clickListener?.invoke(action.actionKey)
        }
        popupHolder.addView(label, insertPosition)
        insertPosition++
    }

    fun show(anchor: View) {
        prepare()
        val xPosition: Int
        val yPosition: Int
        val location = IntArray(2)
        anchor.getLocationOnScreen(location)
        val anchorRect = Rect(location[0], location[1], location[0]
                + anchor.width, location[1] + anchor.height)
        rootView.measure(frameWidth, LayoutParams.WRAP_CONTENT)
        xPosition = anchorRect.right - rootView.measuredWidth + padding
        yPosition = anchorRect.top - padding
        animationStyle = R.style.Animation_DropDownDown
        showAtLocation(anchor, Gravity.NO_GRAVITY, xPosition, yPosition)
    }

    class ListPopupItem<T>(var title: String, var actionKey: T)

    companion object {
        fun makeScorePopup(context: SkeletonActivity): ListPopup<Int> {
            val scoreItems = arrayListOf(
                    ListPopupItem(context.getString(R.string.noScore), 0),
                    ListPopupItem(context.getString(R.string.score10), 10),
                    ListPopupItem(context.getString(R.string.score9), 9),
                    ListPopupItem(context.getString(R.string.score8), 8),
                    ListPopupItem(context.getString(R.string.score7), 7),
                    ListPopupItem(context.getString(R.string.score6), 6),
                    ListPopupItem(context.getString(R.string.score5), 5),
                    ListPopupItem(context.getString(R.string.score4), 4),
                    ListPopupItem(context.getString(R.string.score3), 3),
                    ListPopupItem(context.getString(R.string.score2), 2),
                    ListPopupItem(context.getString(R.string.score1), 1)
            )
            return ListPopup(context, scoreItems)
        }

        fun makeStatusPopup(context: SkeletonActivity, type: TitleType): ListPopup<Status> {
            val animeMode = type === TitleType.ANIME
            val statusItems = arrayListOf(
                    if (animeMode)
                        ListPopupItem(context.getString(R.string.watching), Status.IN_PROGRESS) else ListPopupItem(context.getString(R.string.reading), Status.IN_PROGRESS),
                    ListPopupItem(context.getString(R.string.completed), Status.COMPLETED),
                    ListPopupItem(context.getString(R.string.onHold), Status.ON_HOLD),
                    ListPopupItem(context.getString(R.string.dropped), Status.DROPPED),
                    if (animeMode)
                        ListPopupItem(context.getString(R.string.planToWatch), Status.PLANNED) else ListPopupItem(context.getString(R.string.planToRead), Status.PLANNED)
            )
            return ListPopup(context, statusItems)
        }
    }

    init {
        setupRootView()
        for (item in items) {
            addActionItem(item)
        }
    }
}