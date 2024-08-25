package com.g.pocketmal.ui.popup

import android.animation.Animator
import android.annotation.SuppressLint
import android.app.Activity
import androidx.annotation.Dimension
import android.view.Gravity
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup.LayoutParams
import android.widget.TextView

import com.g.pocketmal.R
import com.g.pocketmal.ui.SkeletonActivity
import com.g.pocketmal.ui.utils.StartEndAnimatorListener
import com.g.pocketmal.util.EpisodeType

class IncrementPopup(context: SkeletonActivity, private val currentType: EpisodeType) : SkeletonPopupWindows(context) {

    private lateinit var rootView: View
    private lateinit var incrementButton: TextView

    var incrementButtonClickListener: ((EpisodeType) -> Unit)? = null

    private var isDismissing: Boolean = false

    @Dimension
    private val popupWidth: Int =
            context.resources.getDimensionPixelSize(R.dimen.increment_popup_width)

    init {
        setRootViewId()
    }

    @SuppressLint("InflateParams")
    private fun setRootViewId() {
        rootView = inflater.inflate(R.layout.popup_increment, null)
        rootView.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)

        incrementButton = rootView.findViewById(R.id.tv_increment_button)

        when (currentType) {
            EpisodeType.EPISODE -> incrementButton.setText(R.string.plusOne)
            EpisodeType.CHAPTER -> incrementButton.setText(R.string.plusOneChapter)
            EpisodeType.VOLUME -> incrementButton.setText(R.string.plusOneVolume)
        }

        incrementButton.setOnClickListener {
            incrementButtonClickListener?.invoke(currentType)
            dismiss()
        }

        contentView = rootView
    }

    fun show(anchor: View) {

        prepare()

        val location = IntArray(2)
        anchor.getLocationOnScreen(location)
        rootView.measure(popupWidth, LayoutParams.WRAP_CONTENT)

        val xPosition = location[0] + anchor.width / 2 - rootView.measuredWidth / 2
        val yPosition = location[1] + anchor.height / 2 - rootView.measuredHeight / 2

        incrementButton.visibility = View.VISIBLE
        animationStyle = R.style.Animation_DropDownCenter

        showAtLocation(anchor, Gravity.NO_GRAVITY, xPosition, yPosition)

        incrementButton.post {
            val animator = ViewAnimationUtils.createCircularReveal(incrementButton,
                    incrementButton.width / 2, incrementButton.height / 2,
                    0f, incrementButton.measuredWidth.toFloat())
            incrementButton.visibility = View.VISIBLE
            animator.duration = 200
            animator.start()
        }
    }

    override fun dismiss() {

        if (!isDismissing) {
            val animator = ViewAnimationUtils.createCircularReveal(incrementButton,
                    incrementButton.width / 2, incrementButton.height / 2,
                    incrementButton.measuredWidth.toFloat(), 0f)
            incrementButton.visibility = View.VISIBLE
            animator.addListener(object : StartEndAnimatorListener() {

                override fun onAnimationStart(animation: Animator) {
                    isDismissing = true
                }

                override fun onAnimationEnd(animation: Animator) {
                    super@IncrementPopup.dismiss()
                }
            })
            animator.duration = 200
            animator.start()
        } else {
            super.dismiss()
        }
    }
}