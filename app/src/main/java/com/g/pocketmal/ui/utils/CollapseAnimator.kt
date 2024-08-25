package com.g.pocketmal.ui.utils

import android.app.Activity
import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.LinearLayout

object CollapseAnimator {

    fun expandView(activity: Activity, animatedView: View, onExpanded: (() -> Unit)? = null) {
        animatedView.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        val targetHeight = animatedView.measuredHeight
        animatedView.layoutParams.height = 0
        animatedView.visibility = View.VISIBLE
        val animation: Animation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                animatedView.layoutParams.height = if (interpolatedTime == 1f) {
                    onExpanded?.invoke()
                    LinearLayout.LayoutParams.WRAP_CONTENT
                } else {
                    (targetHeight * interpolatedTime).toInt()
                }
                animatedView.requestLayout()
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }
        animation.duration = (targetHeight / activity.resources.displayMetrics.density).toLong()
        animatedView.startAnimation(animation)
    }

    fun collapseView(activity: Activity, animatedView: View) {
        val initialHeight = animatedView.measuredHeight
        val animation: Animation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                if (interpolatedTime == 1f) {
                    animatedView.visibility = View.GONE
                } else {
                    animatedView.layoutParams.height = initialHeight - (initialHeight * interpolatedTime).toInt()
                    animatedView.requestLayout()
                }
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }
        animation.duration = (initialHeight / activity.resources.displayMetrics.density).toLong()
        animatedView.startAnimation(animation)
    }
}