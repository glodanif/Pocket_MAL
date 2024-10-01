package com.g.pocketmal.domain.repository

import com.g.pocketmal.domain.TitleType
import com.g.pocketmal.domain.result.SearchResult

interface SearchRepository {
    suspend fun search(query: String, titleType: TitleType): SearchResult
    fun getMinCharactersForQuery() = 3
}
