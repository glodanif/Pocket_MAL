package com.g.pocketmal.di

import com.g.pocketmal.data.api.ApiService
import com.g.pocketmal.data.api.MalApiService
import com.g.pocketmal.data.api.request.OAuthConfig
import com.g.pocketmal.data.database.ListDbStorage
import com.g.pocketmal.data.database.converter.ListRecordDataConverter
import com.g.pocketmal.data.database.converter.TitleDetailsDataConverter
import com.g.pocketmal.data.database.converter.UserProfileDataConverter
import com.g.pocketmal.data.database.datasource.*
import com.g.pocketmal.data.keyvalue.LocalStorage
import com.g.pocketmal.data.keyvalue.MainSettings
import com.g.pocketmal.data.keyvalue.SessionManager
import com.g.pocketmal.data.platform.*
import com.g.pocketmal.data.keyvalue.SharingPatternDispatcher
import com.g.pocketmal.util.list.ListsManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val apiModule = module {
    single { MalApiService(get(), get()) as ApiService }
    single { OAuthConfig() }
}

val storageModule = module {
    single { ListDbStorage(androidContext()) }
    single { RecordDataSourceImpl(get()) as RecordDataSource }
    single { TitleDetailsDataSourceImpl(get()) as TitleDetailsDataSource }
    single { UserProfileDataSourceImpl(get()) as UserProfileDataSource }
    single { SessionManager(androidContext()) }
    single { LocalStorage(androidContext()) }
    single { ListsManager() }
    single { MainSettings(androidContext()) }
    single { SharingPatternDispatcher(androidContext()) }
}

val dataConverterModule = module {
    single { ListRecordDataConverter() }
    single { UserProfileDataConverter() }
    single { TitleDetailsDataConverter() }
}

val platformModule = module {
    single { ClipboardManagerImpl(androidContext()) as ClipboardManager }
    single { CookieManagerImpl() as CookieManager }
    single { NetworkManagerImpl(androidContext()) as NetworkManager }
}
