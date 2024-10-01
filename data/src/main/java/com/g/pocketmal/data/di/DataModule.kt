package com.g.pocketmal.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import com.g.pocketmal.data.api.ApiService
import com.g.pocketmal.data.api.MalApiService
import com.g.pocketmal.data.api.request.OAuthConfig
import com.g.pocketmal.data.converter.ListRecordEntityConverter
import com.g.pocketmal.data.converter.RankingEntityConverter
import com.g.pocketmal.data.converter.RecommendationEntityConverter
import com.g.pocketmal.data.converter.SearchEntityConverter
import com.g.pocketmal.data.converter.SeasonEntityConverter
import com.g.pocketmal.data.converter.UserProfileEntityConverter
import com.g.pocketmal.data.database.ListDbStorage
import com.g.pocketmal.data.database.converter.ListRecordDataConverter
import com.g.pocketmal.data.database.converter.UserProfileDataConverter
import com.g.pocketmal.data.database.datasource.RecordDataSource
import com.g.pocketmal.data.database.datasource.RecordDataSourceImpl
import com.g.pocketmal.data.database.datasource.UserProfileDataSource
import com.g.pocketmal.data.database.datasource.UserProfileDataSourceImpl
import com.g.pocketmal.data.keyvalue.DataUserPreferences
import com.g.pocketmal.data.keyvalue.LocalStorage
import com.g.pocketmal.data.keyvalue.SessionStorage
import com.g.pocketmal.data.keyvalue.SharingPatternDispatcher
import com.g.pocketmal.data.keyvalue.UserPreferencesSerializer
import com.g.pocketmal.data.keyvalue.UserSettingsStorage
import com.g.pocketmal.data.repository.BrowseRepositoryImplementation
import com.g.pocketmal.data.repository.ListRepositoryImplementation
import com.g.pocketmal.data.repository.RecommendationsRepositoryImplementation
import com.g.pocketmal.data.repository.RecordRepositoryImplementation
import com.g.pocketmal.data.repository.SearchRepositoryImplementation
import com.g.pocketmal.data.repository.SeasonalRepositoryImplementation
import com.g.pocketmal.data.repository.SessionRepositoryImplementation
import com.g.pocketmal.data.repository.UserProfileRepositoryImplementation
import com.g.pocketmal.data.repository.UserSettingsRepositoryImplementation
import com.g.pocketmal.domain.repository.BrowseRepository
import com.g.pocketmal.domain.repository.ListRepository
import com.g.pocketmal.domain.repository.RecommendationsRepository
import com.g.pocketmal.domain.repository.RecordRepository
import com.g.pocketmal.domain.repository.SearchRepository
import com.g.pocketmal.domain.repository.SeasonalRepository
import com.g.pocketmal.domain.repository.SessionRepository
import com.g.pocketmal.domain.repository.UserProfileRepository
import com.g.pocketmal.domain.repository.UserSettingsRepository
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
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
    ): DataStore<DataUserPreferences> {
        return DataStoreFactory.create(
            serializer = serializer,
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { DataUserPreferences.defaultPreferences }
            ),
            migrations = emptyList(),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { appContext.dataStoreFile("user_preferences") }
        )
    }

    @Singleton
    @Provides
    fun providesMainSettings(@ApplicationContext appContext: Context): UserSettingsStorage {
        return UserSettingsStorage(appContext)
    }

    @Singleton
    @Provides
    fun providesOAuthConfig(): OAuthConfig {
        return OAuthConfig()
    }

    @Singleton
    @Provides
    fun providesSharingPatternDispatcher(
        @ApplicationContext appContext: Context,
    ): SharingPatternDispatcher {
        return SharingPatternDispatcher(appContext)
    }

    @Singleton
    @Provides
    fun providesLocalStorage(
        @ApplicationContext appContext: Context,
    ): LocalStorage {
        return LocalStorage(appContext)
    }

    @Singleton
    @Provides
    fun providesSessionStorage(
        @ApplicationContext appContext: Context,
    ): SessionStorage {
        return SessionStorage(appContext)
    }

    @Singleton
    @Provides
    fun providesApiService(
        sessionStorage: SessionStorage,
        oAuthConfig: OAuthConfig,
    ): ApiService {
        return MalApiService(sessionStorage, oAuthConfig)
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
    fun providesUserProfileDataSource(listDbStorage: ListDbStorage): UserProfileDataSource {
        return UserProfileDataSourceImpl(listDbStorage)
    }

    @Singleton
    @Provides
    fun providesRecommendationsRepository(
        apiService: ApiService,
        converter: RecommendationEntityConverter,
    ): RecommendationsRepository {
        return RecommendationsRepositoryImplementation(apiService, converter)
    }

    @Singleton
    @Provides
    fun providesSearchRepository(
        apiService: ApiService,
        settings: UserSettingsStorage,
        converter: SearchEntityConverter,
    ): SearchRepository {
        return SearchRepositoryImplementation(apiService, settings, converter)
    }

    @Singleton
    @Provides
    fun providesRecordRepository(
        recordStorage: RecordDataSource,
        converter: ListRecordEntityConverter,
    ): RecordRepository {
        return RecordRepositoryImplementation(recordStorage, converter)
    }

    @Singleton
    @Provides
    fun providesSeasonalRepository(
        apiService: ApiService,
        converter: SeasonEntityConverter,
        mainSettings: UserSettingsStorage,
    ): SeasonalRepository {
        return SeasonalRepositoryImplementation(
            apiService,
            converter,
            mainSettings
        )
    }

    @Singleton
    @Provides
    fun providesListRepository(
        apiService: ApiService,
        converter: ListRecordDataConverter,
        localStorage: LocalStorage,
        mainSettings: UserSettingsStorage,
        recordStorage: RecordDataSource,
    ): ListRepository {
        return ListRepositoryImplementation(
            apiService,
            converter,
            localStorage,
            mainSettings,
            recordStorage
        )
    }

    @Singleton
    @Provides
    fun providesSessionRepository(
        storage: SessionStorage,
        recordRepository: RecordRepository,
        apiService: ApiService,
        sharingPatternDispatcher: SharingPatternDispatcher,
        localStorage: LocalStorage,
    ): SessionRepository {
        return SessionRepositoryImplementation(
            sessionStorage = storage,
            recordRepository = recordRepository,
            apiService = apiService,
            sharingPatterns = sharingPatternDispatcher,
            localStorage = localStorage,
        )
    }

    @Singleton
    @Provides
    fun providesUserProfileRepository(
        apiService: ApiService,
        storage: UserProfileDataSource,
        dataConverter: UserProfileDataConverter,
        converter: UserProfileEntityConverter,
        sessionRepository: SessionRepository,
    ): UserProfileRepository {
        return UserProfileRepositoryImplementation(
            apiService,
            storage,
            dataConverter,
            converter,
            sessionRepository,
        )
    }

    @Singleton
    @Provides
    fun providesBrowseRepository(
        apiService: ApiService,
        converter: RankingEntityConverter,
        userSettings: UserSettingsStorage,
    ): BrowseRepository {
        return BrowseRepositoryImplementation(apiService, converter, userSettings)
    }

    @Singleton
    @Provides
    fun providesUserSettingsRepository(
        userSettings: UserSettingsStorage,
        userPreferencesStorage: DataStore<DataUserPreferences>,
    ): UserSettingsRepository {
        return UserSettingsRepositoryImplementation(userSettings, userPreferencesStorage)
    }

    @Singleton
    @Provides
    fun providesUserProfileDataConverter() = UserProfileDataConverter()

    @Singleton
    @Provides
    fun providesListRecordDataConverter() = ListRecordDataConverter()
}
