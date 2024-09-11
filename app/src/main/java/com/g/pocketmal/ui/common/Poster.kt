package com.g.pocketmal.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage

@Composable
fun Poster(
    url: String?,
    modifier: Modifier,
) {
    Box(modifier = modifier.background(MaterialTheme.colorScheme.inversePrimary)) {
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = url,
            contentScale = ContentScale.Crop,
            contentDescription = "Title poster",
        )
    }
}
