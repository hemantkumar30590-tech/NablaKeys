package com.example.ui.keyboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.KeyboardReturn
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.SpaceBar
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.example.R
import com.example.data.CasioStyle
import com.example.data.KeyDeck
import com.example.data.KeyboardLayoutSettings
import com.example.data.OutputMode
import com.example.data.ScientificCatalog
import com.example.data.ScientificSymbol
import com.example.data.SymbolVariant
import com.example.ui.theme.DisplayFontFamily
import com.example.ui.theme.KeyboardPalette
import com.example.ui.theme.MathMonoFontFamily
import com.example.ui.theme.paletteFor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ScientificKeyboardView(
    settings: KeyboardLayoutSettings,
    activeDeck: KeyDeck,
    onSelectDeck: (KeyDeck) -> Unit,
    onInsertText: (String) -> Unit,
    onInsertTemplate: (String, Int) -> Unit = { text, _ -> onInsertText(text) },
    onBackspace: () -> Unit,
    onEnter: () -> Unit,
    onMoveCursor: (Int) -> Unit,
    onToggleOutputMode: () -> Unit,
    modifier: Modifier = Modifier,
    showImeSwitchKey: Boolean = false,
    onSwitchIme: () -> Unit = {},
    applyNavigationBarPadding: Boolean = false
) {
    val palette = remember(settings.colorway) { paletteFor(settings.colorway) }
    val haptic = LocalHapticFeedback.current
    var shifted by remember { mutableStateOf(false) }
    var activeVariantSymbol by remember { mutableStateOf<ScientificSymbol?>(null) }

    val enabledDecks = remember(settings.enabledDecks) {
        KeyDeck.entries.filter { it in settings.enabledDecks }.ifEmpty { listOf(KeyDeck.CALCULUS) }
    }
    val effectiveDeck = if (activeDeck in enabledDecks) activeDeck else enabledDecks.first()

    val pinnedSymbols = remember(settings.pinnedSymbolIds, settings.customSymbols) {
        ScientificCatalog.resolvePinnedSymbols(settings.pinnedSymbolIds, settings.customSymbols)
    }

    fun triggerHaptic() {
        if (settings.hapticEnabled) {
            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(palette.boardBackground)
            .then(if (applyNavigationBarPadding) Modifier.navigationBarsPadding() else Modifier)
            .padding(top = 4.dp, bottom = 6.dp)
            .testTag("scientific_keyboard_root")
    ) {
        // 1. Customizable Quick-Access Pinned Bar
        if (settings.showPinnedBar) {
            PinnedQuickBar(
                pinnedSymbols = pinnedSymbols,
                settings = settings,
                palette = palette,
                onSymbolClick = { symbol ->
                    triggerHaptic()
                    val template = CasioStyle.templateFor(symbol.id, settings)
                    if (template != null) {
                        onInsertTemplate(template.text, template.cursorOffset)
                    } else {
                        onInsertText(ScientificCatalog.formatInsertion(symbol, settings, shifted = false))
                    }
                },
                onToggleOutputMode = {
                    triggerHaptic()
                    onToggleOutputMode()
                }
            )
            Spacer(modifier = Modifier.height(4.dp))
        }

        // 2. Category Deck Selector Bar
        DeckSelectorRow(
            enabledDecks = enabledDecks,
            activeDeck = effectiveDeck,
            palette = palette,
            showModeInline = !settings.showPinnedBar,
            outputMode = settings.outputMode,
            onToggleOutputMode = {
                triggerHaptic()
                onToggleOutputMode()
            },
            onSelectDeck = { deck ->
                triggerHaptic()
                activeVariantSymbol = null
                onSelectDeck(deck)
            }
        )

        Spacer(modifier = Modifier.height(5.dp))

        // 3. Active Variant Picker Banner (shown when user long-presses a key with alternates)
        AnimatedVisibility(
            visible = activeVariantSymbol != null,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            activeVariantSymbol?.let { host ->
                VariantStripBar(
                    host = host,
                    outputMode = settings.outputMode,
                    palette = palette,
                    onSelectVariant = { variant ->
                        triggerHaptic()
                        val index = if (host.id == "calc_sqrt") {
                            CasioStyle.indexForVariant(variant.label) ?: CasioStyle.indexForVariant(variant.latex)
                        } else {
                            null
                        }
                        if (host.id == "calc_sqrt") {
                            val template = CasioStyle.radical(index, settings.outputMode)
                            onInsertTemplate(template.text, template.cursorOffset)
                        } else {
                            val text = if (settings.outputMode == OutputMode.LATEX) {
                                variant.latex
                            } else {
                                variant.unicode
                            }
                            onInsertText(text)
                        }
                        activeVariantSymbol = null
                    },
                    onDismiss = { activeVariantSymbol = null }
                )
            }
        }

        // 4. Main Key Grid (Either Scientific Deck or QWERTY + Math Deck)
        if (effectiveDeck == KeyDeck.QWERTY) {
            QwertyMathDeck(
                settings = settings,
                palette = palette,
                shifted = shifted,
                onKeyPress = { ch ->
                    triggerHaptic()
                    onInsertText(ch)
                    if (shifted) shifted = false
                },
                onToggleShift = {
                    triggerHaptic()
                    shifted = !shifted
                },
                onBackspace = {
                    triggerHaptic()
                    onBackspace()
                }
            )
        } else {
            val symbols = remember(effectiveDeck, settings.customSymbols) {
                ScientificCatalog.symbolsForDeck(effectiveDeck, settings.customSymbols)
            }
            ScientificSymbolGrid(
                symbols = symbols,
                settings = settings,
                palette = palette,
                shifted = shifted,
                onTapSymbol = { symbol ->
                    triggerHaptic()
                    activeVariantSymbol = null
                    val template = CasioStyle.templateFor(symbol.id, settings, shifted)
                    if (template != null) {
                        onInsertTemplate(template.text, template.cursorOffset)
                    } else {
                        onInsertText(ScientificCatalog.formatInsertion(symbol, settings, shifted))
                    }
                },
                onLongPressSymbol = { symbol ->
                    if (symbol.variants.isNotEmpty()) {
                        triggerHaptic()
                        activeVariantSymbol = symbol
                    } else if (symbol.shiftedUnicode != null) {
                        triggerHaptic()
                        val template = CasioStyle.templateFor(symbol.id, settings, !shifted)
                        if (template != null) {
                            onInsertTemplate(template.text, template.cursorOffset)
                        } else {
                            onInsertText(ScientificCatalog.formatInsertion(symbol, settings, shifted = !shifted))
                        }
                    } else if (symbol.numericValue != null) {
                        triggerHaptic()
                        onInsertText(symbol.numericValue)
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(5.dp))

        // 5. Precision Bottom Control Bar
        BottomControlRow(
            settings = settings,
            palette = palette,
            activeDeck = effectiveDeck,
            shifted = shifted,
            showImeSwitchKey = showImeSwitchKey,
            onToggleShift = {
                triggerHaptic()
                shifted = !shifted
            },
            onSwitchIme = {
                triggerHaptic()
                onSwitchIme()
            },
            onMoveCursor = { delta ->
                triggerHaptic()
                onMoveCursor(delta)
            },
            onSpace = {
                triggerHaptic()
                onInsertText(" ")
            },
            onBackspace = {
                triggerHaptic()
                onBackspace()
            },
            onEnter = {
                triggerHaptic()
                onEnter()
            }
        )
    }
}

@Composable
private fun PinnedQuickBar(
    pinnedSymbols: List<ScientificSymbol>,
    settings: KeyboardLayoutSettings,
    palette: KeyboardPalette,
    onSymbolClick: (ScientificSymbol) -> Unit,
    onToggleOutputMode: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(palette.barBackground)
            .padding(horizontal = 6.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Inline UNI / TeX toggle pill
        Surface(
            onClick = onToggleOutputMode,
            shape = RoundedCornerShape(8.dp),
            color = if (settings.outputMode == OutputMode.LATEX) {
                palette.keyAccentSurface
            } else {
                palette.keyFunctionSurface
            },
            border = BorderStroke(
                1.dp,
                if (settings.outputMode == OutputMode.LATEX) palette.keyAccentText else palette.keyBorder
            ),
            modifier = Modifier
                .height(34.dp)
                .testTag("kb_mode_toggle")
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.padding(horizontal = 9.dp)
            ) {
                Text(
                    text = if (settings.outputMode == OutputMode.LATEX) "TeX" else "UNI",
                    color = if (settings.outputMode == OutputMode.LATEX) {
                        palette.keyAccentText
                    } else {
                        palette.keyConstantText
                    },
                    fontFamily = MathMonoFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
                )
            }
        }

        Spacer(modifier = Modifier.width(6.dp))

        Row(
            modifier = Modifier
                .weight(1f)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            pinnedSymbols.forEach { symbol ->
                val textColor = when {
                    symbol.isCustom -> palette.keyAccentText
                    symbol.deck == KeyDeck.CONSTANTS -> palette.keyConstantText
                    else -> palette.keyText
                }
                Surface(
                    onClick = { onSymbolClick(symbol) },
                    shape = RoundedCornerShape(8.dp),
                    color = palette.keySurface,
                    border = BorderStroke(1.dp, palette.keyBorder),
                    modifier = Modifier
                        .height(34.dp)
                        .testTag("pinned_key_${symbol.id}")
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.padding(horizontal = 11.dp)
                    ) {
                        Text(
                            text = symbol.label,
                            color = textColor,
                            fontFamily = MathMonoFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = if (symbol.label.length > 3) 12.sp else 15.sp,
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DeckSelectorRow(
    enabledDecks: List<KeyDeck>,
    activeDeck: KeyDeck,
    palette: KeyboardPalette,
    showModeInline: Boolean,
    outputMode: OutputMode,
    onToggleOutputMode: () -> Unit,
    onSelectDeck: (KeyDeck) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (showModeInline) {
            Surface(
                onClick = onToggleOutputMode,
                shape = RoundedCornerShape(7.dp),
                color = palette.keyAccentSurface,
                border = BorderStroke(1.dp, palette.keyAccentText),
                modifier = Modifier.height(30.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Text(
                        text = if (outputMode == OutputMode.LATEX) "TeX" else "UNI",
                        color = palette.keyAccentText,
                        fontFamily = MathMonoFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp
                    )
                }
            }
        }

        enabledDecks.forEach { deck ->
            val selected = deck == activeDeck
            val bgColor by animateColorAsState(
                targetValue = if (selected) palette.chipActiveBg else palette.keyFunctionSurface,
                label = "deck_tab_bg"
            )
            val txtColor by animateColorAsState(
                targetValue = if (selected) palette.chipActiveText else palette.keyHintText,
                label = "deck_tab_txt"
            )
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
                    .height(30.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(bgColor)
                    .border(
                        width = 1.dp,
                        color = if (selected) palette.chipActiveBg else palette.keyBorder,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable { onSelectDeck(deck) }
                    .testTag("deck_tab_${deck.name.lowercase()}")
            ) {
                Text(
                    text = deck.shortLabel,
                    color = txtColor,
                    fontFamily = DisplayFontFamily,
                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun VariantStripBar(
    host: ScientificSymbol,
    outputMode: OutputMode,
    palette: KeyboardPalette,
    onSelectVariant: (SymbolVariant) -> Unit,
    onDismiss: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 6.dp, vertical = 3.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(palette.popupSurface)
            .border(1.dp, palette.keyAccentText.copy(alpha = 0.6f), RoundedCornerShape(10.dp))
            .padding(horizontal = 8.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${host.label} alt:",
            color = palette.keyAccentText,
            fontFamily = MathMonoFontFamily,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.width(6.dp))
        Row(
            modifier = Modifier
                .weight(1f)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            host.variants.forEach { variant ->
                Surface(
                    onClick = { onSelectVariant(variant) },
                    shape = RoundedCornerShape(7.dp),
                    color = palette.keySurface,
                    border = BorderStroke(1.dp, palette.keyBorder),
                    modifier = Modifier.height(32.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 10.dp)
                    ) {
                        Text(
                            text = if (outputMode == OutputMode.LATEX) variant.latex else variant.label,
                            color = palette.keyText,
                            fontFamily = MathMonoFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp,
                            maxLines = 1
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "✕",
            color = palette.keyHintText,
            fontSize = 14.sp,
            modifier = Modifier
                .clip(CircleShape)
                .clickable(onClick = onDismiss)
                .padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun ScientificSymbolGrid(
    symbols: List<ScientificSymbol>,
    settings: KeyboardLayoutSettings,
    palette: KeyboardPalette,
    shifted: Boolean,
    onTapSymbol: (ScientificSymbol) -> Unit,
    onLongPressSymbol: (ScientificSymbol) -> Unit
) {
    val cols = settings.columnsPerRow.coerceIn(6, 9)
    val rows = remember(symbols, cols) {
        symbols.chunked(cols).take(4)
    }
    val keyHeight = settings.keyHeightPreset.heightDp.dp
    val cornerRadius = settings.keyCornerPreset.radiusDp.dp

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        rows.forEach { rowSymbols ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                rowSymbols.forEach { symbol ->
                    ScientificKeycap(
                        symbol = symbol,
                        settings = settings,
                        palette = palette,
                        shifted = shifted,
                        cornerRadius = cornerRadius,
                        modifier = Modifier
                            .weight(1f)
                            .height(keyHeight),
                        onTap = { onTapSymbol(symbol) },
                        onLongPress = { onLongPressSymbol(symbol) }
                    )
                }
                val emptySlots = cols - rowSymbols.size
                repeat(emptySlots) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ScientificKeycap(
    symbol: ScientificSymbol,
    settings: KeyboardLayoutSettings,
    palette: KeyboardPalette,
    shifted: Boolean,
    cornerRadius: androidx.compose.ui.unit.Dp,
    modifier: Modifier = Modifier,
    onTap: () -> Unit,
    onLongPress: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.93f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessHigh),
        label = "key_scale"
    )

    val displayLabel = if (shifted && symbol.shiftedLabel != null) {
        symbol.shiftedLabel
    } else {
        symbol.label
    }

    val accentColor = when {
        symbol.isCustom -> palette.keyAccentText
        symbol.deck == KeyDeck.CONSTANTS -> palette.keyConstantText
        symbol.deck == KeyDeck.CALCULUS && symbol.id in setOf(
            "calc_int", "calc_iint", "calc_oint", "calc_partial", "calc_nabla", "calc_sum", "calc_prod"
        ) -> palette.keyAccentText
        else -> palette.keyText
    }

    val hintText = remember(symbol, settings.outputMode, settings.showLatexHints, shifted) {
        if (!settings.showLatexHints) {
            null
        } else if (symbol.deck == KeyDeck.CONSTANTS && symbol.numericValue != null) {
            symbol.numericValue.take(5)
        } else if (settings.outputMode == OutputMode.LATEX) {
            (if (shifted) symbol.shiftedLatex else symbol.latex)
                ?.removePrefix("\\")
                ?.take(5)
        } else if (symbol.shiftedLabel != null && !shifted) {
            symbol.shiftedLabel.take(4)
        } else {
            null
        }
    }

    val fontSize = when {
        displayLabel.length >= 4 -> 11.sp
        displayLabel.length == 3 -> 13.sp
        displayLabel.length == 2 -> 15.sp
        else -> 18.sp
    }

    val shape = RoundedCornerShape(cornerRadius)
    val bgColor = if (isPressed) palette.keySurfacePressed else palette.keySurface

    Box(
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .drawBehind {
                // Machined bottom bevel ledge
                drawRoundRect(
                    color = palette.keyBorder.copy(alpha = 0.65f),
                    topLeft = Offset(0f, 2.dp.toPx()),
                    size = Size(size.width, size.height),
                    cornerRadius = CornerRadius(cornerRadius.toPx(), cornerRadius.toPx())
                )
            }
            .clip(shape)
            .background(bgColor)
            .border(1.dp, palette.keyBorder, shape)
            .combinedClickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onTap,
                onLongClick = onLongPress
            )
            .testTag("key_${symbol.id}"),
        contentAlignment = Alignment.Center
    ) {
        // Floating key press preview popup
        if (isPressed && settings.showKeyPopup) {
            Popup(
                alignment = Alignment.TopCenter,
                offset = IntOffset(0, -110),
                properties = PopupProperties(focusable = false)
            ) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = palette.popupSurface,
                    border = BorderStroke(1.dp, palette.keyAccentText),
                    shadowElevation = 6.dp
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = displayLabel,
                            color = palette.keyText,
                            fontFamily = MathMonoFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp
                        )
                        if (settings.outputMode == OutputMode.LATEX) {
                            Text(
                                text = symbol.latex,
                                color = palette.keyAccentText,
                                fontFamily = MathMonoFontFamily,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }
        }

        // Top-right micro hint
        if (hintText != null) {
            Text(
                text = hintText,
                color = palette.keyHintText.copy(alpha = 0.85f),
                fontFamily = MathMonoFontFamily,
                fontSize = 8.sp,
                maxLines = 1,
                softWrap = false,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 2.dp, end = 4.dp)
            )
        }

        // Subtle long-press indicator dot
        if (symbol.variants.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 3.dp, end = 4.dp)
                    .size(3.5.dp)
                    .clip(CircleShape)
                    .background(palette.keyAccentText.copy(alpha = 0.7f))
            )
        }

        Text(
            text = displayLabel,
            color = accentColor,
            fontFamily = MathMonoFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = fontSize,
            maxLines = 1,
            softWrap = false
        )
    }
}

@Composable
private fun QwertyMathDeck(
    settings: KeyboardLayoutSettings,
    palette: KeyboardPalette,
    shifted: Boolean,
    onKeyPress: (String) -> Unit,
    onToggleShift: () -> Unit,
    onBackspace: () -> Unit
) {
    val numbers = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "0")
    val row1 = listOf("q", "w", "e", "r", "t", "y", "u", "i", "o", "p")
    val row2 = listOf("a", "s", "d", "f", "g", "h", "j", "k", "l")
    val row3 = listOf("z", "x", "c", "v", "b", "n", "m")
    val mathOps = listOf("(", ")", "=", "+", "-", "*", "/", "^", "_", "\\")

    val cornerRadius = settings.keyCornerPreset.radiusDp.dp
    val keyHeight = (settings.keyHeightPreset.heightDp - 3).coerceAtLeast(36).dp

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        if (settings.showNumberRowOnQwerty) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                val topKeys = if (shifted) mathOps else numbers
                topKeys.forEach { k ->
                    SimpleCharKey(
                        label = k,
                        palette = palette,
                        cornerRadius = cornerRadius,
                        isAccent = shifted,
                        modifier = Modifier
                            .weight(1f)
                            .height(34.dp),
                        onClick = { onKeyPress(k) }
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            row1.forEach { k ->
                val out = if (shifted) k.uppercase() else k
                SimpleCharKey(
                    label = out,
                    palette = palette,
                    cornerRadius = cornerRadius,
                    modifier = Modifier
                        .weight(1f)
                        .height(keyHeight),
                    onClick = { onKeyPress(out) }
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            row2.forEach { k ->
                val out = if (shifted) k.uppercase() else k
                SimpleCharKey(
                    label = out,
                    palette = palette,
                    cornerRadius = cornerRadius,
                    modifier = Modifier
                        .weight(1f)
                        .height(keyHeight),
                    onClick = { onKeyPress(out) }
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Shift key
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1.45f)
                    .height(keyHeight)
                    .clip(RoundedCornerShape(cornerRadius))
                    .background(if (shifted) palette.chipActiveBg else palette.keyFunctionSurface)
                    .border(1.dp, palette.keyBorder, RoundedCornerShape(cornerRadius))
                    .clickable(onClick = onToggleShift)
            ) {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowUp,
                    contentDescription = stringResource(R.string.kb_cd_shift),
                    tint = if (shifted) palette.chipActiveText else palette.keyText,
                    modifier = Modifier.size(20.dp)
                )
            }

            row3.forEach { k ->
                val out = if (shifted) k.uppercase() else k
                SimpleCharKey(
                    label = out,
                    palette = palette,
                    cornerRadius = cornerRadius,
                    modifier = Modifier
                        .weight(1f)
                        .height(keyHeight),
                    onClick = { onKeyPress(out) }
                )
            }

            // Quick math comma/equals or backspace duplicate for thumb reach
            SimpleCharKey(
                label = if (shifted) "=" else "(",
                palette = palette,
                cornerRadius = cornerRadius,
                isAccent = true,
                modifier = Modifier
                    .weight(0.95f)
                    .height(keyHeight),
                onClick = { onKeyPress(if (shifted) "=" else "(") }
            )
            SimpleCharKey(
                label = if (shifted) "+" else ")",
                palette = palette,
                cornerRadius = cornerRadius,
                isAccent = true,
                modifier = Modifier
                    .weight(0.95f)
                    .height(keyHeight),
                onClick = { onKeyPress(if (shifted) "+" else ")") }
            )
        }
    }
}

@Composable
private fun SimpleCharKey(
    label: String,
    palette: KeyboardPalette,
    cornerRadius: androidx.compose.ui.unit.Dp,
    modifier: Modifier = Modifier,
    isAccent: Boolean = false,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(cornerRadius)
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .clip(shape)
            .background(palette.keySurface)
            .border(1.dp, palette.keyBorder, shape)
            .clickable(onClick = onClick)
    ) {
        Text(
            text = label,
            color = if (isAccent) palette.keyAccentText else palette.keyText,
            fontFamily = MathMonoFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            maxLines = 1
        )
    }
}

@Composable
private fun BottomControlRow(
    settings: KeyboardLayoutSettings,
    palette: KeyboardPalette,
    activeDeck: KeyDeck,
    shifted: Boolean,
    showImeSwitchKey: Boolean,
    onToggleShift: () -> Unit,
    onSwitchIme: () -> Unit,
    onMoveCursor: (Int) -> Unit,
    onSpace: () -> Unit,
    onBackspace: () -> Unit,
    onEnter: () -> Unit
) {
    val corner = RoundedCornerShape(settings.keyCornerPreset.radiusDp.dp)
    val rowHeight = 44.dp
    val shiftLabel = when (activeDeck) {
        KeyDeck.GREEK -> if (shifted) "ΑΒΓ" else "αβγ"
        KeyDeck.SCRIPTS -> if (shifted) "xₙ" else "xⁿ"
        else -> if (shifted) "ALT" else "2nd"
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 2nd / Shift toggle
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .weight(1.25f)
                .height(rowHeight)
                .clip(corner)
                .background(if (shifted) palette.chipActiveBg else palette.keyFunctionSurface)
                .border(1.dp, if (shifted) palette.chipActiveBg else palette.keyBorder, corner)
                .clickable(onClick = onToggleShift)
                .testTag("kb_shift_toggle")
        ) {
            Text(
                text = shiftLabel,
                color = if (shifted) palette.chipActiveText else palette.keyAccentText,
                fontFamily = DisplayFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
        }

        if (showImeSwitchKey) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(0.9f)
                    .height(rowHeight)
                    .clip(corner)
                    .background(palette.keyFunctionSurface)
                    .border(1.dp, palette.keyBorder, corner)
                    .clickable(onClick = onSwitchIme)
                    .testTag("kb_switch_ime")
            ) {
                Icon(
                    imageVector = Icons.Filled.Language,
                    contentDescription = stringResource(R.string.kb_cd_next_ime),
                    tint = palette.keyText,
                    modifier = Modifier.size(19.dp)
                )
            }
        }

        // Cursor Left
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .weight(0.85f)
                .height(rowHeight)
                .clip(corner)
                .background(palette.keyFunctionSurface)
                .border(1.dp, palette.keyBorder, corner)
                .clickable { onMoveCursor(-1) }
                .testTag("kb_cursor_left")
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = stringResource(R.string.studio_move_cursor_left),
                tint = palette.keyText,
                modifier = Modifier.size(20.dp)
            )
        }

        // Tactile Spacebar
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .weight(2.6f)
                .height(rowHeight)
                .clip(corner)
                .background(palette.keySurface)
                .border(1.dp, palette.keyBorder, corner)
                .clickable(onClick = onSpace)
                .testTag("kb_space")
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.SpaceBar,
                    contentDescription = stringResource(R.string.kb_cd_space),
                    tint = palette.keyHintText,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "NABLA · ${settings.outputMode.label.uppercase()}",
                    color = palette.keyHintText,
                    fontFamily = MathMonoFontFamily,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1
                )
            }
        }

        // Cursor Right
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .weight(0.85f)
                .height(rowHeight)
                .clip(corner)
                .background(palette.keyFunctionSurface)
                .border(1.dp, palette.keyBorder, corner)
                .clickable { onMoveCursor(1) }
                .testTag("kb_cursor_right")
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = stringResource(R.string.studio_move_cursor_right),
                tint = palette.keyText,
                modifier = Modifier.size(20.dp)
            )
        }

        // Repeating Backspace Key
        RepeatingBackspaceKey(
            palette = palette,
            shape = corner,
            modifier = Modifier
                .weight(1.2f)
                .height(rowHeight),
            onBackspace = onBackspace
        )

        // Enter / Return
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .weight(1.25f)
                .height(rowHeight)
                .clip(corner)
                .background(palette.chipActiveBg)
                .clickable(onClick = onEnter)
                .testTag("kb_enter")
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardReturn,
                contentDescription = stringResource(R.string.kb_cd_enter),
                tint = palette.chipActiveText,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun RepeatingBackspaceKey(
    palette: KeyboardPalette,
    shape: RoundedCornerShape,
    modifier: Modifier = Modifier,
    onBackspace: () -> Unit
) {
    val currentOnBackspace by rememberUpdatedState(onBackspace)
    val scope = rememberCoroutineScope()
    var pressed by remember { mutableStateOf(false) }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .clip(shape)
            .background(if (pressed) palette.keySurfacePressed else palette.keyFunctionSurface)
            .border(1.dp, palette.keyBorder, shape)
            .pointerInput(Unit) {
                awaitEachGesture {
                    awaitFirstDown(requireUnconsumed = false)
                    pressed = true
                    currentOnBackspace()
                    val repeatJob = scope.launch {
                        delay(380L)
                        while (true) {
                            currentOnBackspace()
                            delay(65L)
                        }
                    }
                    waitForUpOrCancellation()
                    repeatJob.cancel()
                    pressed = false
                }
            }
            .testTag("kb_backspace")
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.Backspace,
            contentDescription = stringResource(R.string.kb_cd_backspace),
            tint = palette.keyText,
            modifier = Modifier.size(20.dp)
        )
    }
}
