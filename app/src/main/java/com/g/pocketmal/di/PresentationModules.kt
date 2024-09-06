package com.g.pocketmal.di

import com.g.pocketmal.ui.presenter.*
import com.g.pocketmal.ui.viewmodel.converter.*
import dagger.hilt.android.EntryPointAccessors
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val applicationModule = module {
    factory { params ->
        SplashPresenter(params[0], params[0], get(), get(), get(), get())
    }
    factory { params ->
        LoginPresenter(params[0], params[0], get(), get())
    }
    factory { params ->
        ListPresenter(params[0], params[0],
                get(), get(), get(), get(), get(), get(), get(), get(), get(), get())
    }
    factory { params ->
        RankedPresenter(params[0], params[1], params[2], params[2], get(), get(), get())
    }
    factory { params ->
        BrowsePresenter(params[0], params[1], params[2], params[2], get(), get(), get())
    }
    factory { params ->
        SeasonalPresenter(params[0], params[0], get(), get(), get())
    }
    factory { params ->
        UserProfilePresenter(params[0], params[1], params[1], get(), get(), get())
    }
    factory { params ->
        TitleDetailsPresenter(params[0], params[1], params[2], params[2], get(), get(),
                get(), get(), get(), get(), get(), get(), get(), get(), get(), EntryPointAccessors.fromApplication(androidContext(), DataModule.DataStoreEntryPoint::class.java))
    }
    factory { params ->
        SearchPresenter(params[0], params[1], params[1], get(), get(), get())
    }
    factory { params ->
        EditDetailsPresenter(params[0], params[1], params[2], params[2], get(), get())
    }
}

val viewConverterModule = module {
    single { ListItemConverter(androidContext()) }
    single { ListRecordConverter(androidContext()) }
    single { RankedItemConverter(androidContext()) }
    single { BrowseItemConverter(androidContext()) }
    single { UserProfileConverter() }
    single { SeasonalAnimeConverter() }
    single { SeasonalSectionConverter(get()) }
    single { TitleDetailsConverter(androidContext()) }
    single { SearchResultConverter(androidContext()) }
}
