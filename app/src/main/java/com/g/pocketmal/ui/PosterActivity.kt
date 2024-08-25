package com.g.pocketmal.ui

import android.Manifest
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.g.pocketmal.R
import com.g.pocketmal.bind
import com.g.pocketmal.loadUrl
import com.github.chrisbanes.photoview.PhotoView

class PosterActivity : SkeletonToolbarActivity() {

    private val poster: PhotoView by bind(R.id.pv_poster)

    private var posterUrl: String? = null

    private val downloadingReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            showToast(R.string.poster__downloading_completed)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_poster, CHILD_ACTIVITY)

        posterUrl = intent.getStringExtra(EXTRA_POSTER_URL)
        if (TextUtils.isEmpty(posterUrl)) {
            showToast(R.string.poster__empty_url)
            finish()
            return
        }

        poster.minimumScale = .75f
        poster.maximumScale = 2f

        poster.post { poster.scale = .8f }

        poster.loadUrl(posterUrl)
    }

    override fun onStart() {
        super.onStart()
        registerReceiver(downloadingReceiver,
                IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(downloadingReceiver)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.poster_activity_actions, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.action_share -> {
                shareText(posterUrl ?: "")
                true
            }
            R.id.action_download -> {
                checkWriteStoragePermission()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun checkWriteStoragePermission() {

        if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            downloadPoster()
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                explainAskingStoragePermission()
            } else {
                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_STORAGE_PERMISSION)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {

        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                downloadPoster()
            } else {
                explainAskingStoragePermission()
            }
        }
    }

    private fun explainAskingStoragePermission() {

        AlertDialog.Builder(this)
                .setMessage(R.string.poster__permission_explanation_storage)
                .setPositiveButton(R.string.ok
                ) { _, _ ->
                    ActivityCompat.requestPermissions(this@PosterActivity,
                            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                            REQUEST_STORAGE_PERMISSION
                    )
                }
                .show()
    }

    @RequiresPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private fun downloadPoster() {

        val isValidUrl = posterUrl?.startsWith("http", false) ?: false
        if (posterUrl.isNullOrEmpty() || !isValidUrl) {
            return
        }

        val manager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        val downloadsDirectory = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        if (!downloadsDirectory.exists()) {
            downloadsDirectory.mkdirs()
        }

        val posterUri = Uri.parse(posterUrl)
        manager.enqueue(DownloadManager.Request(posterUri)
                .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle(getString(R.string.poster__downloading_title))
                .setDescription(getString(R.string.poster__downloading_description))
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,
                        posterUri.lastPathSegment))
    }

    companion object {

        private const val EXTRA_POSTER_URL = "extra.poster_url"
        private const val REQUEST_STORAGE_PERMISSION = 101

        fun start(context: Context, url: String?) {
            val intent = Intent(context, PosterActivity::class.java)
                    .putExtra(EXTRA_POSTER_URL, url)
            context.startActivity(intent)
        }
    }
}
