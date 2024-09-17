package com.g.pocketmal.ui.browse

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import com.g.pocketmal.data.util.RankingType
import com.g.pocketmal.domain.TitleType
import com.g.pocketmal.transformedArgument
import com.g.pocketmal.ui.legacy.SkeletonActivity
import com.g.pocketmal.ui.theme.PocketMalTheme

class BrowseActivity : SkeletonActivity() {

    private val topType by transformedArgument<Int, RankingType>(EXTRA_TOP_TYPE, RankingType.ALL) {
        RankingType.from(it)
    }
    private val titleType by transformedArgument<Int, TitleType>(
        EXTRA_TITLE_TYPE,
        TitleType.ANIME
    ) {
        TitleType.from(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PocketMalTheme {
                RankedContent()
            }
        }
    }

    companion object {

        private const val EXTRA_TOP_TYPE = "extra_top_type"
        private const val EXTRA_TITLE_TYPE = "extra_title_type"

        private const val TITLES_IN_BUNCH = 50
        private const val LAZY_LOAD_OFFSET = 5

        fun start(context: Context, topType: RankingType, titleType: TitleType) {
            val intent = Intent(context, BrowseActivity::class.java)
                .putExtra(EXTRA_TOP_TYPE, topType.type)
                .putExtra(EXTRA_TITLE_TYPE, titleType.type)
            context.startActivity(intent)
        }
    }
}

@Composable
private fun RankedContent() {

}
