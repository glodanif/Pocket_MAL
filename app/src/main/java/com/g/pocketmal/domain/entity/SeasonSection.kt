package com.g.pocketmal.domain.entity

class SeasonSection(var title: String) {

    val items = mutableListOf<SeasonEntity>()

    fun addItem(item: SeasonEntity) {
        items.add(item)
    }

    override fun toString(): String {
        return "SeasonalSection{" +
                "title='" + title + '\'' +
                ", items=" + items + ", " + items.size +
                '}'
    }
}