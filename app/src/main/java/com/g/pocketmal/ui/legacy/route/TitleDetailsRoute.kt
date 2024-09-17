package com.g.pocketmal.ui.legacy.route

import com.g.pocketmal.domain.TitleType

interface TitleDetailsRoute: com.g.pocketmal.ui.legacy.route.BaseSessionRoute {
    fun openExternalLink(link: String)
    fun openDetailsEditorForResult(id: Int, titleType: TitleType)
    fun openRecommendations(id: Int, titleType: TitleType)
    fun openPoster(url: String?)
    fun shareTitle(link: String)
    fun openDiscussion(link: String)
    fun openExternalLinkSetup()
    fun close()
}
