package com.g.pocketmal.di

import com.g.pocketmal.data.converter.ListRecordEntityConverter
import com.g.pocketmal.data.converter.RankingEntityConverter
import com.g.pocketmal.data.converter.RecommendationEntityConverter
import com.g.pocketmal.data.converter.SearchEntityConverter
import com.g.pocketmal.data.converter.SeasonEntityConverter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataConverterModule {

    @Singleton
    @Provides
    fun providesRecommendationsConverter() = RecommendationEntityConverter()

    @Singleton
    @Provides
    fun providesSearchConverter() = SearchEntityConverter()

    @Singleton
    @Provides
    fun providesRecordConverter() = ListRecordEntityConverter()

    @Singleton
    @Provides
    fun providesSeasonalConverter() = SeasonEntityConverter()

    @Singleton
    @Provides
    fun providesRankingEntityConverter() = RankingEntityConverter()
}
