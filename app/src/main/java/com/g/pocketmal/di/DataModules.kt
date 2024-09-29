package com.g.pocketmal.di

import com.g.pocketmal.data.api.ApiService
import com.g.pocketmal.data.api.MalApiService
import com.g.pocketmal.data.api.request.OAuthConfig
import com.g.pocketmal.data.database.ListDbStorage
import com.g.pocketmal.data.database.converter.TitleDetailsDataConverter
import com.g.pocketmal.data.database.datasource.RecordDataSource
import com.g.pocketmal.data.database.datasource.RecordDataSourceImpl
import com.g.pocketmal.data.database.datasource.TitleDetailsDataSource
import com.g.pocketmal.data.database.datasource.TitleDetailsDataSourceImpl
import com.g.pocketmal.data.keyvalue.LocalStorage
import com.g.pocketmal.data.keyvalue.SessionStorage
import com.g.pocketmal.data.keyvalue.SharingPatternDispatcher
import com.g.pocketmal.data.keyvalue.UserSettings
import com.g.pocketmal.data.platform.NetworkManager
import com.g.pocketmal.data.platform.NetworkManagerImpl
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
    single { SessionStorage(androidContext()) }
    single { LocalStorage(androidContext()) }
    single { UserSettings(androidContext()) }
    single { SharingPatternDispatcher(androidContext()) }
}

val dataConverterModule = module {
    single { TitleDetailsDataConverter() }
}

val platformModule = module {
    single { NetworkManagerImpl(androidContext()) as NetworkManager }
}
