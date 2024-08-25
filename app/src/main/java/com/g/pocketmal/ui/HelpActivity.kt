package com.g.pocketmal.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.TextView
import com.g.pocketmal.R
import com.g.pocketmal.bind

class HelpActivity : SkeletonToolbarActivity() {

    private val helpBody1: TextView by bind(R.id.tv_help_body_1)
    private val helpBody2: TextView by bind(R.id.tv_help_body_2)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help, CHILD_ACTIVITY)

        helpBody1.text = Html.fromHtml(getString(R.string.help__body_1))
        helpBody2.text = Html.fromHtml(getString(R.string.help__body_2))

        findViewById<View>(R.id.tv_recover_username).setOnClickListener {
            openLink("https://myanimelist.net/password.php?username=1&from=%2F")
        }

        findViewById<View>(R.id.tv_pocket_mal_support).setOnClickListener {
            startActivity(Intent(this, SupportActivity::class.java))
        }

        findViewById<View>(R.id.tv_recover_password).setOnClickListener {
            openLink("https://myanimelist.net/password.php?from=%2F")
        }
    }

    companion object {

        fun start(context: Context) {
            context.startActivity(Intent(context, HelpActivity::class.java))
        }
    }
}
