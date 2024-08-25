package com.g.pocketmal.ui.popup

import android.animation.Animator
import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Handler
import android.view.Gravity
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup.LayoutParams
import androidx.cardview.widget.CardView
import com.g.pocketmal.R
import com.g.pocketmal.data.util.PartOfYear
import com.g.pocketmal.data.util.Season
import com.g.pocketmal.ui.SkeletonActivity
import com.g.pocketmal.ui.utils.StartEndAnimatorListener
import com.g.pocketmal.ui.widget.hashtagview.HashtagView
import com.g.pocketmal.util.AnimeSeason.getSeasonText
import com.g.pocketmal.util.AnimeSeason.parseSeasonName
import com.shawnlin.numberpicker.NumberPicker
import java.util.*

class SeasonsSelectorPopup(context: SkeletonActivity) : SkeletonPopupWindows(context) {

    private lateinit var rootView: View
    private lateinit var container: CardView
    private lateinit var yearPicker: NumberPicker
    private lateinit var seasonPicker: HashtagView

    private var year = 0
    private lateinit var partOfYearCode: PartOfYear

    private var isDismissing = false

    var listener: ((Int, PartOfYear) -> Unit)? = null

    init {
        setupRootView()
    }

    @SuppressLint("InflateParams")
    private fun setupRootView() {
        rootView = inflater.inflate(R.layout.popup_season_selector, null)
        rootView.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        contentView = rootView
        container = rootView.findViewById(R.id.cv_container)
        yearPicker = rootView.findViewById(R.id.np_year)
        yearPicker.maxValue = Calendar.getInstance()[Calendar.YEAR] + 1
        seasonPicker = rootView.findViewById(R.id.hv_season)
        val seasonsList = listOf("WINTER", "SPRING", "SUMMER", "FALL")
        seasonPicker.setData(seasonsList)
        seasonPicker.addOnTagSelectListener { item: Any, _: Boolean -> partOfYearCode = parseSeasonName(item.toString()) }
        rootView.findViewById<View>(R.id.btn_load).setOnClickListener {
            listener?.invoke(yearPicker.value, partOfYearCode)
            dismiss()
        }
    }

    fun setData(season: Season) {
        year = season.year
        partOfYearCode = season.partOfYear
    }

    fun show(anchor: View) {
        prepare()
        yearPicker.value = year
        val xPosition: Int
        val yPosition: Int
        val location = IntArray(2)
        anchor.getLocationOnScreen(location)
        val anchorRect = Rect(location[0], location[1], location[0]
                + anchor.width, location[1] + anchor.height)
        rootView.measure(context.resources.getDimensionPixelSize(R.dimen.list_popup_width), LayoutParams.WRAP_CONTENT)
        xPosition = anchorRect.right + rootView.measuredWidth
        yPosition = anchorRect.top
        isFocusable = true
        showAtLocation(anchor, Gravity.NO_GRAVITY, xPosition, yPosition)
        container.post {
            if (container.isAttachedToWindow) {
                val animator = ViewAnimationUtils.createCircularReveal(container,
                        container.width, 0, 0f, container.measuredWidth.toFloat())
                container.visibility = View.VISIBLE
                animator.addListener(object : StartEndAnimatorListener() {
                    override fun onAnimationStart(animation: Animator) {}
                    override fun onAnimationEnd(animation: Animator) {
                        Handler().post { seasonPicker.selectByValue(getSeasonText(partOfYearCode)) }
                    }
                })
                animator.duration = 200
                animator.start()
            }
        }
    }

    override fun dismiss() {
        if (!isDismissing) {
            val animator = ViewAnimationUtils.createCircularReveal(container,
                    container.width, 0, container.measuredWidth.toFloat(), 0f)
            container.visibility = View.VISIBLE
            animator.addListener(object : StartEndAnimatorListener() {
                override fun onAnimationStart(animation: Animator) {
                    isDismissing = true
                }

                override fun onAnimationEnd(animation: Animator) {
                    actualDismiss()
                }
            })
            animator.duration = 200
            animator.start()
        } else {
            actualDismiss()
        }
    }

    private fun actualDismiss() {
        isDismissing = false
        super.dismiss()
    }
}