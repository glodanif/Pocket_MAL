package com.g.pocketmal.di

import com.g.pocketmal.data.api.ApiService
import com.g.pocketmal.data.api.MalApiService
import com.g.pocketmal.data.api.request.OAuthConfig
import com.g.pocketmal.data.database.ListDbStorage
import com.g.pocketmal.data.database.converter.ListRecordDataConverter
import com.g.pocketmal.data.database.converter.TitleDetailsDataConverter
import com.g.pocketmal.data.database.converter.UserProfileDataConverter
import com.g.pocketmal.data.database.datasource.RecordDataSource
import com.g.pocketmal.data.database.datasource.RecordDataSourceImpl
import com.g.pocketmal.data.database.datasource.TitleDetailsDataSource
import com.g.pocketmal.data.database.datasource.TitleDetailsDataSourceImpl
import com.g.pocketmal.data.database.datasource.UserProfileDataSource
import com.g.pocketmal.data.database.datasource.UserProfileDataSourceImpl
import com.g.pocketmal.data.keyvalue.LocalStorage
import com.g.pocketmal.data.keyvalue.MainSettings
import com.g.pocketmal.data.keyvalue.SessionStorage
import com.g.pocketmal.data.keyvalue.SharingPatternDispatcher
import com.g.pocketmal.data.platform.ClipboardManager
import com.g.pocketmal.data.platform.ClipboardManagerImpl
import com.g.pocketmal.data.platform.CookieManager
import com.g.pocketmal.data.platform.CookieManagerImpl
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
    single { UserProfileDataSourceImpl(get()) as UserProfileDataSource }
    single { SessionStorage(androidContext()) }
    single { LocalStorage(androidContext()) }
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
