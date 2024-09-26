package com.g.pocketmal.di

import android.content.Context
import com.g.pocketmal.data.converter.UserProfileEntityConverter
import com.g.pocketmal.ui.common.inliststatus.InListStatusConverter
import com.g.pocketmal.ui.editdetails.presentation.RecordExtraDetailsConverter
import com.g.pocketmal.ui.list.presentation.ListRecordConverter
import com.g.pocketmal.ui.ranked.presentation.RankedItemConverter
import com.g.pocketmal.ui.recommendations.presentation.RecommendedTitleConverter
import com.g.pocketmal.ui.search.presentation.SearchResultConverter
import com.g.pocketmal.ui.seasonal.presentation.SeasonalAnimeConverter
import com.g.pocketmal.ui.seasonal.presentation.SeasonalSectionConverter
import com.g.pocketmal.ui.userprofile.presentation.UserProfileConverter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PresentationModule {

    @Singleton
    @Provides
    fun providesRecommendationsConverter(
        @ApplicationContext context: Context,
        converter: InListStatusConverter,
    ) = RecommendedTitleConverter(context, converter)

    @Singleton
    @Provides
    fun providesSearchConverter(
        @ApplicationContext context: Context,
        converter: InListStatusConverter,
    ) = SearchResultConverter(context, converter)

    @Singleton
    @Provides
    fun providesRecordExtraDetailsConverter() = RecordExtraDetailsConverter()

    @Singleton
    @Provides
    fun providesInListStatusConverter() = InListStatusConverter()

    @Singleton
    @Provides
    fun providesSeasonalAnimeConverter(
        @ApplicationContext context: Context,
        converter: InListStatusConverter,
    ) = SeasonalAnimeConverter(context, converter)

    @Singleton
    @Provides
    fun providesSeasonalSectionConverter(converter: SeasonalAnimeConverter) =
        SeasonalSectionConverter(converter)

    @Singleton
    @Provides
    fun providesUserProfileEntityConverter() = UserProfileEntityConverter()

    @Singleton
    @Provides
    fun providesUserProfileConverter() = UserProfileConverter()

    @Singleton
    @Provides
    fun providesListRecordConverter(
        @ApplicationContext context: Context,
    ) = ListRecordConverter(context)

    @Singleton
    @Provides
    fun providesRankedItemConverter(
        @ApplicationContext context: Context,
    ) = RankedItemConverter(context)
}
