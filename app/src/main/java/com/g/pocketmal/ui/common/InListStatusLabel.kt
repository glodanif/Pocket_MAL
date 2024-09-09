package com.g.pocketmal.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun InListStatusLabel(
    status: String,
    color: Color,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(0.dp),
) {
    Text(
        text = status,
        modifier = modifier
            .fillMaxWidth()
            .background(color.copy(alpha = .75f))
            .clip(shape)
            .innerShadow(
                shape = shape,
                color = Color.Black.copy(alpha = .75f),
            )
            .padding(end = 12.dp, top = 4.dp, bottom = 4.dp, start = 8.dp),
        style = MaterialTheme.typography.labelSmall
            .copy(
                color = Color.White,
            ),
        textAlign = TextAlign.Center,
    )
}
