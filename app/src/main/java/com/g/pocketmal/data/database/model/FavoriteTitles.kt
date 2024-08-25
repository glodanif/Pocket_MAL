package com.g.pocketmal.data.database.model

class FavoriteTitles(val titles: List<Title>) {

    class Title {
        var id: Int = 0
        var name: String? = null
        var poster: String? = null
        var data: String? = null
    }
}
