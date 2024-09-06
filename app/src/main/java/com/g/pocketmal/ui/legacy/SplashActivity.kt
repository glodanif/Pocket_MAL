package com.g.pocketmal.ui.legacy

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.g.pocketmal.R
import com.g.pocketmal.ui.legacy.presenter.SplashPresenter
import com.g.pocketmal.ui.legacy.route.SplashRoute
import com.g.pocketmal.ui.legacy.view.SplashView
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class SplashActivity : Activity(), SplashView,
    SplashRoute {

    private val presenter: SplashPresenter by inject { parametersOf(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        presenter.migrate()
        presenter.checkSession()
    }

    override fun redirectToListScreen() {
        val intent = Intent(this, ListActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
        }
        startActivity(intent)
        finish()
    }

    override fun redirectToLoginScreen() {
        val intent = Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
        }
        startActivity(intent)
        finish()
    }
}