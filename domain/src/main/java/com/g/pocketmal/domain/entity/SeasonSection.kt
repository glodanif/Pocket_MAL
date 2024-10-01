package com.g.pocketmal.domain.entity

class SeasonSection(var title: String) {

    val items = mutableListOf<SeasonalAnime>()

    fun addItem(item: SeasonalAnime) {
        items.add(item)
    }
}
