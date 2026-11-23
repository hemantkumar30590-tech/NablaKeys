package com.example.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

private val NablaDarkColorScheme = darkColorScheme(
    primary = CopperPrimary,
    onPrimary = CopperOnPrimary,
    primaryContainer = CopperContainer,
    onPrimaryContainer = CopperOnContainer,
    secondary = CyanSecondary,
    onSecondary = CyanOnSecondary,
    secondaryContainer = CyanContainer,
    onSecondaryContainer = CyanOnContainer,
    tertiary = EmeraldTertiary,
    onTertiary = EmeraldOnTertiary,
    tertiaryContainer = EmeraldContainer,
    onTertiaryContainer = EmeraldOnContainer,
    background = ObsidianBg,
    onBackground = SlateTextPrimary,
    surface = ObsidianSurface,
    onSurface = SlateTextPrimary,
    surfaceVariant = ObsidianSurfaceElevated,
    onSurfaceVariant = SlateTextSecondary,
    surfaceContainerLow = ObsidianSurface,
    surfaceContainer = ObsidianSurfaceElevated,
    surfaceContainerHigh = ObsidianSurfaceHighest,
    outline = ObsidianOutline,
    outlineVariant = ObsidianOutlineSubtle,
    error = AlertCoral
)

private val NablaLightColorScheme = lightColorScheme(
    primary = CopperLightPrimary,
    onPrimary = PaperSurface,
    primaryContainer = CopperLightContainer,
    onPrimaryContainer = InkPrimary,
    secondary = CyanLightSecondary,
    onSecondary = PaperSurface,
    secondaryContainer = CyanLightContainer,
    onSecondaryContainer = InkPrimary,
    tertiary = EmeraldOnTertiary,
    onTertiary = PaperSurface,
    background = PaperBg,
    onBackground = InkPrimary,
    surface = PaperSurface,
    onSurface = InkPrimary,
    surfaceVariant = PaperSurfaceElevated,
    onSurfaceVariant = InkSecondary,
    surfaceContainerLow = PaperSurface,
    surfaceContainer = PaperSurfaceElevated,
    surfaceContainerHigh = PaperSurfaceHighest,
    outline = PaperOutline,
    outlineVariant = PaperSurfaceHighest,
    error = AlertCoral
)

val NablaShapes = Shapes(
    extraSmall = RoundedCornerShape(6.dp),
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(14.dp),
    large = RoundedCornerShape(18.dp),
    extraLarge = RoundedCornerShape(24.dp)
)

@Composable
fun NablaKeysTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) NablaDarkColorScheme else NablaLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = NablaTypography,
        shapes = NablaShapes,
        content = content
    )
}
