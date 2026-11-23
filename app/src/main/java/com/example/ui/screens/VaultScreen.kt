package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.FormulaTemplate
import com.example.data.KeyDeck
import com.example.data.KeyboardLayoutSettings
import com.example.data.OutputMode
import com.example.data.ScientificCatalog
import com.example.data.ScientificSymbol
import com.example.ui.VaultFilter
import com.example.ui.theme.MathMonoFontFamily

@Composable
fun VaultScreen(
    settings: KeyboardLayoutSettings,
    query: String,
    selectedFilter: VaultFilter,
    onQueryChange: (String) -> Unit,
    onFilterChange: (VaultFilter) -> Unit,
    onTogglePin: (String) -> Unit,
    onInsertSymbol: (ScientificSymbol) -> Unit,
    onInsertTemplate: (FormulaTemplate) -> Unit,
    onCopyFeedback: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val clipboard = LocalClipboardManager.current
    val allSymbols = remember(settings.customSymbols) {
        settings.customSymbols + ScientificCatalog.allBuiltInSymbols
    }
    val pinnedSet = remember(settings.pinnedSymbolIds) { settings.pinnedSymbolIds.toSet() }

    val filteredSymbols = remember(allSymbols, query, selectedFilter) {
        if (selectedFilter == VaultFilter.TEMPLATES) {
            emptyList()
        } else {
            val deckFiltered = when (selectedFilter) {
                VaultFilter.ALL -> allSymbols
                VaultFilter.CALCULUS -> allSymbols.filter { it.deck == KeyDeck.CALCULUS }
                VaultFilter.CONSTANTS -> allSymbols.filter { it.deck == KeyDeck.CONSTANTS }
                VaultFilter.GREEK -> allSymbols.filter { it.deck == KeyDeck.GREEK }
                VaultFilter.LOGIC -> allSymbols.filter { it.deck == KeyDeck.LOGIC || it.deck == KeyDeck.SCRIPTS }
                VaultFilter.TEMPLATES -> emptyList()
            }
            val q = query.trim().lowercase()
            if (q.isEmpty()) deckFiltered
            else deckFiltered.filter { s ->
                s.name.lowercase().contains(q) ||
                    s.label.lowercase().contains(q) ||
                    s.unicode.lowercase().contains(q) ||
                    s.latex.lowercase().contains(q) ||
                    (s.numericValue?.lowercase()?.contains(q) == true) ||
                    (s.units?.lowercase()?.contains(q) == true)
            }
        }
    }

    val filteredTemplates = remember(query, selectedFilter) {
        if (selectedFilter != VaultFilter.TEMPLATES && selectedFilter != VaultFilter.ALL) {
            emptyList()
        } else {
            val q = query.trim().lowercase()
            if (q.isEmpty()) ScientificCatalog.formulaTemplates
            else ScientificCatalog.formulaTemplates.filter { t ->
                t.title.lowercase().contains(q) ||
                    t.domain.lowercase().contains(q) ||
                    t.unicode.lowercase().contains(q) ||
                    t.latex.lowercase().contains(q)
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 840.dp)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(R.string.vault_title),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = stringResource(R.string.vault_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Search bar
            OutlinedTextField(
                value = query,
                onValueChange = onQueryChange,
                singleLine = true,
                placeholder = {
                    Text(
                        text = stringResource(R.string.vault_search_placeholder),
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(onClick = { onQueryChange("") }) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = stringResource(R.string.studio_clear_action)
                            )
                        }
                    }
                },
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("vault_search_input")
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Filter Chips Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                VaultFilter.entries.forEach { filter ->
                    val selected = filter == selectedFilter
                    FilterChip(
                        selected = selected,
                        onClick = { onFilterChange(filter) },
                        label = {
                            Text(
                                text = filter.label,
                                style = MaterialTheme.typography.labelLarge
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = selected,
                            borderColor = MaterialTheme.colorScheme.outline,
                            selectedBorderColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier.testTag("vault_filter_${filter.name.lowercase()}")
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (filteredSymbols.isEmpty() && filteredTemplates.isEmpty()) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(32.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "∅",
                            fontFamily = MathMonoFontFamily,
                            fontSize = 42.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = stringResource(R.string.vault_empty_title),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = stringResource(R.string.vault_empty_body),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(top = 4.dp, bottom = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (filteredTemplates.isNotEmpty() && selectedFilter == VaultFilter.TEMPLATES) {
                        items(filteredTemplates, key = { it.id }) { tpl ->
                            TemplateVaultCard(
                                template = tpl,
                                outputMode = settings.outputMode,
                                onInsert = { onInsertTemplate(tpl) },
                                onCopyLatex = {
                                    val displayed = if (settings.outputMode == OutputMode.LATEX) {
                                        tpl.latex
                                    } else {
                                        tpl.unicode
                                    }
                                    clipboard.setText(AnnotatedString(displayed))
                                    onCopyFeedback("Copied formula for ${tpl.title}")
                                }
                            )
                        }
                    }

                    items(filteredSymbols, key = { it.id }) { symbol ->
                        SymbolVaultCard(
                            symbol = symbol,
                            isPinned = symbol.id in pinnedSet,
                            onTogglePin = { onTogglePin(symbol.id) },
                            onInsert = { onInsertSymbol(symbol) },
                            onCopyValue = { value ->
                                clipboard.setText(AnnotatedString(value))
                                onCopyFeedback("Copied ${symbol.label} = $value")
                            },
                            onCopyLatex = {
                                clipboard.setText(AnnotatedString(symbol.latex))
                                onCopyFeedback("Copied ${symbol.latex}")
                            }
                        )
                    }

                    if (filteredTemplates.isNotEmpty() && selectedFilter == VaultFilter.ALL) {
                        item(key = "templates_header") {
                            Text(
                                text = "CALCULUS & PHYSICS TEMPLATES",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(top = 8.dp, bottom = 2.dp)
                            )
                        }
                        items(filteredTemplates, key = { it.id }) { tpl ->
                            TemplateVaultCard(
                                template = tpl,
                                outputMode = settings.outputMode,
                                onInsert = { onInsertTemplate(tpl) },
                                onCopyLatex = {
                                    val displayed = if (settings.outputMode == OutputMode.LATEX) {
                                        tpl.latex
                                    } else {
                                        tpl.unicode
                                    }
                                    clipboard.setText(AnnotatedString(displayed))
                                    onCopyFeedback("Copied formula for ${tpl.title}")
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SymbolVaultCard(
    symbol: ScientificSymbol,
    isPinned: Boolean,
    onTogglePin: () -> Unit,
    onInsert: () -> Unit,
    onCopyValue: (String) -> Unit,
    onCopyLatex: () -> Unit
) {
    val badgeAccent = when {
        symbol.isCustom -> MaterialTheme.colorScheme.tertiary
        symbol.deck == KeyDeck.CONSTANTS -> MaterialTheme.colorScheme.secondary
        symbol.deck == KeyDeck.CALCULUS -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.onSurface
    }

    Surface(
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(
            width = 1.dp,
            color = if (isPinned) MaterialTheme.colorScheme.primary.copy(alpha = 0.5f) else MaterialTheme.colorScheme.outlineVariant
        ),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("vault_card_${symbol.id}")
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Machined Keycap Badge
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(11.dp))
                        .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                        .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(11.dp))
                ) {
                    Text(
                        text = symbol.label,
                        color = badgeAccent,
                        fontFamily = MathMonoFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = if (symbol.label.length > 2) 14.sp else 22.sp,
                        maxLines = 1
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = symbol.name,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = symbol.latex,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "• ${symbol.deck.displayName}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    if (symbol.numericValue != null) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = buildString {
                                append(symbol.numericValue)
                                if (!symbol.units.isNullOrBlank()) {
                                    append("  ")
                                    append(symbol.units)
                                }
                            },
                            fontFamily = MathMonoFontFamily,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }

                // Pin Toggle Button (48dp touch target)
                IconButton(
                    onClick = onTogglePin,
                    modifier = Modifier.testTag("pin_btn_${symbol.id}")
                ) {
                    Icon(
                        imageVector = if (isPinned) Icons.Filled.PushPin else Icons.Outlined.PushPin,
                        contentDescription = stringResource(
                            if (isPinned) R.string.vault_unpin_action else R.string.vault_pin_action
                        ),
                        tint = if (isPinned) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Action Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SmallActionChip(
                    label = stringResource(R.string.vault_insert_action),
                    icon = Icons.Filled.Add,
                    highlighted = true,
                    onClick = onInsert
                )
                if (symbol.numericValue != null) {
                    SmallActionChip(
                        label = stringResource(R.string.vault_copy_value_action),
                        icon = Icons.Filled.ContentCopy,
                        highlighted = false,
                        onClick = { onCopyValue(symbol.numericValue) }
                    )
                }
                SmallActionChip(
                    label = stringResource(R.string.vault_copy_latex_action),
                    icon = Icons.Filled.ContentCopy,
                    highlighted = false,
                    onClick = onCopyLatex
                )
            }
        }
    }
}

@Composable
private fun TemplateVaultCard(
    template: FormulaTemplate,
    outputMode: OutputMode,
    onInsert: () -> Unit,
    onCopyLatex: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = template.title,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = template.domain.uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                    .padding(horizontal = 10.dp, vertical = 8.dp)
            ) {
                Text(
                    text = if (outputMode == OutputMode.LATEX) template.latex else template.unicode,
                    fontFamily = MathMonoFontFamily,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                SmallActionChip(
                    label = "Insert to Studio",
                    icon = Icons.Filled.Add,
                    highlighted = true,
                    onClick = onInsert
                )
                SmallActionChip(
                    label = stringResource(R.string.studio_copy_action),
                    icon = Icons.Filled.ContentCopy,
                    highlighted = false,
                    onClick = onCopyLatex
                )
            }
        }
    }
}

@Composable
private fun SmallActionChip(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    highlighted: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(9.dp),
        color = if (highlighted) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surfaceVariant
        },
        border = BorderStroke(
            1.dp,
            if (highlighted) MaterialTheme.colorScheme.primary.copy(alpha = 0.5f) else MaterialTheme.colorScheme.outlineVariant
        ),
        modifier = Modifier.height(32.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier.padding(horizontal = 10.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (highlighted) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = if (highlighted) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
        }
    }
}
