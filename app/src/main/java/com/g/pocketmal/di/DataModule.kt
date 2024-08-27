package com.g.pocketmal.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import com.g.pocketmal.data.keyvalue.MainSettings
import com.g.pocketmal.data.keyvalue.UserPreferences
import com.g.pocketmal.data.keyvalue.UserPreferencesSerializer
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Singleton
    @Provides
    fun providesGson(): Gson {
        return Gson()
    }

    @Singleton
    @Provides
    fun providesUserPreferencesSerializer(gson: Gson): UserPreferencesSerializer {
        return UserPreferencesSerializer(gson)
    }

    @Singleton
    @Provides
    fun providesUserPreferencesDataStore(
        @ApplicationContext appContext: Context,
        serializer: UserPreferencesSerializer,
    ): DataStore<UserPreferences> {
        return DataStoreFactory.create(
            serializer = serializer,
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { UserPreferences.defaultPreferences }
            ),
            migrations = emptyList(),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { appContext.dataStoreFile("user_preferences") }
        )
    }

    @Singleton
    @Provides
    fun providesMainSettings(@ApplicationContext appContext: Context): MainSettings {
        return MainSettings(appContext)
    }

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface DataStoreEntryPoint {
        fun getUserPreferencesDataStore(): DataStore<UserPreferences>
    }
}
