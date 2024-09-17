package com.g.pocketmal.di

import com.g.pocketmal.data.converter.ListRecordEntityConverter
import com.g.pocketmal.data.converter.RankingEntityConverter
import com.g.pocketmal.data.converter.SearchEntityConverter
import com.g.pocketmal.data.converter.SeasonEntityConverter
import com.g.pocketmal.domain.interactor.*
import org.koin.dsl.module

val interactorModule = module {
    factory { LoadListFromNetworkInteractor(get(), get(), get(), get(), get()) }
    factory { LogoutInteractor(get(), get(), get(), get(), get()) }
    factory { RemoveTitleFromListInteractor(get(), get()) }
    factory { AddTitleToListInteractor(get(), get(), get(), get()) }
    factory { GetTitleDetailsInteractor(get(), get(), get(), get()) }
    factory { UpdateTitleInteractor(get(), get()) }
    factory { GetListsFromDbInteractor(get()) }
    factory { GetRecordFromDbInteractor(get()) }
}

val entityConverterModule = module {
    single { SeasonEntityConverter() }
    single { ListRecordEntityConverter() }
    single { SearchEntityConverter() }
    single { RankingEntityConverter() }
}
