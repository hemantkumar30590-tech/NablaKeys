package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.ConstantInsertMode
import com.example.data.KeyCornerPreset
import com.example.data.KeyDeck
import com.example.data.KeyHeightPreset
import com.example.data.KeyboardColorway
import com.example.data.KeyboardLayoutSettings
import com.example.data.OutputMode
import com.example.data.ScientificCatalog
import com.example.ime.ImeStatus
import com.example.ime.ImeStatusHelper
import com.example.ui.theme.MathMonoFontFamily
import com.example.ui.theme.paletteFor

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LayoutStudioScreen(
    settings: KeyboardLayoutSettings,
    imeStatus: ImeStatus,
    onOutputModeChange: (OutputMode) -> Unit,
    onConstantInsertModeChange: (ConstantInsertMode) -> Unit,
    onColumnsChange: (Int) -> Unit,
    onKeyHeightChange: (KeyHeightPreset) -> Unit,
    onKeyCornerChange: (KeyCornerPreset) -> Unit,
    onColorwayChange: (KeyboardColorway) -> Unit,
    onToggleDeckEnabled: (KeyDeck) -> Unit,
    onDefaultDeckChange: (KeyDeck) -> Unit,
    onMovePinned: (String, Int) -> Unit,
    onRemovePinned: (String) -> Unit,
    onAddCustomSymbol: (label: String, unicode: String, latex: String, desc: String) -> Unit,
    onRemoveCustomSymbol: (String) -> Unit,
    onHapticChange: (Boolean) -> Unit,
    onKeyPopupChange: (Boolean) -> Unit,
    onLatexHintsChange: (Boolean) -> Unit,
    onAutoSpaceChange: (Boolean) -> Unit,
    onShowPinnedBarChange: (Boolean) -> Unit,
    onShowNumberRowChange: (Boolean) -> Unit,
    onResetDefaults: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showCustomKeyDialog by remember { mutableStateOf(false) }

    val pinnedSymbols = remember(settings.pinnedSymbolIds, settings.customSymbols) {
        ScientificCatalog.resolvePinnedSymbols(settings.pinnedSymbolIds, settings.customSymbols)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .widthIn(max = 840.dp)
                .fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item(key = "header") {
                Column {
                    Text(
                        text = stringResource(R.string.layout_title),
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = stringResource(R.string.layout_subtitle),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // 1. System IME Setup Banner Card with bespoke blueprint vector-field artwork
            item(key = "ime_hero_card") {
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Image(
                            painter = painterResource(id = R.drawable.img_calculus_blueprint_1795439724751),
                            contentDescription = "Vector field blueprint header",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.matchParentSize()
                        )
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color(0xD90E131B),
                                            Color(0xEE0D1117)
                                        )
                                    )
                                )
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = if (imeStatus.isSelected) Icons.Filled.CheckCircle else Icons.Filled.Keyboard,
                                    contentDescription = null,
                                    tint = if (imeStatus.isSelected) {
                                        MaterialTheme.colorScheme.tertiary
                                    } else {
                                        MaterialTheme.colorScheme.primary
                                    },
                                    modifier = Modifier.size(22.dp)
                                )
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = stringResource(R.string.layout_ime_card_title),
                                        style = MaterialTheme.typography.titleMedium,
                                        color = Color(0xFFF8FAFC)
                                    )
                                    Text(
                                        text = when {
                                            imeStatus.isSelected -> stringResource(R.string.layout_ime_status_active)
                                            imeStatus.isEnabled -> stringResource(R.string.layout_ime_status_enabled)
                                            else -> stringResource(R.string.layout_ime_status_disabled)
                                        },
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color(0xFFCBD5E1)
                                    )
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = { ImeStatusHelper.openInputMethodSettings(context) },
                                    shape = RoundedCornerShape(11.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary,
                                        contentColor = MaterialTheme.colorScheme.onPrimary
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(44.dp)
                                        .testTag("ime_enable_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Settings,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = stringResource(R.string.layout_ime_enable_btn),
                                        style = MaterialTheme.typography.labelLarge
                                    )
                                }
                                OutlinedButton(
                                    onClick = { ImeStatusHelper.showInputMethodPicker(context) },
                                    shape = RoundedCornerShape(11.dp),
                                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(44.dp)
                                        .testTag("ime_switch_button")
                                ) {
                                    Text(
                                        text = stringResource(R.string.layout_ime_switch_btn),
                                        style = MaterialTheme.typography.labelLarge,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 2. Live Keycap Preview Strip
            item(key = "live_preview") {
                LiveKeycapPreviewCard(settings = settings)
            }

            // 3. Keycap Colorway Section
            item(key = "colorway_section") {
                SettingsSectionCard(title = stringResource(R.string.layout_section_theme)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        KeyboardColorway.entries.forEach { colorway ->
                            val selected = colorway == settings.colorway
                            val pal = paletteFor(colorway)
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(pal.boardBackground)
                                    .border(
                                        width = if (selected) 2.dp else 1.dp,
                                        color = if (selected) MaterialTheme.colorScheme.primary else pal.keyBorder,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .clickable { onColorwayChange(colorway) }
                                    .padding(vertical = 10.dp, horizontal = 4.dp)
                                    .testTag("colorway_${colorway.name.lowercase()}")
                            ) {
                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clip(RoundedCornerShape(5.dp))
                                            .background(pal.keySurface)
                                            .border(1.dp, pal.keyBorder, RoundedCornerShape(5.dp))
                                    ) {
                                        Text(
                                            text = "∫",
                                            color = pal.keyAccentText,
                                            fontFamily = MathMonoFontFamily,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clip(RoundedCornerShape(5.dp))
                                            .background(pal.keySurface)
                                            .border(1.dp, pal.keyBorder, RoundedCornerShape(5.dp))
                                    ) {
                                        Text(
                                            text = "ℏ",
                                            color = pal.keyConstantText,
                                            fontFamily = MathMonoFontFamily,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = colorway.displayName.substringBefore(" "),
                                    color = pal.keyText,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }
                }
            }

            // 4. Keycap Geometry & Grid Density
            item(key = "geometry_section") {
                SettingsSectionCard(title = stringResource(R.string.layout_section_geometry)) {
                    SettingRowLabel("Keys per Row (Grid Density)", "${settings.columnsPerRow} columns")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(6, 7, 8, 9).forEach { cols ->
                            SegmentedChoicePill(
                                text = "$cols cols",
                                selected = settings.columnsPerRow == cols,
                                onClick = { onColumnsChange(cols) },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("cols_$cols")
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    SettingRowLabel("Keycap Height", "${settings.keyHeightPreset.heightDp} dp")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        KeyHeightPreset.entries.forEach { preset ->
                            SegmentedChoicePill(
                                text = preset.displayName,
                                selected = settings.keyHeightPreset == preset,
                                onClick = { onKeyHeightChange(preset) },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("height_${preset.name.lowercase()}")
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    SettingRowLabel("Keycap Shape", "${settings.keyCornerPreset.radiusDp} dp radius")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        KeyCornerPreset.entries.forEach { preset ->
                            SegmentedChoicePill(
                                text = preset.displayName,
                                selected = settings.keyCornerPreset == preset,
                                onClick = { onKeyCornerChange(preset) },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("corner_${preset.name.lowercase()}")
                            )
                        }
                    }
                }
            }

            // 5. Output & Constants Behavior
            item(key = "output_section") {
                SettingsSectionCard(title = stringResource(R.string.layout_section_output)) {
                    SettingRowLabel("Default Symbol Output", settings.outputMode.label)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutputMode.entries.forEach { mode ->
                            SegmentedChoicePill(
                                text = mode.label,
                                selected = settings.outputMode == mode,
                                onClick = { onOutputModeChange(mode) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    SettingRowLabel(
                        "Constant Tap Behavior",
                        settings.constantInsertMode.description
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ConstantInsertMode.entries.forEach { mode ->
                            SegmentedChoicePill(
                                text = mode.label,
                                selected = settings.constantInsertMode == mode,
                                onClick = { onConstantInsertModeChange(mode) },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("const_mode_${mode.name.lowercase()}")
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    ToggleSettingRow(
                        title = "Auto-space after calculus & logic operators",
                        subtitle = "Appends a trailing space after operators like ∫, ∇, ∑, ∀",
                        checked = settings.autoSpaceOperators,
                        onCheckedChange = onAutoSpaceChange
                    )
                }
            }

            // 6. Pinned Quick-Access Bar & Custom Macro Keys
            item(key = "pinned_section") {
                SettingsSectionCard(title = stringResource(R.string.layout_section_pinned)) {
                    ToggleSettingRow(
                        title = "Show Quick-Access Bar above keyboard",
                        subtitle = "One-tap strip for pinned symbols & custom macros",
                        checked = settings.showPinnedBar,
                        onCheckedChange = onShowPinnedBarChange
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = { showCustomKeyDialog = true },
                        shape = RoundedCornerShape(11.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("add_custom_key_button")
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = stringResource(R.string.layout_add_custom_key),
                            style = MaterialTheme.typography.labelLarge
                        )
                    }

                    if (pinnedSymbols.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "PINNED ORDER (${pinnedSymbols.size})",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            pinnedSymbols.forEachIndexed { idx, sym ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(MaterialTheme.colorScheme.surfaceVariant)
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = sym.label,
                                        fontFamily = MathMonoFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = if (sym.isCustom) {
                                            MaterialTheme.colorScheme.tertiary
                                        } else {
                                            MaterialTheme.colorScheme.primary
                                        },
                                        modifier = Modifier.widthIn(min = 38.dp)
                                    )
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = sym.name,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = sym.latex,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    IconButton(
                                        onClick = { onMovePinned(sym.id, -1) },
                                        enabled = idx > 0,
                                        modifier = Modifier.size(34.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.ArrowUpward,
                                            contentDescription = "Move up",
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                    IconButton(
                                        onClick = { onMovePinned(sym.id, 1) },
                                        enabled = idx < pinnedSymbols.lastIndex,
                                        modifier = Modifier.size(34.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.ArrowDownward,
                                            contentDescription = "Move down",
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                    IconButton(
                                        onClick = {
                                            if (sym.isCustom) onRemoveCustomSymbol(sym.id)
                                            else onRemovePinned(sym.id)
                                        },
                                        modifier = Modifier.size(34.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (sym.isCustom) Icons.Filled.DeleteOutline else Icons.Filled.Close,
                                            contentDescription = "Remove from bar",
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 7. Visible Symbol Decks & Default Deck
            item(key = "decks_section") {
                SettingsSectionCard(title = stringResource(R.string.layout_section_decks)) {
                    Text(
                        text = "Toggle which symbol decks appear on the keyboard bar:",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        KeyDeck.entries.forEach { deck ->
                            val enabled = deck in settings.enabledDecks
                            FilterChip(
                                selected = enabled,
                                onClick = { onToggleDeckEnabled(deck) },
                                label = { Text("${deck.shortLabel} · ${deck.displayName}") },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                                    selectedLabelColor = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    SettingRowLabel("Default Opening Deck", settings.defaultDeck.displayName)
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        KeyDeck.entries.filter { it in settings.enabledDecks }.forEach { deck ->
                            val isDefault = deck == settings.defaultDeck
                            FilterChip(
                                selected = isDefault,
                                onClick = { onDefaultDeckChange(deck) },
                                label = { Text(deck.shortLabel) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                                )
                            )
                        }
                    }
                }
            }

            // 8. Tactile & Display Feedback
            item(key = "feedback_section") {
                SettingsSectionCard(title = stringResource(R.string.layout_section_feedback)) {
                    ToggleSettingRow(
                        title = "Haptic key feedback",
                        subtitle = "Subtle tactile tick on every scientific key press",
                        checked = settings.hapticEnabled,
                        onCheckedChange = onHapticChange
                    )
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outlineVariant,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    ToggleSettingRow(
                        title = "Key press popup preview",
                        subtitle = "Show magnified glyph & LaTeX bubble when pressing keys",
                        checked = settings.showKeyPopup,
                        onCheckedChange = onKeyPopupChange
                    )
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outlineVariant,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    ToggleSettingRow(
                        title = "Show LaTeX / value micro-hints on keycaps",
                        subtitle = "Displays subtle secondary hints in top-right key corners",
                        checked = settings.showLatexHints,
                        onCheckedChange = onLatexHintsChange
                    )
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outlineVariant,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    ToggleSettingRow(
                        title = "Dedicated number & math-op row on QWERTY",
                        subtitle = "Adds 1–0 (and shifted ( ) = + - ^ _) above QWERTY",
                        checked = settings.showNumberRowOnQwerty,
                        onCheckedChange = onShowNumberRowChange
                    )
                }
            }

            // 9. Reset Button
            item(key = "reset_section") {
                OutlinedButton(
                    onClick = onResetDefaults,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("reset_defaults_button")
                ) {
                    Icon(
                        imageVector = Icons.Filled.RestartAlt,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(R.string.layout_reset_defaults))
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    if (showCustomKeyDialog) {
        AddCustomMacroDialog(
            onDismiss = { showCustomKeyDialog = false },
            onSave = { label, unicode, latex, desc ->
                onAddCustomSymbol(label, unicode, latex, desc)
                showCustomKeyDialog = false
            }
        )
    }
}

@Composable
private fun LiveKeycapPreviewCard(settings: KeyboardLayoutSettings) {
    val palette = remember(settings.colorway) { paletteFor(settings.colorway) }
    val corner = RoundedCornerShape(settings.keyCornerPreset.radiusDp.dp)
    val height = settings.keyHeightPreset.heightDp.dp
    val sampleKeys = listOf(
        Triple("∫", "\\int", true),
        Triple("∂/∂x", "\\frac", true),
        Triple("∇²", "\\nabla", true),
        Triple("ℏ", "1.05e", false),
        Triple("π", "3.141", false),
        Triple("∑", "\\sum", true)
    )

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = palette.boardBackground,
        border = BorderStroke(1.dp, palette.keyBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.layout_preview_title).uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = palette.keyHintText
                )
                Text(
                    text = "${settings.columnsPerRow} cols · ${settings.keyHeightPreset.heightDp}dp · ${settings.outputMode.label}",
                    style = MaterialTheme.typography.labelSmall,
                    color = palette.keyAccentText
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                sampleKeys.forEach { (glyph, hint, isCalc) ->
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .weight(1f)
                            .height(height)
                            .clip(corner)
                            .background(palette.keySurface)
                            .border(1.dp, palette.keyBorder, corner)
                    ) {
                        if (settings.showLatexHints) {
                            Text(
                                text = hint,
                                color = palette.keyHintText,
                                fontFamily = MathMonoFontFamily,
                                fontSize = 8.sp,
                                maxLines = 1,
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(top = 2.dp, end = 4.dp)
                            )
                        }
                        Text(
                            text = glyph,
                            color = if (isCalc) palette.keyAccentText else palette.keyConstantText,
                            fontFamily = MathMonoFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = if (glyph.length > 2) 12.sp else 18.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingsSectionCard(
    title: String,
    content: @Composable () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = title.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(10.dp))
            content()
        }
    }
}

@Composable
private fun SettingRowLabel(title: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = value,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}

@Composable
private fun SegmentedChoicePill(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
        border = BorderStroke(
            1.dp,
            if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
        ),
        modifier = modifier.height(38.dp)
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(horizontal = 6.dp)) {
            Text(
                text = text,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                color = if (selected) {
                    MaterialTheme.colorScheme.onPrimary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                maxLines = 1
            )
        }
    }
}

@Composable
private fun ToggleSettingRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
private fun AddCustomMacroDialog(
    onDismiss: () -> Unit,
    onSave: (label: String, unicode: String, latex: String, desc: String) -> Unit
) {
    var label by remember { mutableStateOf("") }
    var unicode by remember { mutableStateOf("") }
    var latex by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(R.string.dialog_custom_key_title),
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = label,
                    onValueChange = { label = it },
                    label = { Text(stringResource(R.string.dialog_key_label)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = unicode,
                    onValueChange = { unicode = it },
                    label = { Text(stringResource(R.string.dialog_key_unicode)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = latex,
                    onValueChange = { latex = it },
                    label = { Text(stringResource(R.string.dialog_key_latex)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = desc,
                    onValueChange = { desc = it },
                    label = { Text(stringResource(R.string.dialog_key_desc)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(label, unicode, latex, desc) },
                enabled = label.isNotBlank() || unicode.isNotBlank()
            ) {
                Text(stringResource(R.string.dialog_save))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.dialog_cancel))
            }
        }
    )
}
