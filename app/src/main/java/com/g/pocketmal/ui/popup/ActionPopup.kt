package com.g.pocketmal.ui.popup

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.provider.CalendarContract
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.widget.ImageButton
import android.widget.Toast

import com.g.pocketmal.R
import com.g.pocketmal.data.common.Status
import com.g.pocketmal.data.keyvalue.SharingPatternDispatcher
import com.g.pocketmal.ui.viewmodel.RecordViewModel
import com.g.pocketmal.data.util.TitleType
import com.g.pocketmal.ui.SkeletonActivity
import com.g.pocketmal.util.Action

//FIXME unload of business logic
class ActionPopup(private val activity: SkeletonActivity) : SkeletonPopupWindows(activity, true) {

    private lateinit var rootView: View

    private var discussionUrl: String? = null
    private var testToShare: String? = null

    private var reminderTitle: String? = null
    private var reminderDescription: String? = null

    private lateinit var remindAction: ImageButton

    var id: Long = 0

    private var title: RecordViewModel? = null
    private var action: Action = Action.ACTION_NONE

    init {
        setRootViewId()
    }

    @SuppressLint("InflateParams")
    private fun setRootViewId() {

        rootView = inflater.inflate(R.layout.popup_share, null)
        rootView.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        contentView = rootView

        remindAction = rootView.findViewById(R.id.fab_set_reminder)
        remindAction.setOnClickListener {

            val intent = Intent(Intent.ACTION_INSERT)
                    .setData(CalendarContract.Events.CONTENT_URI)
                    .putExtra(CalendarContract.Events.TITLE, reminderTitle)
                    .putExtra(CalendarContract.Events.DESCRIPTION, reminderDescription)

            try {
                context.startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(context, R.string.noCalendar, Toast.LENGTH_LONG).show()
            }
        }

        rootView.findViewById<View>(R.id.fab_share).setOnClickListener {
            activity.shareText(testToShare!!)
        }
    }

    fun show(title: RecordViewModel, action: Action) {

        this.title = title
        this.action = action

        if (shouldShowReminderIcon(title.myStatus, action)) {
            remindAction.visibility = View.VISIBLE
            reminderTitle = context.getString(R.string.reminder__title, title.seriesTitle)
            reminderDescription = getReminderDescription(title)
        } else {
            remindAction.visibility = View.GONE
        }
        discussionUrl = title.discussionLink
        testToShare = SharingPatternDispatcher(context).getPattern(title, action)

        showPopup()
    }

    private fun showPopup() {

        prepare()

        rootView.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)

        val xPosition = displayMetrics.widthPixels - rootView.measuredWidth
        val yPosition = displayMetrics.heightPixels - rootView.measuredHeight -
                context.resources.getDimension(R.dimen.action_popup_offset).toInt()

        animationStyle = R.style.Animation_ActionPopupAnimation
        showAtLocation(rootView, Gravity.NO_GRAVITY, xPosition, yPosition)
    }

    private fun shouldShowReminderIcon(status: Status, actionType: Action): Boolean {
        return actionType == Action.ACTION_STATUS && (status == Status.ON_HOLD || status == Status.PLANNED)
    }

    private fun getReminderDescription(title: RecordViewModel): String {

        val isAnime = title.recordType === TitleType.ANIME

        return if (title.myStatus == Status.ON_HOLD)
            context.getString(if (isAnime)
                R.string.reminder__continue_watching
            else
                R.string.reminder__continue_reading,
                    title.seriesTitle, title.malLink)
        else
            context.getString(if (isAnime)
                R.string.reminder__plan_to_watch
            else
                R.string.reminder__plan_to_read,
                    title.seriesTitle, title.malLink)
    }
}
