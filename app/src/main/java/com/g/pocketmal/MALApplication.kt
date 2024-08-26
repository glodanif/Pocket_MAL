package com.g.pocketmal

import android.app.Application
import com.g.pocketmal.di.*
import dagger.hilt.android.HiltAndroidApp
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

@HiltAndroidApp
class MALApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MALApplication)
            modules(
                    listOf(
                            applicationModule,
                            viewConverterModule,
                            apiModule, storageModule, dataConverterModule, platformModule,
                            entityConverterModule, interactorModule
                    )
            )
        }
    }
}
