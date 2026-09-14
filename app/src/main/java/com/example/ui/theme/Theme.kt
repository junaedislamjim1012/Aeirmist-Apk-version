package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val AeirmistColorScheme = darkColorScheme(
    primary = AeirmistCyan,
    onPrimary = AeirmistBg,
    primaryContainer = AeirmistSurfaceElevated,
    onPrimaryContainer = AeirmistCyan,
    secondary = AeirmistMagenta,
    onSecondary = AeirmistBg,
    secondaryContainer = AeirmistSurfaceElevated,
    onSecondaryContainer = AeirmistMagenta,
    tertiary = AeirmistLime,
    onTertiary = AeirmistBg,
    background = AeirmistBg,
    onBackground = AeirmistTextPrimary,
    surface = AeirmistSurface,
    onSurface = AeirmistTextPrimary,
    surfaceVariant = AeirmistSurfaceElevated,
    onSurfaceVariant = AeirmistTextSecondary,
    outline = AeirmistBorder
)

@Composable
fun AeirmistTheme(
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = AeirmistBg.toArgb()
            window.navigationBarColor = AeirmistBg.toArgb()
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = false
                isAppearanceLightNavigationBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = AeirmistColorScheme,
        content = content
    )
}
