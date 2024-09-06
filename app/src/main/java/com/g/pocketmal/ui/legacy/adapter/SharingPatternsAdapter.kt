package com.g.pocketmal.ui.legacy.adapter

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.g.pocketmal.R
import com.g.pocketmal.data.util.TitleType
import com.g.pocketmal.ui.legacy.fragment.SharingPatternsFragment
import com.g.pocketmal.ui.legacy.fragment.SharingPatternsFragment.Companion.newInstance
import com.g.pocketmal.ui.legacy.fragment.SharingPatternsHelpFragment

class SharingPatternsAdapter(activity: Activity, fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    private val list: Array<String> =
            arrayOf(activity.getString(R.string.anime), activity.getString(R.string.manga), activity.getString(R.string.help))
    private val animeFragment: SharingPatternsFragment = newInstance(TitleType.ANIME)
    private val mangaFragment: SharingPatternsFragment = newInstance(TitleType.MANGA)

    override fun getPageTitle(position: Int): CharSequence? {
        return list[position]
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> animeFragment
            1 -> mangaFragment
            else -> SharingPatternsHelpFragment()
        }
    }

    fun savePatterns() {
        animeFragment.savePatterns()
        mangaFragment.savePatterns()
    }
}