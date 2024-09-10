package com.g.pocketmal.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import com.g.pocketmal.data.api.ApiService
import com.g.pocketmal.data.api.MalApiService
import com.g.pocketmal.data.api.request.OAuthConfig
import com.g.pocketmal.data.converter.ListRecordEntityConverter
import com.g.pocketmal.data.keyvalue.MainSettings
import com.g.pocketmal.data.keyvalue.SessionManager
import com.g.pocketmal.data.keyvalue.UserPreferences
import com.g.pocketmal.data.keyvalue.UserPreferencesSerializer
import com.g.pocketmal.data.repository.RecommendationsRepository
import com.g.pocketmal.data.repository.SearchRepository
import com.g.pocketmal.data.converter.RecommendationEntityConverter
import com.g.pocketmal.data.converter.SearchEntityConverter
import com.g.pocketmal.data.converter.SeasonEntityConverter
import com.g.pocketmal.data.database.ListDbStorage
import com.g.pocketmal.data.database.datasource.RecordDataSource
import com.g.pocketmal.data.database.datasource.RecordDataSourceImpl
import com.g.pocketmal.data.repository.RecordRepository
import com.g.pocketmal.data.repository.SeasonalRepository
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


    @Singleton
    @Provides
    fun providesOAuthConfig(): OAuthConfig {
        return OAuthConfig()
    }

    @Singleton
    @Provides
    fun providesSessionManager(
        @ApplicationContext appContext: Context,
    ): SessionManager {
        return SessionManager(appContext)
    }

    @Singleton
    @Provides
    fun providesApiService(
        sessionManager: SessionManager,
        oAuthConfig: OAuthConfig,
    ): ApiService {
        return MalApiService(sessionManager, oAuthConfig)
    }

    @Singleton
    @Provides
    fun providesListDatabase(@ApplicationContext context: Context): ListDbStorage {
        return ListDbStorage(context)
    }

    @Singleton
    @Provides
    fun providesRecordDataSource(listDbStorage: ListDbStorage): RecordDataSource {
        return RecordDataSourceImpl(listDbStorage)
    }

    @Singleton
    @Provides
    fun providesRecommendationsRepository(
        apiService: ApiService,
        converter: RecommendationEntityConverter,
    ): RecommendationsRepository {
        return RecommendationsRepository(apiService, converter)
    }

    @Singleton
    @Provides
    fun providesSearchRepository(
        apiService: ApiService,
        settings: MainSettings,
        converter: SearchEntityConverter,
    ): SearchRepository {
        return SearchRepository(apiService, settings, converter)
    }

    @Singleton
    @Provides
    fun providesRecordRepository(
        recordStorage: RecordDataSource,
        converter: ListRecordEntityConverter,
    ): RecordRepository {
        return RecordRepository(recordStorage, converter)
    }

    @Singleton
    @Provides
    fun providesSeasonalRepository(
        apiService: ApiService,
        converter: SeasonEntityConverter,
        mainSettings: MainSettings,
    ): SeasonalRepository {
        return SeasonalRepository(apiService, converter, mainSettings)
    }
}
