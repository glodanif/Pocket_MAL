package com.g.pocketmal.ui.legacy

import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import androidx.annotation.IntDef
import androidx.appcompat.widget.Toolbar
import com.g.pocketmal.R
import com.g.pocketmal.ui.legacy.view.BaseSessionView

open class SkeletonToolbarActivity : SkeletonActivity(),
    BaseSessionView {

    protected var toolbar: Toolbar? = null

    protected fun setContentView(layoutResId: Int, @ActivityType type: Int) {

        when (type) {
            CUSTOM_ACTIVITY -> super.setContentView(layoutResId)
            CHILD_ACTIVITY -> super.setContentView(R.layout.activity_toolbar_base)
        }

        toolbar = findViewById(R.id.tb_toolbar)

        requireNotNull(toolbar) { "Layout should contain Toolbar with \"toolbar\" ID" }

        setSupportActionBar(toolbar)

        val actionBar = supportActionBar

        val rootView = findViewById<ViewGroup>(R.id.ll_root)
        val inflater = LayoutInflater.from(this)

        if (type == CHILD_ACTIVITY) {
            inflater.inflate(layoutResId, rootView, true)
            actionBar?.setDisplayHomeAsUpEnabled(true)
        } else {
            actionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    fun setActionBarTitle(titleResourceId: Int) {
        setActionBarTitle(getString(titleResourceId))
    }

    fun setActionBarTitle(title: String) {
        supportActionBar?.title = title
    }

    fun setActionBarSubtitle(subTitle: String) {
        supportActionBar?.subtitle = subTitle
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return if (item.itemId == android.R.id.home) {
            hideKeyboard()
            finish()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    companion object {

        const val DRAWER_ACTIVITY = 0
        const val CHILD_ACTIVITY = 1
        const val CUSTOM_ACTIVITY = 2

        @IntDef(DRAWER_ACTIVITY, CHILD_ACTIVITY, CUSTOM_ACTIVITY)
        @Retention(AnnotationRetention.SOURCE)
        annotation class ActivityType
    }
}