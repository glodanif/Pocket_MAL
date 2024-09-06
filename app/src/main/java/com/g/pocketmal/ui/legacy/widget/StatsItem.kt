package com.g.pocketmal.ui.legacy.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.g.pocketmal.R

class StatsItem : FrameLayout {

    private lateinit var data: Pair<String, String>

    constructor(context: Context?, data: Pair<String, String>) : super(context!!) {
        this.data = data
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context!!, attrs, defStyleAttr)

    private fun init() {
        val rootView = View.inflate(context, R.layout.include_stats_item, null)
        val label = rootView.findViewById<TextView>(R.id.tv_label)
        val value = rootView.findViewById<TextView>(R.id.tv_value)
        label.text = data.first
        value.text = data.second
        addView(rootView)
    }
}