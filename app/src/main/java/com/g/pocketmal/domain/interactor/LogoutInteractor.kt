package com.g.pocketmal.domain.interactor

import com.g.pocketmal.data.database.datasource.RecordDataSource
import com.g.pocketmal.data.keyvalue.LocalStorage
import com.g.pocketmal.data.keyvalue.SessionManager
import com.g.pocketmal.data.keyvalue.SharingPatternDispatcher
import com.g.pocketmal.util.list.ListsManager

class LogoutInteractor(
        private val recordStorage: RecordDataSource,
        private val listsManager: ListsManager,
        private val sharingPatterns: SharingPatternDispatcher,
        private val localStorage: LocalStorage,
        private val sessionManager: SessionManager
) : BaseInteractor<Unit, Unit>() {

    override suspend fun execute(input: Unit) {
        recordStorage.dropTable()
        localStorage.reset()
        sessionManager.logout()
        listsManager.clearInstance()
        sharingPatterns.reset()
    }
}