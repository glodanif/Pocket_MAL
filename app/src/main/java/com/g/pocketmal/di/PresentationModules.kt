package com.g.pocketmal.di

import com.g.pocketmal.ui.legacy.presenter.BrowsePresenter
import com.g.pocketmal.ui.editdetails.EditDetailsPresenter
import com.g.pocketmal.ui.legacy.presenter.ListPresenter
import com.g.pocketmal.ui.legacy.presenter.LoginPresenter
import com.g.pocketmal.ui.legacy.presenter.RankedPresenter
import com.g.pocketmal.ui.legacy.presenter.SeasonalPresenter
import com.g.pocketmal.ui.legacy.presenter.SplashPresenter
import com.g.pocketmal.ui.legacy.presenter.TitleDetailsPresenter
import com.g.pocketmal.ui.legacy.presenter.UserProfilePresenter
import com.g.pocketmal.ui.legacy.viewentity.converter.ListItemConverter
import com.g.pocketmal.ui.legacy.viewentity.converter.ListRecordConverter
import com.g.pocketmal.ui.legacy.viewentity.converter.RankedItemConverter
import com.g.pocketmal.ui.search.presentation.SearchResultConverter
import com.g.pocketmal.ui.legacy.viewentity.converter.SeasonalAnimeConverter
import com.g.pocketmal.ui.legacy.viewentity.converter.SeasonalSectionConverter
import com.g.pocketmal.ui.legacy.viewentity.converter.TitleDetailsConverter
import com.g.pocketmal.ui.legacy.viewentity.converter.UserProfileConverter
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
        EditDetailsPresenter(params[0], params[1], params[2], params[2], get(), get())
    }
}

val viewConverterModule = module {
    single { ListItemConverter(androidContext()) }
    single { ListRecordConverter(androidContext()) }
    single { RankedItemConverter(androidContext()) }
    single { com.g.pocketmal.ui.legacy.viewentity.converter.BrowseItemConverter(androidContext()) }
    single { UserProfileConverter() }
    single { SeasonalAnimeConverter() }
    single { SeasonalSectionConverter(get()) }
    single { TitleDetailsConverter(androidContext()) }
    single { SearchResultConverter(androidContext()) }
}
