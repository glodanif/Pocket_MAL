package com.g.pocketmal.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ScoreLabel(
    score: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.tertiaryContainer)
            .innerShadow(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.onTertiaryContainer,
            )
            .padding(end = 12.dp, top = 4.dp, bottom = 4.dp, start = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            Icons.Rounded.Star,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.onTertiaryContainer,
            contentDescription = "Score star icon",
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = score,
            style = MaterialTheme.typography.labelLarge
                .copy(
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    fontWeight = FontWeight.Bold,
                ),
        )
    }
}
