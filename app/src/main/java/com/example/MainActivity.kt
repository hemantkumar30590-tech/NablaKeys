package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.outlined.Functions
import androidx.compose.material.icons.outlined.Keyboard
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.ScientificCatalog
import com.example.ui.AppDestination
import com.example.ui.MainViewModel
import com.example.ui.screens.LayoutStudioScreen
import com.example.ui.screens.StudioScreen
import com.example.ui.screens.VaultScreen
import com.example.ui.theme.NablaKeysTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NablaKeysTheme(darkTheme = true) {
                NablaKeysApp()
            }
        }
    }
}

@Composable
private fun NablaKeysApp(
    viewModel: MainViewModel = viewModel()
) {
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    val destination by viewModel.currentDestination.collectAsStateWithLifecycle()
    val activeDeck by viewModel.activeDeck.collectAsStateWithLifecycle()
    val studioValue by viewModel.studioValue.collectAsStateWithLifecycle()
    val canUndo by viewModel.canUndo.collectAsStateWithLifecycle()
    val vaultQuery by viewModel.vaultQuery.collectAsStateWithLifecycle()
    val vaultFilter by viewModel.vaultFilter.collectAsStateWithLifecycle()
    val imeStatus by viewModel.imeStatus.collectAsStateWithLifecycle()
    val toastMessage by viewModel.toastMessage.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.refreshImeStatus()
    }

    LaunchedEffect(toastMessage) {
        toastMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearToast()
        }
    }

    BackHandler(enabled = destination != AppDestination.STUDIO) {
        viewModel.selectDestination(AppDestination.STUDIO)
    }

    val navItems = listOf(
        Triple(
            AppDestination.STUDIO,
            stringResource(R.string.nav_studio),
            Icons.Filled.Keyboard to Icons.Outlined.Keyboard
        ),
        Triple(
            AppDestination.VAULT,
            stringResource(R.string.nav_vault),
            Icons.Filled.Functions to Icons.Outlined.Functions
        ),
        Triple(
            AppDestination.LAYOUT,
            stringResource(R.string.nav_layout),
            Icons.Filled.Tune to Icons.Outlined.Tune
        )
    )

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        val useNavRail = maxWidth >= 700.dp

        Scaffold(
            contentWindowInsets = WindowInsets.safeDrawing,
            containerColor = MaterialTheme.colorScheme.background,
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            bottomBar = {
                if (!useNavRail) {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surface,
                        tonalElevation = 2.dp
                    ) {
                        navItems.forEach { (dest, label, icons) ->
                            val selected = dest == destination
                            NavigationBarItem(
                                selected = selected,
                                onClick = { viewModel.selectDestination(dest) },
                                icon = {
                                    Icon(
                                        imageVector = if (selected) icons.first else icons.second,
                                        contentDescription = label
                                    )
                                },
                                label = { Text(label) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                                    selectedTextColor = MaterialTheme.colorScheme.primary,
                                    indicatorColor = MaterialTheme.colorScheme.primary,
                                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                                ),
                                modifier = Modifier.testTag("nav_${dest.name.lowercase()}")
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                if (useNavRail) {
                    NavigationRail(
                        containerColor = MaterialTheme.colorScheme.surface,
                        modifier = Modifier.statusBarsPadding()
                    ) {
                        navItems.forEach { (dest, label, icons) ->
                            val selected = dest == destination
                            NavigationRailItem(
                                selected = selected,
                                onClick = { viewModel.selectDestination(dest) },
                                icon = {
                                    Icon(
                                        imageVector = if (selected) icons.first else icons.second,
                                        contentDescription = label
                                    )
                                },
                                label = { Text(label) },
                                colors = NavigationRailItemDefaults.colors(
                                    selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                                    selectedTextColor = MaterialTheme.colorScheme.primary,
                                    indicatorColor = MaterialTheme.colorScheme.primary,
                                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                                ),
                                modifier = Modifier.testTag("nav_${dest.name.lowercase()}")
                            )
                        }
                    }
                }

                Box(modifier = Modifier.weight(1f)) {
                    when (destination) {
                        AppDestination.STUDIO -> StudioScreen(
                            settings = settings,
                            activeDeck = activeDeck,
                            studioValue = studioValue,
                            canUndo = canUndo,
                            imeStatus = imeStatus,
                            onUpdateStudioValue = viewModel::updateStudioValue,
                            onSelectDeck = viewModel::selectDeck,
                            onInsertText = viewModel::insertIntoStudio,
                            onInsertTemplate = viewModel::insertTemplate,
                            onBackspace = viewModel::backspaceStudio,
                            onMoveCursor = viewModel::moveStudioCursor,
                            onUndo = viewModel::undoStudio,
                            onClear = viewModel::clearStudio,
                            onToggleOutputMode = viewModel::toggleOutputMode,
                            onOpenImeSetup = { viewModel.selectDestination(AppDestination.LAYOUT) },
                            onCopiedFeedback = viewModel::showToast
                        )

                        AppDestination.VAULT -> VaultScreen(
                            settings = settings,
                            query = vaultQuery,
                            selectedFilter = vaultFilter,
                            onQueryChange = viewModel::setVaultQuery,
                            onFilterChange = viewModel::setVaultFilter,
                            onTogglePin = { id ->
                                viewModel.togglePinnedSymbol(id)
                                viewModel.showToast("Updated Quick-Access Bar")
                            },
                            onInsertSymbol = { sym ->
                                val out = ScientificCatalog.formatInsertion(sym, settings, shifted = false)
                                viewModel.insertIntoStudio(out)
                                viewModel.selectDestination(AppDestination.STUDIO)
                            },
                            onInsertTemplate = { tpl ->
                                viewModel.insertTemplate(tpl)
                                viewModel.selectDestination(AppDestination.STUDIO)
                            },
                            onCopyFeedback = viewModel::showToast
                        )

                        AppDestination.LAYOUT -> LayoutStudioScreen(
                            settings = settings,
                            imeStatus = imeStatus,
                            onOutputModeChange = viewModel::setOutputMode,
                            onConstantInsertModeChange = viewModel::setConstantInsertMode,
                            onColumnsChange = viewModel::setColumnsPerRow,
                            onKeyHeightChange = viewModel::setKeyHeightPreset,
                            onKeyCornerChange = viewModel::setKeyCornerPreset,
                            onColorwayChange = viewModel::setColorway,
                            onToggleDeckEnabled = viewModel::toggleDeckEnabled,
                            onDefaultDeckChange = viewModel::setDefaultDeck,
                            onMovePinned = viewModel::movePinnedSymbol,
                            onRemovePinned = viewModel::togglePinnedSymbol,
                            onAddCustomSymbol = viewModel::addCustomSymbol,
                            onRemoveCustomSymbol = viewModel::removeCustomSymbol,
                            onHapticChange = viewModel::setHapticEnabled,
                            onKeyPopupChange = viewModel::setShowKeyPopup,
                            onLatexHintsChange = viewModel::setShowLatexHints,
                            onAutoSpaceChange = viewModel::setAutoSpaceOperators,
                            onShowPinnedBarChange = viewModel::setShowPinnedBar,
                            onShowNumberRowChange = viewModel::setShowNumberRowOnQwerty,
                            onResetDefaults = viewModel::resetSettingsToDefaults
                        )
                    }
                }
            }
        }
    }
}
