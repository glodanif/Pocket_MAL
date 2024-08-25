package com.g.pocketmal.ui

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import com.g.pocketmal.BuildConfig
import com.g.pocketmal.R
import com.g.pocketmal.bind

class SupportActivity : SkeletonToolbarActivity() {

    private val body: EditText by bind(R.id.et_support)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_support, CHILD_ACTIVITY)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.support_actions, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.action_send -> {
                val text = body.text.toString().trim { it <= ' ' }
                if (!TextUtils.isEmpty(text) && text.length >= 15) {
                    hideKeyboard()
                    openEmailClient(text)
                } else {
                    Toast.makeText(this, R.string.support__hint, Toast.LENGTH_LONG).show()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun openEmailClient(text: String) {

        val intent = Intent(Intent.ACTION_SENDTO).apply {
            type = "text/plain"
            data = Uri.parse("mailto:pocketmal.app@gmail.com")
            putExtra(Intent.EXTRA_SUBJECT, "Pocket MAL (Support)")
            putExtra(Intent.EXTRA_TEXT, String.format("%s\n\nSent from Pocket MAL (v%s/%s)\nDevice: %s %s (%s)\nAndroid: %s (%s)",
                    text, BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE, Build.MANUFACTURER, Build.MODEL, Build.BRAND, Build.VERSION.RELEASE, Build.VERSION.SDK_INT))
        }

        try {
            startActivity(intent)
            finish()
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, R.string.about__no_email_client, Toast.LENGTH_LONG).show()
        } catch (e: SecurityException) {
            Toast.makeText(this, R.string.about__no_email_client, Toast.LENGTH_LONG).show()
        }
    }

    companion object {

        fun start(context: Context) {
            context.startActivity(Intent(context, SupportActivity::class.java))
        }
    }
}
