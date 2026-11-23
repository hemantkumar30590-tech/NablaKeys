package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.CasioStyle
import com.example.data.FormulaTemplate
import com.example.data.KeyDeck
import com.example.data.KeyboardLayoutSettings
import com.example.data.OutputMode
import com.example.data.ScientificCatalog
import com.example.ime.ImeStatus
import com.example.ui.CasioNaturalPreview
import com.example.ui.keyboard.ScientificKeyboardView
import com.example.ui.theme.DisplayFontFamily
import com.example.ui.theme.MathMonoFontFamily

@Suppress("DEPRECATION")
@Composable
fun StudioScreen(
    settings: KeyboardLayoutSettings,
    activeDeck: KeyDeck,
    studioValue: TextFieldValue,
    canUndo: Boolean,
    imeStatus: ImeStatus,
    onUpdateStudioValue: (TextFieldValue) -> Unit,
    onSelectDeck: (KeyDeck) -> Unit,
    onInsertText: (String) -> Unit,
    onInsertAtCursor: (String, Int) -> Unit,
    onInsertTemplate: (FormulaTemplate) -> Unit,
    onBackspace: () -> Unit,
    onMoveCursor: (Int) -> Unit,
    onUndo: () -> Unit,
    onClear: () -> Unit,
    onToggleOutputMode: () -> Unit,
    onOpenImeSetup: () -> Unit,
    onCopiedFeedback: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val clipboard = LocalClipboardManager.current
    val copiedToast = stringResource(R.string.studio_copied_toast)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Instrument Header + Scratchpad
        Column(
            modifier = Modifier
                .weight(1f)
                .widthIn(max = 840.dp)
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StudioHeaderBar(
                outputMode = settings.outputMode,
                imeStatus = imeStatus,
                onToggleOutputMode = onToggleOutputMode,
                onOpenImeSetup = onOpenImeSetup
            )

            // Mathematical Scratchpad Canvas
            val gridLineColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.22f)
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .drawBehind {
                            val step = 24.dp.toPx()
                            var x = step
                            while (x < size.width) {
                                drawLine(
                                    color = gridLineColor,
                                    start = Offset(x, 0f),
                                    end = Offset(x, size.height),
                                    strokeWidth = 1f
                                )
                                x += step
                            }
                            var y = step
                            while (y < size.height) {
                                drawLine(
                                    color = gridLineColor,
                                    start = Offset(0f, y),
                                    end = Offset(size.width, y),
                                    strokeWidth = 1f
                                )
                                y += step
                            }
                        }
                        .padding(12.dp)
                ) {
                    // Scratchpad top utility strip
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(7.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (settings.outputMode == OutputMode.LATEX) {
                                            MaterialTheme.colorScheme.primary
                                        } else {
                                            MaterialTheme.colorScheme.secondary
                                        }
                                    )
                            )
                            Text(
                                text = "${settings.outputMode.label.uppercase()} SCRATCHPAD · ${
                                    stringResource(R.string.studio_char_count, studioValue.text.length)
                                }",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            IconButton(
                                onClick = onUndo,
                                enabled = canUndo,
                                modifier = Modifier
                                    .size(36.dp)
                                    .testTag("studio_undo_button")
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Undo,
                                    contentDescription = stringResource(R.string.studio_undo_action),
                                    tint = if (canUndo) {
                                        MaterialTheme.colorScheme.onSurface
                                    } else {
                                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.35f)
                                    },
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            IconButton(
                                onClick = {
                                    if (studioValue.text.isNotEmpty()) {
                                        clipboard.setText(AnnotatedString(studioValue.text))
                                        onCopiedFeedback(copiedToast)
                                    }
                                },
                                enabled = studioValue.text.isNotEmpty(),
                                modifier = Modifier
                                    .size(36.dp)
                                    .testTag("studio_copy_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.ContentCopy,
                                    contentDescription = stringResource(R.string.studio_copy_action),
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            IconButton(
                                onClick = {
                                    if (studioValue.text.isNotEmpty()) {
                                        clipboard.setText(AnnotatedString(CasioStyle.toLatexFractions(studioValue.text)))
                                        onCopiedFeedback(stringResource(R.string.studio_copied_latex_toast))
                                    }
                                },
                                enabled = studioValue.text.isNotEmpty(),
                                modifier = Modifier
                                    .size(36.dp)
                                    .testTag("studio_copy_latex_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Code,
                                    contentDescription = stringResource(R.string.studio_copy_latex_action),
                                    tint = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            IconButton(
                                onClick = onClear,
                                enabled = studioValue.text.isNotEmpty(),
                                modifier = Modifier
                                    .size(36.dp)
                                    .testTag("studio_clear_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.DeleteOutline,
                                    contentDescription = stringResource(R.string.studio_clear_action),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Cursor-controlled editor with soft keyboard suppressed so docked Nabla keyboard stays active
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                    ) {
                        if (studioValue.text.isNotEmpty()) {
                            CasioNaturalPreview(
                                text = studioValue.text,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Text(
                                text = stringResource(R.string.studio_raw_input_label),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        if (studioValue.text.isEmpty()) {
                            Text(
                                text = stringResource(R.string.studio_scratchpad_placeholder),
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontFamily = MathMonoFontFamily,
                                    fontSize = 15.sp
                                ),
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.55f)
                            )
                        }

                        CompositionLocalProvider(LocalTextInputService provides null) {
                            BasicTextField(
                                value = studioValue,
                                onValueChange = onUpdateStudioValue,
                                textStyle = TextStyle(
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontFamily = MathMonoFontFamily,
                                    fontSize = 19.sp,
                                    lineHeight = 28.sp,
                                    fontWeight = FontWeight.Medium
                                ),
                                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("studio_equation_input")
                            )
                        }
                    }
                }
            }

            // Quick Calculus Formula Templates Strip
            QuickTemplateStrip(
                templates = ScientificCatalog.formulaTemplates,
                onSelectTemplate = onInsertTemplate
            )
        }

        // Docked Interactive Scientific Keyboard
        Box(
            modifier = Modifier
                .widthIn(max = 840.dp)
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant
                )
        ) {
            ScientificKeyboardView(
                settings = settings,
                activeDeck = activeDeck,
                onSelectDeck = onSelectDeck,
                onInsertText = onInsertText,
                onInsertTemplate = onInsertAtCursor,
                onBackspace = onBackspace,
                onEnter = { onInsertText("\n") },
                onMoveCursor = onMoveCursor,
                onToggleOutputMode = onToggleOutputMode,
                showImeSwitchKey = false,
                applyNavigationBarPadding = false
            )
        }
    }
}

