package com.g.pocketmal.domain.interactor

import com.g.pocketmal.data.database.ListDbStorage
import com.g.pocketmal.data.keyvalue.MainSettings

class MigrationInteractor(
        private val settings: MainSettings,
        private val listDbStorage: ListDbStorage
) : BaseInteractor<Unit, Unit>() {

    override suspend fun execute(input: Unit) {
        settings.migrate()
        listDbStorage.migrateLegacy()
    }
}
