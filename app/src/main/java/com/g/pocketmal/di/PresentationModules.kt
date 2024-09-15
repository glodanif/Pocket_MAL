package com.g.pocketmal.di

import com.g.pocketmal.ui.legacy.presenter.BrowsePresenter
import com.g.pocketmal.ui.legacy.presenter.ListPresenter
import com.g.pocketmal.ui.legacy.presenter.RankedPresenter
import com.g.pocketmal.ui.legacy.presenter.TitleDetailsPresenter
import com.g.pocketmal.ui.legacy.viewentity.converter.BrowseItemConverter
import com.g.pocketmal.ui.legacy.viewentity.converter.ListItemConverter
import com.g.pocketmal.ui.legacy.viewentity.converter.ListRecordConverter
import com.g.pocketmal.ui.legacy.viewentity.converter.RankedItemConverter
import com.g.pocketmal.ui.legacy.viewentity.converter.TitleDetailsConverter
import com.g.pocketmal.ui.userprofile.presentation.UserProfileConverter
import com.g.pocketmal.ui.seasonal.presentation.SeasonalSectionConverter
import dagger.hilt.android.EntryPointAccessors
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val applicationModule = module {
    factory { params ->
        ListPresenter(
            params[0],
            params[0],
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }
    factory { params ->
        RankedPresenter(params[0], params[1], params[2], params[2], get(), get(), get())
    }
    factory { params ->
        BrowsePresenter(params[0], params[1], params[2], params[2], get(), get(), get())
    }
    factory { params ->
        TitleDetailsPresenter(
            params[0],
            params[1],
            params[2],
            params[2],
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            EntryPointAccessors.fromApplication(
                androidContext(),
                DataModule.DataStoreEntryPoint::class.java
            )
        )
    }
}

val viewConverterModule = module {
    single {
        EntryPointAccessors.fromApplication(
            androidContext(),
            DataModule.ListsManagerEntryPoint::class.java
        ).getListsManager()
    }

    single { ListItemConverter(androidContext()) }
    single { ListRecordConverter(androidContext()) }
    single { RankedItemConverter(androidContext()) }
    single { BrowseItemConverter(androidContext()) }
    single { TitleDetailsConverter(androidContext()) }
}
