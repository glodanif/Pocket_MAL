package com.g.pocketmal.domain.entity

class SearchEntity(
        val id: Int,
        val title: String,
        val picture: String?,
        val mediaType: String,
        val episodes: Int,
        val chapters: Int,
        val synopsis: String?,
        val score: Float?,
        val nsfw: String?
)
