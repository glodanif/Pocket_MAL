package com.g.pocketmal.ui.presenter

import android.os.Handler
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.g.pocketmal.data.keyvalue.SessionManager
import com.g.pocketmal.domain.interactor.GetListsFromDbInteractor
import com.g.pocketmal.domain.interactor.MigrationInteractor
import com.g.pocketmal.ui.route.SplashRoute
import com.g.pocketmal.ui.view.SplashView
import com.g.pocketmal.util.list.ListsManager

class SplashPresenter(
        private val view: SplashView,
        private val route: SplashRoute,
        private val session: SessionManager,
        private val listsManager: ListsManager,
        private val migrationInteractor: MigrationInteractor,
        private val getListsFromDbInteractor: GetListsFromDbInteractor
) : LifecycleObserver {

    private val splashDelay = 500.toLong()

    fun migrate() {
        migrationInteractor.execute(Unit)
    }

    fun checkSession() {

        if (session.isUserLoggedIn) {
            initLists()
        } else {
            Handler().postDelayed({
                route.redirectToLoginScreen()
            }, splashDelay)
        }
    }

    private fun initLists() {

        getListsFromDbInteractor.execute(Unit,
                onResult = { lists ->
                    if (lists.animeList != null) {
                        listsManager.initAnimeLists(lists.animeList)
                    }
                    if (lists.mangaList != null) {
                        listsManager.initMangaLists(lists.mangaList)
                    }
                },
                onComplete = {
                    route.redirectToListScreen()
                }
        )
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop() {
        migrationInteractor.cancel()
        getListsFromDbInteractor.cancel()
    }
}
