package com.example.ui.theme

import androidx.compose.ui.graphics.Color
import com.example.data.KeyboardColorway

// Brand Swiss Scientific Instrument Palette (Obsidian Slate & Copper-Amber + Precision Cyan)
val ObsidianBg = Color(0xFF0D1117)
val ObsidianSurface = Color(0xFF141A23)
val ObsidianSurfaceElevated = Color(0xFF1B2330)
val ObsidianSurfaceHighest = Color(0xFF242F40)
val ObsidianOutline = Color(0xFF303D52)
val ObsidianOutlineSubtle = Color(0xFF222C3C)

val CopperPrimary = Color(0xFFF0A35E)
val CopperOnPrimary = Color(0xFF261404)
val CopperContainer = Color(0xFF3E2610)
val CopperOnContainer = Color(0xFFFFDCB9)

val CyanSecondary = Color(0xFF56CCF2)
val CyanOnSecondary = Color(0xFF04222D)
val CyanContainer = Color(0xFF113340)
val CyanOnContainer = Color(0xFFBFEFFF)

val EmeraldTertiary = Color(0xFF4ADE80)
val EmeraldOnTertiary = Color(0xFF052914)
val EmeraldContainer = Color(0xFF123622)
val EmeraldOnContainer = Color(0xFFBBF7D0)

val SlateTextPrimary = Color(0xFFF1F5F9)
val SlateTextSecondary = Color(0xFF94A3B8)
val AlertCoral = Color(0xFFF87171)

// Daylight Paper & Ink Palette
val PaperBg = Color(0xFFF4F5F7)
val PaperSurface = Color(0xFFFFFFFF)
val PaperSurfaceElevated = Color(0xFFECEFF4)
val PaperSurfaceHighest = Color(0xFFE2E7F0)
val PaperOutline = Color(0xFFC5CEDD)
val InkPrimary = Color(0xFF111827)
val InkSecondary = Color(0xFF4B5563)
val CopperLightPrimary = Color(0xFFB95B14)
val CopperLightContainer = Color(0xFFFFE5D0)
val CyanLightSecondary = Color(0xFF0369A1)
val CyanLightContainer = Color(0xFFD8F3FF)

data class KeyboardPalette(
    val boardBackground: Color,
    val barBackground: Color,
    val keySurface: Color,
    val keySurfacePressed: Color,
    val keyFunctionSurface: Color,
    val keyAccentSurface: Color,
    val keyBorder: Color,
    val keyText: Color,
    val keyAccentText: Color,
    val keyConstantText: Color,
    val keyHintText: Color,
    val chipActiveBg: Color,
    val chipActiveText: Color,
    val popupSurface: Color
)

fun paletteFor(colorway: KeyboardColorway): KeyboardPalette = when (colorway) {
    KeyboardColorway.OBSIDIAN -> KeyboardPalette(
        boardBackground = Color(0xFF0B0E14),
        barBackground = Color(0xFF121721),
        keySurface = Color(0xFF1C2331),
        keySurfacePressed = Color(0xFF2B3548),
        keyFunctionSurface = Color(0xFF151B26),
        keyAccentSurface = Color(0xFF3A2512),
        keyBorder = Color(0xFF2C364A),
        keyText = Color(0xFFF1F5F9),
        keyAccentText = Color(0xFFF0A35E),
        keyConstantText = Color(0xFF56CCF2),
        keyHintText = Color(0xFF7B8AA3),
        chipActiveBg = Color(0xFFF0A35E),
        chipActiveText = Color(0xFF1D1004),
        popupSurface = Color(0xFF263043)
    )
    KeyboardColorway.BLUEPRINT -> KeyboardPalette(
        boardBackground = Color(0xFF081426),
        barBackground = Color(0xFF0D1D36),
        keySurface = Color(0xFF13294B),
        keySurfacePressed = Color(0xFF1E3D6B),
        keyFunctionSurface = Color(0xFF0F213D),
        keyAccentSurface = Color(0xFF103E54),
        keyBorder = Color(0xFF24497A),
        keyText = Color(0xFFEAF4FF),
        keyAccentText = Color(0xFF63D4FF),
        keyConstantText = Color(0xFFFFCA7A),
        keyHintText = Color(0xFF7EA2CE),
        chipActiveBg = Color(0xFF56CCF2),
        chipActiveText = Color(0xFF041A29),
        popupSurface = Color(0xFF1D3A66)
    )
    KeyboardColorway.CHALK -> KeyboardPalette(
        boardBackground = Color(0xFFE6EAF0),
        barBackground = Color(0xFFEDF0F5),
        keySurface = Color(0xFFFFFFFF),
        keySurfacePressed = Color(0xFFDCE3EE),
        keyFunctionSurface = Color(0xFFD7DEE9),
        keyAccentSurface = Color(0xFFFCE6D4),
        keyBorder = Color(0xFFC5CFDF),
        keyText = Color(0xFF111827),
        keyAccentText = Color(0xFFB45309),
        keyConstantText = Color(0xFF0369A1),
        keyHintText = Color(0xFF64748B),
        chipActiveBg = Color(0xFF1E293B),
        chipActiveText = Color(0xFFF8FAFC),
        popupSurface = Color(0xFF1E293B)
    )
    KeyboardColorway.EMERALD_LAB -> KeyboardPalette(
        boardBackground = Color(0xFF07110D),
        barBackground = Color(0xFF0D1B15),
        keySurface = Color(0xFF13261E),
        keySurfacePressed = Color(0xFF1E3A2E),
        keyFunctionSurface = Color(0xFF0F1F18),
        keyAccentSurface = Color(0xFF153B2B),
        keyBorder = Color(0xFF214234),
        keyText = Color(0xFFE6F7EF),
        keyAccentText = Color(0xFF4ADE80),
        keyConstantText = Color(0xFF7DD3FC),
        keyHintText = Color(0xFF6B9480),
        chipActiveBg = Color(0xFF4ADE80),
        chipActiveText = Color(0xFF042211),
        popupSurface = Color(0xFF1B362A)
    )
}
