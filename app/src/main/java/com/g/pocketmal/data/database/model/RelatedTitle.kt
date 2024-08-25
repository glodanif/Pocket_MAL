package com.g.pocketmal.data.database.model

import com.g.pocketmal.data.util.TitleType

class RelatedTitle {

    var recordType = TitleType.ANIME
    var id: Int = 0
    var name: String? = null
    var type: Int = 0

    fun isLink(): Boolean = type == LINK

    constructor()

    constructor(entityId: Int, name: String, type: Int, recordType: TitleType) {
        this.id = entityId
        this.name = name
        this.type = type
        this.recordType = recordType
    }

    companion object {
        const val LABEL = 101
        const val LINK = 102
        const val UNKNOWN_ID = -1
    }
}
