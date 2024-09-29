package com.g.pocketmal.ui.browse

import androidx.compose.runtime.Composable
import com.g.pocketmal.data.util.RankingType
import com.g.pocketmal.domain.TitleType

@Composable
fun BrowseScreen(
    rankingType: RankingType,
    titleType: TitleType,
    onBrowseItemClicked: (Int, TitleType) -> Unit,
    onBackPressed: () -> Unit,
) {

}
