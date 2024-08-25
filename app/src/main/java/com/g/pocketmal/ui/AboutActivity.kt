package com.g.pocketmal.ui

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.g.pocketmal.BuildConfig
import com.g.pocketmal.R
import com.g.pocketmal.bind
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity

class AboutActivity : SkeletonToolbarActivity() {

    private val applicationName: TextView by bind(R.id.tv_application_name)

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about, CHILD_ACTIVITY)

        applicationName.text = String.format("Pocket MAL\nv %s / %s",
                BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE)

        findViewById<View>(R.id.tv_open_source_libraries).setOnClickListener {
            startActivity(Intent(this, OssLicensesMenuActivity::class.java))
        }

        /*findViewById<View>(R.id.tv_website).setOnClickListener {
            openLinkInBrowser("https://glodanif.github.io/pocketmal/")
        }

        findViewById<View>(R.id.tv_mal_support).setOnClickListener {
            openLink("https://myanimelist.net/about.php?go=support")
        }

        findViewById<View>(R.id.tv_mal_faq).setOnClickListener {
            openLink("https://myanimelist.net/forum/index.php?topicid=515949")
        }*/
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.about_actions, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            R.id.action_share -> {
                shareText(APPLICATION_LINK)
                return true
            }
            R.id.action_rate -> {
                try {
                    val viewIntent = Intent("android.intent.action.VIEW",
                            Uri.parse("market://details?id=com.g.pocketmal"))
                    startActivity(viewIntent)
                } catch (e: ActivityNotFoundException) {
                    showToast(R.string.about__no_google_play)
                }

                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    companion object {

        const val APPLICATION_LINK = "https://play.google.com/store/apps/details?id=com.g.pocketmal"

        fun startActivity(context: Context) {
            context.startActivity(Intent(context, AboutActivity::class.java))
        }
    }
}