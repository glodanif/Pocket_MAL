package com.g.pocketmal.di

import com.g.pocketmal.domain.entity.converter.RecommendationEntityConverter
import com.g.pocketmal.domain.entity.converter.SearchEntityConverter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    @Singleton
    @Provides
    fun providesRecommendationsConverter() = RecommendationEntityConverter()

    @Singleton
    @Provides
    fun providesSearchConverter() = SearchEntityConverter()
}
