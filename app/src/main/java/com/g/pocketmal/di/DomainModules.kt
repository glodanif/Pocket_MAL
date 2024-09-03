package com.g.pocketmal.di

import androidx.datastore.core.DataStore
import com.g.pocketmal.domain.entity.converter.*
import com.g.pocketmal.domain.interactor.*
import dagger.hilt.android.EntryPointAccessors
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val interactorModule = module {
    factory { LoadListFromNetworkInteractor(get(), get(), get(), get(), get()) }
    factory { LogoutInteractor(get(), get(), get(), get(), get()) }
    factory { GetSeasonalAnimeInteractor(get(), get(), get()) }
    factory { AuthorizationInteractor(get(), get(), get()) }
    factory { LoadLoginPageInteractor(get(), get(), get(), get()) }
    factory { GetUserProfileInteractor(get(), get(), get(), get()) }
    factory { RemoveTitleFromListInteractor(get(), get()) }
    factory { AddTitleToListInteractor(get(), get(), get(), get()) }
    factory { GetTitleDetailsInteractor(get(), get(), get(), get()) }
    factory { UpdateTitleInteractor(get(), get()) }
    factory { SearchInteractor(get(), get(), get()) }
    factory { GetRecommendationsInteractor(get(), get()) }
    factory { GetListsFromDbInteractor(get()) }
    factory { GetRecordFromDbInteractor(get()) }
    factory { GetTopInteractor(get(), get(), get()) }
    factory { MigrationInteractor(get(), get()) }
}

val entityConverterModule = module {
    single { SeasonEntityConverter() }
    single { LoginEntityConverter() }
    single { UserProfileEntityConverter() }
    single { ListRecordEntityConverter() }
    single { SearchEntityConverter() }
    single { RecommendationEntityConverter() }
    single { RankingEntityConverter() }
}
