package com.g.pocketmal.di

import android.content.Context
import com.g.pocketmal.ui.editdetails.presentation.RecordExtraDetailsConverter
import com.g.pocketmal.ui.recommendations.presentation.RecommendedTitleConverter
import com.g.pocketmal.ui.search.presentation.SearchResultConverter
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
    fun providesRecommendationsConverter(@ApplicationContext context: Context) =
        RecommendedTitleConverter(context)

    @Singleton
    @Provides
    fun providesSearchConverter(@ApplicationContext context: Context) =
        SearchResultConverter(context)

    @Singleton
    @Provides
    fun providesRecordExtraDetailsConverter(@ApplicationContext context: Context) =
        RecordExtraDetailsConverter(context)
}