@Composable
private fun StudioHeaderBar(
    outputMode: OutputMode,
    imeStatus: ImeStatus,
    onToggleOutputMode: () -> Unit,
    onOpenImeSetup: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(34.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.6f), RoundedCornerShape(10.dp))
            ) {
                Text(
                    text = "∇",
                    color = MaterialTheme.colorScheme.primary,
                    fontFamily = MathMonoFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 19.sp
                )
            }
            Column {
                Text(
                    text = stringResource(R.string.studio_title),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Hold keys with • for alternates",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Unicode / LaTeX segmented pill
            Surface(
                onClick = onToggleOutputMode,
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                modifier = Modifier
                    .height(34.dp)
                    .testTag("studio_mode_toggle")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 3.dp)
                ) {
                    ModePillSegment(
                        text = stringResource(R.string.studio_mode_unicode),
                        selected = outputMode == OutputMode.UNICODE
                    )
                    ModePillSegment(
                        text = stringResource(R.string.studio_mode_latex),
                        selected = outputMode == OutputMode.LATEX
                    )
                }
            }

            // System IME Status Pill
            Surface(
                onClick = onOpenImeSetup,
                shape = RoundedCornerShape(10.dp),
                color = if (imeStatus.isSelected) {
                    MaterialTheme.colorScheme.tertiaryContainer
                } else {
                    MaterialTheme.colorScheme.secondaryContainer
                },
                modifier = Modifier
                    .height(34.dp)
                    .testTag("studio_ime_badge")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.padding(horizontal = 9.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Keyboard,
                        contentDescription = stringResource(R.string.studio_ime_setup_action),
                        tint = if (imeStatus.isSelected) {
                            MaterialTheme.colorScheme.tertiary
                        } else {
                            MaterialTheme.colorScheme.secondary
                        },
                        modifier = Modifier.size(15.dp)
                    )
                    Text(
                        text = if (imeStatus.isSelected) "IME On" else "IME",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (imeStatus.isSelected) {
                            MaterialTheme.colorScheme.onTertiaryContainer
                        } else {
                            MaterialTheme.colorScheme.onSecondaryContainer
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ModePillSegment(text: String, selected: Boolean) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .clip(RoundedCornerShape(7.dp))
            .background(if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            fontFamily = DisplayFontFamily,
            fontSize = 11.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun QuickTemplateStrip(
    templates: List<FormulaTemplate>,
    onSelectTemplate: (FormulaTemplate) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(end = 2.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Functions,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = stringResource(R.string.studio_templates_label),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        templates.forEach { tpl ->
            Surface(
                onClick = { onSelectTemplate(tpl) },
                shape = RoundedCornerShape(9.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                modifier = Modifier
                    .height(30.dp)
                    .testTag("template_${tpl.id}")
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.padding(horizontal = 10.dp)
                ) {
                    Text(
                        text = tpl.shortLabel,
                        fontFamily = MathMonoFontFamily,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}
