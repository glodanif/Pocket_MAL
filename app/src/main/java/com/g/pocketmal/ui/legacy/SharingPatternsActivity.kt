package com.g.pocketmal.ui.legacy

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.viewpager.widget.ViewPager
import com.g.pocketmal.R
import com.g.pocketmal.bind
import com.g.pocketmal.ui.legacy.adapter.SharingPatternsAdapter
import com.google.android.material.tabs.TabLayout

class SharingPatternsActivity : SkeletonToolbarActivity() {

    private val tabLayout: TabLayout by bind(R.id.tl_sliding_tabs)
    private val patternsContainer: ViewPager by bind(R.id.vp_patterns)

    private lateinit var adapter: SharingPatternsAdapter

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sharing_patterns, SkeletonToolbarActivity.CHILD_ACTIVITY)

        adapter = SharingPatternsAdapter(this, supportFragmentManager)
        patternsContainer.adapter = adapter
        tabLayout.setupWithViewPager(patternsContainer)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.actions_sharing_pattern, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return if (item.itemId == R.id.action_save) {
            savePatterns()
            finish()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    private fun savePatterns() {
        Thread(Runnable { adapter.savePatterns() }).run()
    }

    companion object {

        fun startActivity(context: Context) {
            context.startActivity(Intent(context, SharingPatternsActivity::class.java))
        }
    }
}