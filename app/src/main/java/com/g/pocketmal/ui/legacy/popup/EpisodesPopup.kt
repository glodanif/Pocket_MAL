package com.g.pocketmal.ui.legacy.popup

import android.animation.Animator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.os.Handler
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup.LayoutParams
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import com.g.pocketmal.R
import com.g.pocketmal.domain.TitleType
import com.g.pocketmal.ui.legacy.SkeletonActivity
import com.g.pocketmal.ui.utils.StartEndAnimatorListener

class EpisodesPopup(context: SkeletonActivity, private val viewModel: com.g.pocketmal.ui.legacy.viewentity.RecordViewModel) : SkeletonPopupWindows(context) {

    private lateinit var rootView: View
    private val inputMethodManager: InputMethodManager?

    var submitClickListener: ((Int?, Int?) -> Unit)? = null

    private val padding: Int = context.resources.getDimensionPixelSize(R.dimen.episodes_popup_padding)

    private lateinit var errorLabel: TextView
    private lateinit var episodesField: EditText
    private lateinit var subEpisodesField: EditText
    private lateinit var container: View

    private var isDismissing: Boolean = false

    private val isSubEpisodesAvailable = viewModel.recordType == TitleType.MANGA

    init {
        this.inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        setupRootView()
    }

    @SuppressLint("InflateParams")
    private fun setupRootView() {

        rootView = inflater.inflate(R.layout.popup_episodes, null)
        rootView.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)

        val subEpisodesHolder = rootView.findViewById<LinearLayout>(R.id.ll_sub_episodes_holder)

        if (!isSubEpisodesAvailable) {
            subEpisodesHolder.visibility = View.GONE
        }

        contentView = rootView

        val seriesEpisodesLabel = rootView.findViewById<TextView>(R.id.tv_series_episodes)
        val seriesSubEpisodesLabel = rootView.findViewById<TextView>(R.id.tv_series_sub_episodes)
        val episodesLabel = rootView.findViewById<TextView>(R.id.tv_episodes_label)
        val subEpisodesLabel = rootView.findViewById<TextView>(R.id.tv_sub_episodes_label)

        errorLabel = rootView.findViewById(R.id.tv_error)

        episodesField = rootView.findViewById(R.id.et_episodes_field)
        subEpisodesField = rootView.findViewById(R.id.et_sub_episodes_field)
        container = rootView.findViewById(R.id.fl_container)

        val updateButton = rootView.findViewById<ImageButton>(R.id.ib_update)

        val myEpisodes = viewModel.myEpisodes.toString()
        val seriesEpisodes = if (viewModel.seriesEpisodes == 0)
            "—"
        else
            viewModel.seriesEpisodes.toString()
        episodesField.setText(myEpisodes)
        episodesField.setSelection(myEpisodes.length)

        seriesEpisodesLabel.text = context.getString(R.string.series_episodes_label, seriesEpisodes)
        episodesField.setOnKeyListener(object : View.OnKeyListener {

            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                if (event != null && event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    handleClick()
                    return true
                }
                return false
            }
        })
        episodesLabel.text = viewModel.episodesTypeLabel

        if (isSubEpisodesAvailable) {
            val mySubEpisodes = viewModel.mySubEpisodes.toString()
            val seriesSubEpisodes = if (viewModel.seriesSubEpisodes == 0)
                "—"
            else
                viewModel.seriesSubEpisodes.toString()
            subEpisodesField.setText(mySubEpisodes)
            subEpisodesField.setSelection(mySubEpisodes.length)

            seriesSubEpisodesLabel.text = context.getString(R.string.series_episodes_label, seriesSubEpisodes)
            subEpisodesField.setOnKeyListener(object : View.OnKeyListener {

                override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                    if (event != null && event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                        handleClick()
                        return true
                    }
                    return false
                }
            })
            subEpisodesLabel.text = viewModel.subEpisodesTypeLabel
        }

        updateButton.setOnClickListener { handleClick() }
    }

    private fun handleClick() {

        errorLabel.visibility = View.GONE

        try {

            val episodesText = episodesField.text.toString()
            val enteredEpisodes = Integer.valueOf(episodesText)
            val subEpisodesText = subEpisodesField.text.toString()
            var enteredSubEpisodes: Int? = null
            if (isSubEpisodesAvailable) {
                enteredSubEpisodes = Integer.valueOf(subEpisodesText)
            }

            val areEpisodesValid = validateEpisodes(enteredEpisodes, viewModel.seriesEpisodes)
            val areSubEpisodesValid = validateEpisodes(enteredSubEpisodes, viewModel.seriesSubEpisodes)
            val isValid = if (!isSubEpisodesAvailable)
                areEpisodesValid
            else
                areEpisodesValid && areSubEpisodesValid

            submit(enteredEpisodes, enteredSubEpisodes, isValid)

        } catch (e: Exception) {
            e.printStackTrace()
            showInvalidQuantity()
        }
    }

    private fun submit(episodes: Int?, subEpisodes: Int?, areEpisodesValid: Boolean) {

        if (areEpisodesValid) {
            submitClickListener?.invoke(episodes,
                    if (isSubEpisodesAvailable) subEpisodes else null)
            dismiss()
        } else {
            showInvalidQuantity()
        }
    }

    private fun showInvalidQuantity() {
        errorLabel.visibility = View.VISIBLE
    }

    private fun validateEpisodes(enteredEpisodes: Int?, maxEpisode: Int?): Boolean {
        return (enteredEpisodes != null && maxEpisode != null
                && (maxEpisode == 0 || enteredEpisodes <= maxEpisode))
    }

    fun show(anchor: View) {

        episodesField.setText(viewModel.myEpisodes.toString())
        subEpisodesField.setText(viewModel.mySubEpisodes.toString())

        prepare()

        errorLabel.visibility = View.GONE

        val xPosition: Int
        val yPosition: Int

        val location = IntArray(2)
        anchor.getLocationOnScreen(location)

        val anchorRect = Rect(location[0], location[1], location[0] + anchor.width, location[1] + anchor.height)

        rootView.measure(context.resources.getDimensionPixelSize(R.dimen.list_popup_width), LayoutParams.WRAP_CONTENT)

        xPosition = anchorRect.right - rootView.measuredWidth + padding
        yPosition = anchorRect.top + anchor.height - rootView.measuredHeight + padding
        isFocusable = true

        showAtLocation(anchor, Gravity.NO_GRAVITY, xPosition, yPosition)

        container.post {

            if (container.isAttachedToWindow) {

                val animator = ViewAnimationUtils.createCircularReveal(container,
                        container.width, container.height, 0f, container.measuredWidth.toFloat())
                container.visibility = View.VISIBLE
                animator.addListener(object : StartEndAnimatorListener() {

                    override fun onAnimationStart(animation: Animator) {

                    }

                    override fun onAnimationEnd(animation: Animator) {

                        inputMethodManager?.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)
                        episodesField.requestFocus()
                    }
                })
                animator.duration = 200
                animator.start()
            } else {
                inputMethodManager?.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)
                episodesField.requestFocus()
            }
        }
    }

    override fun dismiss() {

        if (!isDismissing) {
            val animator = ViewAnimationUtils.createCircularReveal(container,
                    container.width, container.height, container.measuredWidth.toFloat(), 0f)
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

        Handler().post {
            context.hideKeyboard()
        }
    }

    private fun actualDismiss() {
        isDismissing = false
        super.dismiss()
    }
}
