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
import com.g.pocketmal.data.converter.RankingEntityConverter
import com.g.pocketmal.data.keyvalue.UserSettings
import com.g.pocketmal.data.keyvalue.SessionStorage
import com.g.pocketmal.data.keyvalue.UserPreferences
import com.g.pocketmal.data.keyvalue.UserPreferencesSerializer
import com.g.pocketmal.data.repository.RecommendationsRepository
import com.g.pocketmal.data.repository.SearchRepository
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
import com.g.pocketmal.data.keyvalue.LocalStorage
import com.g.pocketmal.data.keyvalue.SharingPatternDispatcher
import com.g.pocketmal.data.platform.CookieManager
import com.g.pocketmal.data.platform.CookieManagerImpl
import com.g.pocketmal.data.platform.NetworkManager
import com.g.pocketmal.data.platform.NetworkManagerImpl
import com.g.pocketmal.data.repository.BrowseRepository
import com.g.pocketmal.data.repository.ListRepository
import com.g.pocketmal.data.repository.RecordRepository
import com.g.pocketmal.data.repository.SeasonalRepository
import com.g.pocketmal.data.repository.SessionRepository
import com.g.pocketmal.data.repository.UserProfileRepository
import com.g.pocketmal.util.list.ListsManager
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
    fun providesMainSettings(@ApplicationContext appContext: Context): UserSettings {
        return UserSettings(appContext)
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

    //TODO: Remove this
    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface ListsManagerEntryPoint {
        fun getListsManager(): ListsManager
    }

    @Singleton
    @Provides
    fun providesListManager(): ListsManager {
        return ListsManager()
    }

    @Singleton
    @Provides
    fun providesCookieManager(): CookieManager {
        return CookieManagerImpl()
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
    fun providesNetworkManager(
        @ApplicationContext appContext: Context,
    ): NetworkManager {
        return NetworkManagerImpl(appContext)
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
        return RecommendationsRepository(apiService, converter)
    }

    @Singleton
    @Provides
    fun providesSearchRepository(
        apiService: ApiService,
        settings: UserSettings,
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
        mainSettings: UserSettings,
    ): SeasonalRepository {
        return SeasonalRepository(apiService, converter, mainSettings)
    }

    @Singleton
    @Provides
    fun providesListRepository(
        apiService: ApiService,
        converter: ListRecordDataConverter,
        localStorage: LocalStorage,
        mainSettings: UserSettings,
        recordStorage: RecordDataSource,
        listsManager: ListsManager,
    ): ListRepository {
        return ListRepository(apiService, converter, localStorage, mainSettings, recordStorage)
    }

    @Singleton
    @Provides
    fun providesSessionRepository(
        storage: SessionStorage,
        recordRepository: RecordRepository,
        cookieManager: CookieManager,
        apiService: ApiService,
        listsManager: ListsManager,
        sharingPatternDispatcher: SharingPatternDispatcher,
        localStorage: LocalStorage,
        networkManager: NetworkManager,
    ): SessionRepository {
        return SessionRepository(
            sessionStorage = storage,
            recordRepository = recordRepository,
            cookieManager = cookieManager,
            apiService = apiService,
            listsManager = listsManager,
            sharingPatterns = sharingPatternDispatcher,
            localStorage = localStorage,
            networkManager = networkManager,
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
        return UserProfileRepository(
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
        userSettings: UserSettings,
    ): BrowseRepository {
        return BrowseRepository(apiService, converter, userSettings)
    }

    @Singleton
    @Provides
    fun providesUserProfileDataConverter() = UserProfileDataConverter()

    @Singleton
    @Provides
    fun providesListRecordDataConverter() = ListRecordDataConverter()
}
