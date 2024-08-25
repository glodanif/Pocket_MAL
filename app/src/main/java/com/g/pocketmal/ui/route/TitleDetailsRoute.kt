package com.g.pocketmal.ui.route

import com.g.pocketmal.data.util.TitleType

interface TitleDetailsRoute: BaseSessionRoute {
    fun openMalLink(link: String)
    fun openDetailsEditorForResult(id: Int, titleType: TitleType)
    fun openRecommendations(id: Int, titleType: TitleType)
    fun openPoster(url: String?)
    fun shareTitle(link: String)
    fun openDiscussion(link: String)
    fun close()
}