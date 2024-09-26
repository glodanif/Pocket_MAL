package com.g.pocketmal.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Replay
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.g.pocketmal.domain.TitleType
import com.g.pocketmal.ui.theme.onRe
import com.g.pocketmal.ui.theme.re

@Composable
fun ReLabel(
    titleType: TitleType,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(re)
            .innerShadow(
                shape = RoundedCornerShape(16.dp),
                offsetX = 1.dp,
                offsetY = 1.dp,
                color = onRe,
            )
            .padding(end = 12.dp, top = 2.dp, bottom = 2.dp, start = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val description = if (titleType.isAnime()) "Rewatching" else "Rereading"
        Icon(
            Icons.Rounded.Replay,
            modifier = Modifier.size(14.dp),
            tint = onRe,
            contentDescription = "$description icon",
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "RE",
            style = MaterialTheme.typography.labelSmall
                .copy(
                    color = onRe,
                    fontWeight = FontWeight.Bold,
                ),
        )
    }
}
