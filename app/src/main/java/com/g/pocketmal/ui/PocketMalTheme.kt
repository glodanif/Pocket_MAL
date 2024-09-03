package com.g.pocketmal.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun PocketMalTheme(
    ds: String,
    theme: ThemeMode,
    content: @Composable () -> Unit,
) {

    val colors = when (theme) {
        ThemeMode.BLACK -> darkColorScheme(
            primary = Color(0xff2e51a2),
            onPrimary = Color(0xffffffff),
            secondary = Color(0xff33b5e5),
            onSecondary = Color(0xffffffff),
            background = Color(0xff000000),
            onBackground = Color(0xffffffff),
        )

        ThemeMode.DARK -> darkColorScheme(
            primary = Color(0xff2e51a2),
            onPrimary = Color(0xffffffff),
            secondary = Color(0xff33b5e5),
            onSecondary = Color(0xffffffff),
            background = Color(0xff222222),
            onBackground = Color(0xffffffff),
        )

        else -> lightColorScheme(
            primary = Color(0xff2e51a2),
            onPrimary = Color(0xffffffff),
            secondary = Color(0xff33b5e5),
            onSecondary = Color(0xffffffff),
            background = Color(0xffe5e5e5),
            onBackground = Color(0xff636363),
        )
    }

    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}
