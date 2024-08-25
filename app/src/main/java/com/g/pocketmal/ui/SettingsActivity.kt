package com.g.pocketmal.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import android.preference.PreferenceFragment
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.g.pocketmal.R
import com.g.pocketmal.bind
import com.g.pocketmal.ui.fragment.EntityListFragment
import com.g.pocketmal.data.keyvalue.MainSettings
import com.google.android.material.snackbar.Snackbar

class SettingsActivity : SkeletonToolbarActivity() {

    private val container: FrameLayout by bind(R.id.fl_settings_container)

    private lateinit var preferences: SharedPreferences

    private val listener = OnSharedPreferenceChangeListener { _, key ->
        when (key) {
            MainSettings.SIMPLE_VIEW_KEY, MainSettings.THEME_KEY ->
                Snackbar.make(container, R.string.settings__simple_view_notification, Snackbar.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings, CHILD_ACTIVITY)

        preferences = PreferenceManager.getDefaultSharedPreferences(this)

        if (savedInstanceState == null) {
            fragmentManager.beginTransaction()
                    .add(R.id.fl_settings_container, SettingsFragment())
                    .commitAllowingStateLoss()
        }
    }

    override fun onStop() {
        super.onStop()
        preferences.unregisterOnSharedPreferenceChangeListener(listener)
    }

    override fun onStart() {
        super.onStart()
        preferences.registerOnSharedPreferenceChangeListener(listener)
    }

    class SettingsFragment : PreferenceFragment() {

        override fun onCreate(paramBundle: Bundle?) {
            super.onCreate(paramBundle)
            addPreferencesFromResource(R.xml.preferences)
        }

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

            val view = super.onCreateView(inflater, container, savedInstanceState)

            findPreference("textPatterns").setOnPreferenceClickListener {
                SharingPatternsActivity.startActivity(activity)
                true
            }

            return view
        }
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, SettingsActivity::class.java))
        }
    }
}
