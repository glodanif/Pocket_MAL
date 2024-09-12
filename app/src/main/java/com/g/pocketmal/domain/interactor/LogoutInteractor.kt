package com.g.pocketmal.domain.interactor

import com.g.pocketmal.data.database.datasource.RecordDataSource
import com.g.pocketmal.data.keyvalue.LocalStorage
import com.g.pocketmal.data.keyvalue.SessionStorage
import com.g.pocketmal.data.keyvalue.SharingPatternDispatcher
import com.g.pocketmal.util.list.ListsManager

class LogoutInteractor(
        private val recordStorage: RecordDataSource,
        private val listsManager: ListsManager,
        private val sharingPatterns: SharingPatternDispatcher,
        private val localStorage: LocalStorage,
        private val sessionStorage: SessionStorage
) : BaseInteractor<Unit, Unit>() {

    override suspend fun execute(input: Unit) {
        recordStorage.dropTable()
        localStorage.reset()
        sessionStorage.logout()
        listsManager.clearInstance()
        sharingPatterns.reset()
    }
}