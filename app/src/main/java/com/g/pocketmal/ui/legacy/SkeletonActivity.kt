package com.g.pocketmal.ui.legacy

import android.annotation.TargetApi
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.Lifecycle
import com.g.pocketmal.R
import com.g.pocketmal.data.keyvalue.UserSettings
import com.g.pocketmal.domain.ThemeMode
import com.g.pocketmal.ui.legacy.dialog.LoadingDialog
import com.g.pocketmal.ui.legacy.dialog.MessageDialog
import com.g.pocketmal.ui.legacy.popup.ActionPopup
import com.g.pocketmal.ui.legacy.route.BaseSessionRoute
import com.g.pocketmal.ui.legacy.view.BaseSessionView
import com.g.pocketmal.ui.legacy.viewentity.RecordViewModel
import com.g.pocketmal.ui.utils.customtabs.CustomTabActivityHelper
import com.g.pocketmal.util.Action
import org.koin.android.ext.android.inject
import java.util.Locale

open class SkeletonActivity : AppCompatActivity(), BaseSessionView, BaseSessionRoute {

    private val settings: UserSettings by inject()

    private lateinit var actionPopup: ActionPopup
    private lateinit var customTabActivityHelper: CustomTabActivityHelper

    private var progress: Dialog? = null

    private var isDestroyed = false

    protected open fun getDarkTheme() = R.style.Theme_Mal_Dark

    protected open fun getBlackTheme() = R.style.Theme_Mal_Black

    private fun isNetworkOnline() = try {
        val cm = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        cm.activeNetworkInfo?.isConnectedOrConnecting ?: false
    } catch (e: Exception) {
        false
    }

    @StyleRes
    protected fun getCurrentTheme(): Int {
        return when (settings.getThemeMode()) {
            ThemeMode.DARK -> {
                getDarkTheme()
            }

            ThemeMode.BLACK -> {
                getBlackTheme()
            }

            ThemeMode.LIGHT -> {
                R.style.Theme_Mal
            }

            //FIXME
            ThemeMode.SYSTEM -> {
                getDarkTheme()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()

        val theme = settings.getThemeMode()
        if (ThemeMode.DARK == theme) {
            setTheme(getDarkTheme())
        } else if (ThemeMode.BLACK == theme) {
            setTheme(getBlackTheme())
        }

        super.onCreate(savedInstanceState)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        actionPopup = ActionPopup(this)
        customTabActivityHelper = CustomTabActivityHelper()
    }

    public override fun onStart() {
        super.onStart()
        customTabActivityHelper.bindCustomTabsService(this)
    }

    public override fun onStop() {
        super.onStop()
        customTabActivityHelper.unbindCustomTabsService(this)
    }

    public override fun onDestroy() {

        isDestroyed = true

        hideProgressDialog()
        hideActionsPopup()

        super.onDestroy()
    }

    override fun onBackPressed() {
        isDestroyed = true
        super.onBackPressed()
    }

    override fun finish() {
        isDestroyed = true
        super.finish()
    }

    override fun attachBaseContext(newBase: Context?) {
        val context = if (newBase == null) newBase else updateBaseContextLocale(newBase)
        super.attachBaseContext(context)
    }

    private fun updateBaseContextLocale(context: Context): Context {
        Locale.setDefault(Locale.US)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResourcesLocale(context, Locale.US)
        }
        return updateResourcesLocaleLegacy(context, Locale.US)
    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun updateResourcesLocale(context: Context, locale: Locale): Context {
        val configuration = context.resources.configuration;
        configuration.setLocale(locale)
        return context.createConfigurationContext(configuration)
    }

    @SuppressWarnings("deprecation")
    private fun updateResourcesLocaleLegacy(context: Context, locale: Locale): Context {
        val resources = context.resources
        val configuration = resources.configuration
        configuration.locale = locale
        resources.updateConfiguration(configuration, resources.displayMetrics)
        return context
    }

    fun shareText(testToShare: String) {

        try {
            val shareLinkIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, testToShare)
                putExtra(Intent.EXTRA_TEXT, testToShare)
            }
            startActivity(
                Intent.createChooser(
                    shareLinkIntent,
                    getString(R.string.general__share_via)
                )
            )
        } catch (e: ActivityNotFoundException) {
            showToast(R.string.noShareApp)
        } catch (e: SecurityException) {
            showToast(R.string.noShareApp)
        }
    }

    fun openLinkInBrowser(link: String) {
        openLink(link, true)
    }

    fun openLink(link: String) {
        openLink(link, false)
    }

    private fun openLink(link: String, onlyExternal: Boolean) {

        try {
            if (settings.useExternalBrowser() || onlyExternal) {
                openLinkInExternalBrowser(link)
            } else {
                openLinkInChromeTab(link)
            }
        } catch (e: ActivityNotFoundException) {
            showToast(R.string.noWebBrowser)
        } catch (e: SecurityException) {
            showToast(R.string.noWebBrowser)
        }
    }

    private fun openLinkInExternalBrowser(link: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(link)
        startActivity(intent)
    }

    private fun openLinkInChromeTab(link: String) {

        val intentBuilder = CustomTabsIntent.Builder().apply {
            setToolbarColor(resources.getColor(R.color.main))
            setShowTitle(true)
        }

        CustomTabActivityHelper.openCustomTab(this, intentBuilder.build(), Uri.parse(link))
        { _, uri -> startActivity(Intent(Intent.ACTION_VIEW, uri)) }
    }

    fun showToast(stringRes: Int) {
        Toast.makeText(this, stringRes, Toast.LENGTH_LONG).show()
    }

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    fun showMessageDialog(stringRes: Int) {
        showMessageDialog(getString(stringRes))
    }

    private fun showMessageDialog(message: String) {
        if (!isFinishing && !isDestroyed()) {
            MessageDialog(this, getCurrentTheme(), message).show()
        }
    }

    fun hideKeyboard() {
        val inputManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        window.decorView.rootView?.let { focus ->
            inputManager.hideSoftInputFromWindow(focus.windowToken, 0)
        }
    }

    fun executeIfOnline(task: () -> Unit) {
        if (isNetworkOnline()) {
            task.invoke()
        } else {
            MessageDialog.getOfflineDialog(this, getCurrentTheme()).show()
        }
    }

    fun showActionsPopup(title: RecordViewModel, actionType: Action) {
        if (!isFinishing && !isDestroyed() && settings.shouldShowFloatingSharingButton(actionType, title)) {
            actionPopup.show(title, actionType)
        }
    }

    private fun hideActionsPopup() {
        if (!isFinishing && !isDestroyed() && actionPopup.isShowing) {
            actionPopup.dismiss()
        }
    }

    fun showProgressDialog(messageRes: Int) {
        showProgressDialog(getString(messageRes))
    }

    fun showProgressDialog(message: String) {
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
            progress = LoadingDialog(this, getCurrentTheme(), message)
            progress?.show()
        }
    }

    fun hideProgressDialog() {
        if (!isFinishing && progress != null && progress?.isShowing == true) {
            try {
                progress?.dismiss()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                progress = null
            }
        }
    }

    override fun isDestroyed(): Boolean {
        return isDestroyed
    }

    override fun notifyUserAboutForceLogout() {
        showToast(R.string.forceLogout)
    }

    override fun redirectToLoginScreen() {
        /*val intent = Intent(this, LoginActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        startActivity(intent)*/
    }
}