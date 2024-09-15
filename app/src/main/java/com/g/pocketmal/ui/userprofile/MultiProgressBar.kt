package com.g.pocketmal.ui.userprofile

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.g.pocketmal.ui.userprofile.presentation.ProgressItem

@Composable
fun MultiProgressBar(
    progressItems: List<ProgressItem>,
    modifier: Modifier = Modifier,
) {
    val maxValue = progressItems.sumOf { it.value }

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(36.dp),
    ) {
        val viewWidth = size.width
        val viewHeight = size.height

        val ratio = viewWidth / maxValue

        var leftOffset = 0f

        progressItems.forEachIndexed { index, item ->
            val rectWidth = ratio * item.value
            val rightEdge = if (index == progressItems.size - 1) viewWidth else leftOffset + rectWidth

            drawRect(
                color = item.color,
                topLeft = Offset(leftOffset, 0f),
                size = Size(rightEdge - leftOffset, viewHeight)
            )

            leftOffset += rectWidth
        }
    }
}

@Preview
@Composable
fun SampleProgressBar() {
    val progressItems = listOf(
        ProgressItem(35, Color.Red),
        ProgressItem(661, Color.Magenta),
        ProgressItem(13, Color.Yellow),
        ProgressItem(31, Color.Blue),
        ProgressItem(161, Color.Cyan)
    )

    MultiProgressBar(progressItems)
}
